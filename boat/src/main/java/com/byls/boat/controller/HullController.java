package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.config.PersistentHardwareWebSocketClient;
import com.byls.boat.service.BoatHullService;
import com.byls.boat.util.ResponseUtil;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/hull/v1")
public class HullController {

    @Autowired
    private BoatHullService boatHullService;
    private PersistentHardwareWebSocketClient client;
    private boolean isManualMode = true; // 默认手动模式
    private String isRunning = "stop"; // 默认停止状态

    public HullController() throws URISyntaxException {
        URI uri = new URI("ws://127.0.0.1:8081/boat/signal");
        this.client = PersistentHardwareWebSocketClient.getInstance(uri);
        this.client.connectAndReconnect();
    }

    @GetMapping("/sendToHardware")
    public ResponseResult<?> sendToHardware(@RequestParam String directionValue) {
        if (!isRunning.equals("start")) {
            return ResponseUtil.failResponse("当前状态不允许发送方向控制命令");
        }

        if (client.getReadyState() == ReadyState.OPEN) {
            switch (directionValue) {
                case "forwardPress":
                    // 处理前进按下事件
                    break;
                case "backPress":
                    // 处理后退按下事件
                    break;
                case "leftPress":
                    // 处理向左按下事件
                    break;
                case "rightPress":
                    // 处理向右按下事件
                    break;
                default:
                    return ResponseUtil.failResponse("无效操作");
            }
            client.send(directionValue);
            return ResponseUtil.successResponse(directionValue);
        } else {
            return ResponseUtil.failResponse("发送数据失败:连接未打开");
        }
    }

    //模式切换 手动、自动
    @GetMapping("/switchMode")
    public ResponseResult<?> switchMode(@RequestParam boolean manualMode) {
        isManualMode = manualMode;
        return ResponseUtil.successResponse(isManualMode);
    }

    //状态切换成运行、暂停、停止
    @GetMapping("/controlState")
    public ResponseResult<?> controlState(@RequestParam String state) {
        switch (state) {
            case "start":
                isRunning = "start";
                break;
            case "pause":
                isRunning = "pause";
                break;
            case "stop":
                isRunning = "stop";
                break;
            default:
                return ResponseUtil.failResponse("无效状态");
        }
        return ResponseUtil.successResponse(state);
    }

    //获取模式
    @GetMapping("/getCurrentMode")
    public ResponseResult<?> getCurrentMode() {
        return ResponseUtil.successResponse(isManualMode);
    }

    // 获取状态
    @GetMapping("/getCurrentState")
    public ResponseResult<?> getCurrentState() {
        return ResponseUtil.successResponse(isRunning);
    }

    //获取船实时坐标
    @GetMapping("/getBoatLocation")
    public ResponseResult<?> getBoatLocation() {
        return ResponseUtil.successResponse(boatHullService.getCurrentLocation());
    }
}
