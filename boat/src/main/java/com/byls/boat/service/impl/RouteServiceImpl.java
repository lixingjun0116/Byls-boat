package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Route;
import com.byls.boat.mapper.RouteMapper;
import com.byls.boat.service.IRouteService;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.vo.RouteRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 航线表Service实现类
 */
@Slf4j
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements IRouteService {

    @Autowired
    private IUnmannedShipService unmannedShipService;

    @Override
    public List<RouteRespVO> getRoutes() {
        List<Route> list = this.list();
        List<RouteRespVO> routeRespVOList = new ArrayList<>();
        if (list != null) {
            // stream流遍历根据设备id查询设备名称 然后返回RouteRespVO
            routeRespVOList = list.stream()
                    .map(route -> {
                        RouteRespVO routeRespVO = new RouteRespVO();
                        routeRespVO.setId(route.getId());
                        routeRespVO.setRouteCode(route.getRouteCode());
                        routeRespVO.setRouteName(route.getRouteName());
                        routeRespVO.setBoatDeviceId(route.getBoatDeviceId());
                        routeRespVO.setDescription(route.getDescription());
                        routeRespVO.setCreatedTime(route.getCreatedTime());
                        routeRespVO.setUpdatedTime(route.getUpdatedTime());
                        routeRespVO.setStatus(route.getStatus());
                        routeRespVO.setEnableStatus(route.getEnableStatus());
                        try {
                            routeRespVO.setBoatDeviceName(unmannedShipService.getUnmannedShip(route.getBoatDeviceId()).getShipName());
                        } catch (Exception e) {
                            log.error("获取船名称失败: " + e);
                        }
                        return routeRespVO;
                    })
                    .collect(Collectors.toList());
        }

        return routeRespVOList;
    }

    @Override
    public List<Route> getRoutesByCondition(Route route) {
        LambdaQueryWrapper<Route> queryWrapper = new LambdaQueryWrapper<>();
        if (route != null) {
            if (route.getId() != null) {
                queryWrapper.eq(Route::getId, route.getId());
            }
            if (route.getRouteCode() != null) {
                queryWrapper.eq(Route::getRouteCode, route.getRouteCode());
            }
            if (route.getRouteName() != null) {
                queryWrapper.eq(Route::getRouteName, route.getRouteName());
            }
            if (route.getStatus() != null) {
                queryWrapper.eq(Route::getStatus, route.getStatus());
            }
            if (route.getBoatDeviceId() != null) {
                queryWrapper.eq(Route::getBoatDeviceId, route.getBoatDeviceId());
            }
            if (route.getEnableStatus() != null) {
                queryWrapper.eq(Route::getEnableStatus, route.getEnableStatus());
            }
            if (route.getDescription() != null) {
                queryWrapper.eq(Route::getDescription, route.getDescription());
            }
            if (route.getCreatedTime() != null) {
                queryWrapper.eq(Route::getCreatedTime, route.getCreatedTime());
            }
            if (route.getUpdatedTime() != null) {
                queryWrapper.eq(Route::getUpdatedTime, route.getUpdatedTime());
            }
        }
        return baseMapper.selectList(queryWrapper);
    }


    // 通过条件更新
    @Override
    public boolean updateRouteByCondition(Route route) {
        route.setUpdatedTime(new Date());
        // 根据传入的条件更新
        return this.update(route, new LambdaQueryWrapper<Route>()
                .eq(route.getId() != null, Route::getId, route.getId())
                .eq(route.getRouteCode() != null, Route::getRouteCode, route.getRouteCode())
                .eq(route.getRouteName() != null, Route::getRouteName, route.getRouteName())
                .eq(route.getBoatDeviceId() != null, Route::getBoatDeviceId, route.getBoatDeviceId())
                .eq(route.getEnableStatus() != null, Route::getEnableStatus, route.getEnableStatus())
                .eq(route.getStatus() != null, Route::getStatus, route.getStatus()));
    }

    // 通过id更新
    @Override
    public boolean updateRouteById(Route route) {
        route.setUpdatedTime(new Date());
        return updateById(route);
    }

    // 通过航线编号查询 航线信息
    @Override
    public Route getRouteByRouteCode(String routeCode) {
        if (routeCode != null) {
            return this.getOne(new LambdaQueryWrapper<Route>().eq(Route::getRouteCode, routeCode));
        }
        return new Route();
    }

    // 通过船设备id查询航线信息
    @Override
    public List<Route> getRoutesByBoatDeviceId(String boatDeviceId) {
        if (boatDeviceId != null) {
            return this.list(new LambdaQueryWrapper<Route>().eq(Route::getBoatDeviceId, boatDeviceId));
        }
        return Collections.emptyList();
    }
}
