package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.BoatCourseMaking;
import com.byls.boat.entity.BoatNavigationRecords;
import com.byls.boat.entity.Route;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.mapper.BoatCourseMakingMapper;
import com.byls.boat.service.IBoatCourseMakingService;
import com.byls.boat.service.IRouteService;
import com.byls.boat.service.IWaypointService;
import com.byls.boat.util.RedisUtil;
import com.byls.boat.vo.BoatRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class BoatCourseMakingServiceImpl extends ServiceImpl<BoatCourseMakingMapper, BoatCourseMaking> implements IBoatCourseMakingService {

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IWaypointService waypointService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加航线点并返回主键ID
     *
     * @param course 航线点实体
     * @return 主键ID
     */
    @Override
    public Integer addCoursePointAndReturnId(BoatCourseMaking course) {
        boolean isSaved = this.save(course);
        if (isSaved) {
            return course.getId();
        }
        return null;
    }


    /**
     * 更新航线点
     *
     * @param course 航线点实体
     */
    @Override
    public boolean updateCoursePoint(BoatCourseMaking course) {
        return this.updateById(course);
    }

    /**
     * 删除航线点
     *
     * @param id 航线点ID
     */
    @Override
    public boolean deleteCoursePoint(Integer id) {
        LambdaUpdateWrapper<BoatCourseMaking> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(BoatCourseMaking::getDeleteStatus, true);
        updateWrapper.eq(BoatCourseMaking::getId, id);
        return this.update(null, updateWrapper);
    }

    /**
     * 根据ID获取航线点
     *
     * @param id 航线点ID
     * @return 航线点实体
     */
    @Override
    public BoatCourseMaking getCoursePointById(Integer id) {
        return this.getById(id);
    }

    /**
     * 获取所有航线点
     *
     * @return 航线点列表
     */
    @Override
    public List<BoatCourseMaking> getAllCoursePoints() {
        try {
            LambdaQueryWrapper<BoatCourseMaking> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BoatCourseMaking::getDeleteStatus, false);
            //根据下标 升序排列
            queryWrapper.orderByAsc(BoatCourseMaking::getOrderIndex);
            return this.getBaseMapper().selectList(queryWrapper);
        }catch (Exception e) {
            log.error("获取缓存航线数据失败:{}",e);
        }
        return null;
    }

    @Override
    public void deleteAllCoursePoints() {
        LambdaUpdateWrapper<BoatCourseMaking> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(BoatCourseMaking::getDeleteStatus, false);
        updateWrapper.set(BoatCourseMaking::getDeleteStatus, true);

        this.update(null, updateWrapper);
    }

    /**
     * 航线采集
     *
     * @return BoatCourseMaking
     * @author lixingjun
     * &#064;date  2024/8/2 10:13
     */
    @Override
    public BoatCourseMaking collectCourseMaking(String shipCode) {
        // 采集航线数据   每点击一次采集    获取缓存中硬件设备传的gps定位   存入一条到航线制作表    将这条数据返回页面
        try {
            String redisKey = BoatType.TOURIST_BOAT.getType() + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + shipCode;
            String navigationData = redisUtil.getByType(BoatType.TOURIST_BOAT, redisKey);
            if (navigationData == null){
                log.error("获取缓存导航数据失败:{}"+redisKey);
                return null;
            }
            BoatNavigationRecords newRecord = JSON.parseObject(navigationData, BoatNavigationRecords.class);
            BoatCourseMaking course = new BoatCourseMaking();
            course.setLatitude(newRecord.getLatitude());
            course.setLongitude(newRecord.getLongitude());
            //获取数据库总条数
            course.setOrderIndex(countByDeleteStatus() + 1);
            course.setCreatedTime(new Date());
            course.setUpdatedTime(new Date());
            course.setDeleteStatus(false);
            this.save(course);
            return course;
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("获取缓存航线数据失败");
        return null;
    }

    private int countByDeleteStatus() {
        QueryWrapper<BoatCourseMaking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_status", false);
        return this.count(queryWrapper);
    }

    //批量更新航线制作数据
    @Override
    public boolean updateBatch(List<BoatCourseMaking> entityList) {
        if (entityList.isEmpty()) {
            return false;
        }
        //重新设置orderIndex
        for (int i = 0; i < entityList.size(); i++) {
            entityList.get(i).setOrderIndex(i + 1);
            entityList.get(i).setUpdatedTime(new Date());
        }
        return this.updateBatchById(entityList);
    }


    //保存航线-制作完成
    @Override
    public boolean saveRoute(BoatRouteVO boatRouteVO) {
        try {
            //1.替换已存在的航线（逻辑删除）
            deleteAllRouteByDeviceId(boatRouteVO.getBoatDeviceId());
            //2.填充航线信息
            Route route = createRoute(boatRouteVO);
            //3.填充航线相关航点信息
            List<Waypoint> waypoints = createWaypoints(boatRouteVO.getCourseMakingList(), route.getRouteCode());

            // 调用IRouteService的保存方法
            boolean routeSaved = routeService.save(route);

            // 如果Route保存成功，再调用IWaypointService的批量保存方法
            if (routeSaved) {
                // 如果航点也保存成功，则整个操作成功
                return waypointService.saveBatch(waypoints);
            }
        }catch (Exception e) {
            log.error("保存航线数据失败:{}",e);
        }
        // 如果Route保存失败，直接返回false
        return false;
    }

    //判断是否有效航线 status是0
    @Override
    public boolean isExistValidRoute(String boatDeviceId) {
        QueryWrapper<Route> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("boat_device_id", boatDeviceId);
        queryWrapper.eq("status", 0);
        return routeService.count(queryWrapper) > 0;
    }

    //删除已存在的航线
    private void deleteAllRouteByDeviceId(String boatDeviceId) {
        LambdaUpdateWrapper<Route> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Route::getStatus, 0);
        updateWrapper.eq(Route::getBoatDeviceId, boatDeviceId);
        updateWrapper.set(Route::getStatus, 1);
        routeService.update(null, updateWrapper);
    }

    //创建Route对象
    private Route createRoute(BoatRouteVO boatRouteVO) {
        Route route = new Route();
        String routeCode = UUID.randomUUID().toString();
        route.setRouteCode(routeCode);
        route.setRouteName(boatRouteVO.getRouteName());
        route.setBoatDeviceId(boatRouteVO.getBoatDeviceId());
        route.setDescription(boatRouteVO.getDescription());
        route.setStatus(0);
        route.setCreatedTime(new Date());
        route.setUpdatedTime(new Date());
        return route;
    }

    // 创建Waypoint列表
    private List<Waypoint> createWaypoints(List<BoatCourseMaking> entityList, String routeCode) {
        List<Waypoint> waypoints = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            BoatCourseMaking boatCourseMaking = entityList.get(i);
            Waypoint waypoint = new Waypoint();
            waypoint.setRouteCode(routeCode);
            waypoint.setLatitude(boatCourseMaking.getLatitude());
            waypoint.setLongitude(boatCourseMaking.getLongitude());
            waypoint.setRoutePointName(boatCourseMaking.getRoutePointName());
            waypoint.setSequence(i + 1);
            waypoints.add(waypoint);
        }
        return waypoints;
    }

}
