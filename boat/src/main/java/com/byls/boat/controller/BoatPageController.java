package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.constant.BoatType;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.service.IRouteService;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.service.IWaypointService;
import com.byls.boat.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 页面控制、加载
 * @Date 2024/8/1 14:39
 * @Created by lxj
 */
@Slf4j
@RestController
@RequestMapping("/api/page/v1")
public class BoatPageController {

    @Autowired
    private IUnmannedShipService unmannedShipService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IWaypointService waypointService;

    //船设备列表
    @GetMapping("/boatDeviceList")
    public ResponseResult<?> boatDeviceList() {
        try {
            return ResponseUtil.successResponse(unmannedShipService.getUnmannedShipList());
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //航线列表
    @GetMapping("/routeList")
    public ResponseResult<?> routeList() {
        try {
            return ResponseUtil.successResponse(routeService.getRoutes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.failResponse();
        }
    }

    //根据航线获取航点信息
    @GetMapping("/waypointList")
    public ResponseResult<?> waypointList(@RequestParam String routeCode) {
        try {
            Waypoint waypoint = new Waypoint();
            waypoint.setRouteCode(routeCode);
            return ResponseUtil.successResponse(waypointService.getWaypointByCondition(waypoint));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.failResponse();
        }
    }

    //获取船类型列表
    @GetMapping("/shipTypeList")
    public ResponseResult<?> shipTypeList() {
        try {
            return ResponseUtil.successResponse(BoatType.values());
        } catch (Exception e) {
            log.error("获取船类型列表失败: {}", String.valueOf(e));
            return ResponseUtil.failResponse();
        }
    }

    //根据船类型获取船列表
    @GetMapping("/boatListByType")
    public ResponseResult<?> boatListByType(@RequestParam String shipType) {
        try {
            return ResponseUtil.successResponse(unmannedShipService.getUnmannedShipsByType(BoatType.fromType(shipType)));
        } catch (Exception e) {
            log.error("根据船类型获取船列表失败: {}", String.valueOf(e));
            return ResponseUtil.failResponse();
        }
    }

    //获取所有船的实时坐标
    @GetMapping("/boatLocationList")
    public ResponseResult<?> boatLocationList() {
        try {
            return ResponseUtil.successResponse(unmannedShipService.getAllBoatLocation());
        } catch (Exception e) {
            log.error("获取所有船的实时坐标失败: {}", String.valueOf(e));
            return ResponseUtil.failResponse();
        }
    }
}
