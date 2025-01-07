package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.common.WebSocketHelper;
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
import com.byls.boat.service.catchhandler.CacheCenter;
import com.byls.boat.util.RedisUtil;
import com.byls.boat.vo.BoatRouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoatCourseMakingServiceImpl extends ServiceImpl<BoatCourseMakingMapper, BoatCourseMaking> implements IBoatCourseMakingService {

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IWaypointService waypointService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheCenter cacheCenter;

    private final WebSocketHelper webSocketHelper;


    public BoatCourseMakingServiceImpl(WebSocketHelper webSocketHelper) {
        this.webSocketHelper = webSocketHelper;
    }

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
    public List<BoatCourseMaking> getAllCoursePoints(String boatDeviceId) {
        try {
            LambdaQueryWrapper<BoatCourseMaking> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BoatCourseMaking::getDeleteStatus, false);
            //船设备id
            queryWrapper.eq(BoatCourseMaking::getBoatDeviceId, boatDeviceId);
            //根据下标 升序排列
            queryWrapper.orderByAsc(BoatCourseMaking::getOrderIndex);
            return this.getBaseMapper().selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取缓存航线数据失败:{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void deleteAllCoursePoints(String boatDeviceId) {
        LambdaUpdateWrapper<BoatCourseMaking> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BoatCourseMaking::getBoatDeviceId, boatDeviceId);
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
    public BoatCourseMaking collectCourseMaking(String boatDeviceId) {
        // 采集航线数据   每点击一次采集    获取缓存中硬件设备传的gps定位   存入一条到航线制作表    将这条数据返回页面
        try {
            String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
            String redisKey = deviceType + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + boatDeviceId;
            String navigationData = redisUtil.getByType(BoatType.fromType(deviceType), redisKey);
            if (navigationData == null) {
                log.error("获取缓存导航数据失败:{}", redisKey);
                return null;
            }
            BoatNavigationRecords newRecord = JSON.parseObject(navigationData, BoatNavigationRecords.class);
            BoatCourseMaking course = new BoatCourseMaking();
            course.setBoatDeviceId(boatDeviceId);
            course.setLatitude(newRecord.getLatitude());
            course.setLongitude(newRecord.getLongitude());
            //获取数据库总条数
            course.setOrderIndex(countByDeleteStatus(boatDeviceId) + 1);
            course.setCreatedTime(new Date());
            course.setUpdatedTime(new Date());
            course.setDeleteStatus(false);
            this.save(course);
            return course;
        } catch (Exception e) {
            log.error("获取缓存导航数据失败:{}", e.getMessage(), e);
        }
        log.error("获取缓存航线数据失败,返回空");
        return null;
    }

    private int countByDeleteStatus(String boatDeviceId) {
        QueryWrapper<BoatCourseMaking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("boat_device_id", boatDeviceId);
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
            /*deleteAllRouteByDeviceId(boatRouteVO.getBoatDeviceId());*/
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
        } catch (Exception e) {
            log.error("保存航线数据失败:{}", e.getMessage(), e);
        }
        // 如果Route保存失败，直接返回false
        return false;
    }

    //判断是否有效航线
    @Override
    public boolean isExistValidRoute(String boatDeviceId, String routeName) {
        QueryWrapper<Route> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("boat_device_id", boatDeviceId);
        queryWrapper.eq("route_name", routeName);
        queryWrapper.eq("status", 0);
        return routeService.count(queryWrapper) > 0;
    }

    //当前是否正在录制航点
    @Override
    public boolean isRecording(String boatDeviceId) {
        //判断redis中是否有该设备的录制状态
        boolean b = redisUtil.hasKey(RedisKeyConstants.RECORD_POINT_STATUS + ":" + boatDeviceId);
        if (b) {
            String value = redisUtil.getValue(RedisKeyConstants.RECORD_POINT_STATUS + ":" + boatDeviceId);
            return "1".equals(value);
        }
        return false;
    }

    // 设置录制航线状态
    @Override
    public boolean setRecordingStatus(String boatDeviceId, Integer status) {
        try {
            if (redisUtil.hasKey(RedisKeyConstants.RECORD_POINT_STATUS + ":" + boatDeviceId)) {
                String value = redisUtil.getValue(RedisKeyConstants.RECORD_POINT_STATUS + ":" + boatDeviceId);
                if (String.valueOf(status).equals(value)) {
                    log.error("{}:{}设置录制航线状态失败-状态已存在:{}", boatDeviceId, status, value);
                    return false;
                }
            }
            // 发送指令到船控端
            String command = "{\"function\":\"C1004\",\"action\":\"" + status + "\"}";
            webSocketHelper.sendMessageToBoat(boatDeviceId, command);
            redisUtil.setValue(RedisKeyConstants.RECORD_POINT_STATUS + ":" + boatDeviceId, String.valueOf(status));
            log.info("设置录制航线状态成功:{}:{}", boatDeviceId, status);
            return true;
        } catch (Exception e) {
            log.error("设置录制航线状态失败:{}", e.getMessage(), e);
            return false;
        }

    }

    //发送航点信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean sendRouteInfo(String routeCode) {
        try {
            // 1.判断航路是否有已经启用的航路了
            Route routeByRouteCode = routeService.getRouteByRouteCode(routeCode);
            if (routeByRouteCode == null) {
                log.error("获取航路信息失败:{}", routeCode);
                return false;
            }
            String boatDeviceId = routeByRouteCode.getBoatDeviceId();
            List<Route> routesByBoatDeviceId = routeService.getRoutesByBoatDeviceId(boatDeviceId);
            for (Route r : routesByBoatDeviceId) {
                if (r.getEnableStatus()) {
                    Route routeUpEnable = new Route();
                    routeUpEnable.setId(r.getId());
                    routeUpEnable.setEnableStatus(false);
                    routeUpEnable.setDescription("给该船更换航线，启用状态改为未启用");
                    routeService.updateRouteById(routeUpEnable);
                }
            }

            // 2.构造航点数据 发送给船端
            String routeData = constructRouteData(routeCode);
            String command = "{\"function\":\"C1003\",\"routeData\":" + routeData + "}";
            webSocketHelper.sendMessageToBoat(boatDeviceId, command);
            // 3. 条件修改航线表为启用状态
            Route route = new Route();
            route.setId(routeByRouteCode.getId());
            route.setEnableStatus(true);
            route.setDescription("给船端发送航路成功，启用状态改为已启用");
            return routeService.updateRouteById(route);
        } catch (Exception e) {
            log.error("给船端发送航路失败:{},开始回滚数据库操作", e.getMessage());
        }
        return false;
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

    //组织返回的航点数据
    private String constructRouteData(String routeCode) {
        try {
            Waypoint waypoint = new Waypoint();
            waypoint.setRouteCode(routeCode);
            List<Waypoint> waypointByCondition = waypointService.getWaypointByCondition(waypoint);

            if (waypointByCondition != null && !waypointByCondition.isEmpty()) {
                List<Map<String, Double>> coordinates = waypointByCondition.stream()
                        .sorted(Comparator.comparing(Waypoint::getSequence))
                        .map(wp -> {
                            Map<String, Double> map = new HashMap<>();
                            map.put("longitude", wp.getLongitude());
                            map.put("latitude", wp.getLatitude());
                            return map;
                        })
                        .collect(Collectors.toList());
                return com.alibaba.fastjson.JSON.toJSONString(coordinates);
            }
        } catch (Exception e) {
            log.error("构造航路信息失败: {}", String.valueOf(e));
        }
        return "[]";
    }

}
