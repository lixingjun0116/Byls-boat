package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.service.IRouteService;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.service.IWaypointService;
import com.byls.boat.util.ResponseUtil;
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
}
