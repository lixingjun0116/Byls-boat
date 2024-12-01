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
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateRoute(Route route) {
        return updateById(route);
    }
}
