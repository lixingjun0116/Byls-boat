package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.service.BoatHullService;
import com.byls.boat.util.ResponseUtil;
import com.byls.boat.vo.BoatPushRodVO;
import com.byls.boat.vo.SwitchModeRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 控制页面、调试页面控制层
 */
@Slf4j
@RestController
@RequestMapping("/api/hull/v1")
@Validated
public class HullController {

    @Autowired
    private BoatHullService boatHullService;

    /*private PersistentHardwareWebSocketClient client;*/

    /*public HullController() throws URISyntaxException {
        URI uri = new URI("ws://127.0.0.1:8081/boat/signal");
        this.client = PersistentHardwareWebSocketClient.getInstance(uri);
        this.client.connectAndReconnect();
    }*/

    //模式切换 0-调试 1-航路
    @PostMapping("/switchMode")
    public ResponseResult<?> switchMode(@Valid @RequestBody SwitchModeRequest request) {
        try {
            boatHullService.switchBoatControlMode(request.getBoatDeviceId(), request.getState());
            log.info("成功切换船 {} 的模式为 {}", request.getBoatDeviceId(), request.getState());
            return ResponseUtil.successResponse(request.getState());
        } catch (Exception e) {
            log.error("切换船 {} 的模式失败: {}", request.getBoatDeviceId(), e.getMessage(), e);
            return ResponseUtil.failResponse("切换模式失败");
        }
    }

    // 获取船手自动状态
    @GetMapping("/getCurrentState")
    public ResponseResult<?> getCurrentState(@RequestParam("boatDeviceId") String boatDeviceId) {
        String boatStatus = boatHullService.getBoatStatus(boatDeviceId);
        if (StringUtils.isEmpty(boatStatus)) {
            log.error("获取船编号{}状态失败,默认设置为手动状态-0", boatDeviceId);
            boatStatus = "0";
        }
        return ResponseUtil.successResponse(boatStatus);
    }

    // 获取船控模式
    @GetMapping("/getCurrentMode")
    public ResponseResult<?> getCurrentMode(@RequestParam("boatDeviceId") String boatDeviceId) {
        String boatControlMode = boatHullService.getBoatControlMode(boatDeviceId);
        if (StringUtils.isEmpty(boatControlMode)) {
            log.error("获取船编号{}模式失败,默认设置为航路模式-1", boatDeviceId);
            boatControlMode = "1";
        }
        return ResponseUtil.successResponse(boatControlMode);
    }

    // 获取船实时坐标
    @GetMapping("/getBoatLocation")
    public ResponseResult<?> getBoatLocation(@RequestParam("boatDeviceId") String boatDeviceId) {
        if (StringUtils.isNotBlank(boatDeviceId)) {
            return ResponseUtil.successResponse(boatHullService.getCurrentLocation(boatDeviceId));
        }
        log.error("获取船实时坐标失败: boatDeviceId 不能为空");
        return ResponseUtil.failResponse("boatDeviceId 不能为空");
    }

    // 控制左右推杆
    @PostMapping("/controlLeftRight")
    public ResponseResult<?> controlLeftRight(@RequestBody @Valid BoatPushRodVO boatPushRodVO) {
        try {
            boatHullService.controlLeftRightPushRod(boatPushRodVO);
            return ResponseUtil.successResponse();
        } catch (Exception e) {
            log.error("控制船 {} 的左右推杆失败: {}", boatPushRodVO.getBoatDeviceId(), e.getMessage(), e);
            return ResponseUtil.failResponse("控制推杆失败");
        }
    }

    // 查询预跑航线
    @GetMapping("/getPreRunRoute")
    public ResponseResult<?> getPreRunRoute(@RequestParam("boatDeviceId") String boatDeviceId) {
        return ResponseUtil.successResponse(boatHullService.getPreRunRoute(boatDeviceId));
    }

    // 获取船实时信息
    @GetMapping("/getBoatDynamicsInfo")
    public ResponseResult<?> getBoatDynamicsInfo(@RequestParam("boatDeviceId") String boatDeviceId) {
        return ResponseUtil.successResponse(boatHullService.showBoatDynamicsInfo(boatDeviceId));
    }

    // 获取左右推杆数值
    @GetMapping("/getLeftRightValue")
    public ResponseResult<?> getLeftRightValue(@RequestParam("boatDeviceId") String boatDeviceId) {
        if (StringUtils.isBlank(boatDeviceId)) {
            log.error("获取左右推杆数值失败: boatDeviceId 不能为空");
            return ResponseUtil.failResponse("boatDeviceId 不能为空");
        }
        return ResponseUtil.successResponse(boatHullService.getLeftRightPushRodValue(boatDeviceId));
    }
}
