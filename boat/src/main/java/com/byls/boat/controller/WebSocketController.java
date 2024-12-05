package com.byls.boat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.hardware.BoatHardWareContext;
import com.byls.boat.entity.hardware.IntegratedNavigationInfo;
import com.byls.boat.entity.hardware.MotorInfo;
import com.byls.boat.entity.hardware.SensorInfo;
import com.byls.boat.service.BoatDeviceTypeRelationCatcheService;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/*websocket接收各端的数据：目前接收船端姿态信息、传感器数据、发动机数据*/
@Slf4j
@Component
@RestController
public class WebSocketController extends TextWebSocketHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BoatDeviceTypeRelationCatcheService relationCatcheService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private WebSocketSession hardwareSession;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        log.info("建立WebSocket连接: {}", session.getId());

        // 如果是硬件设备连接，保存会话
        if (Objects.requireNonNull(session.getUri()).getPath().endsWith("/hardware")) {
            hardwareSession = session;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
        log.info("WebSocket连接已关闭: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("接收到消息: {}", payload);

        // 硬件设备发送的消息
        if (session.equals(hardwareSession)) {
            storeHardWareData(payload);
        }
    }

    private void storeHardWareData(String payload) {
        try {
            if (payload == null || payload.isEmpty()) {
                log.error("payload 不能为空");
                return;
            }

            BoatHardWareContext hardWareContext = JSON.parseObject(payload, BoatHardWareContext.class);
            String keyId = hardWareContext.getKeyId();
            String jsonData = hardWareContext.getJsonData();

            if (keyId == null || keyId.isEmpty()) {
                log.error("keyId数据为空");
                return;
            }
            if (jsonData == null || jsonData.isEmpty()) {
                log.error("jsonData数据为空");
                return;
            }

            // 根据不同标识符keyId，解析存储不同设备信息
            switch (keyId) {
                case "A01":
                    handleIntegratedNavigationInfo(jsonData);
                    break;
                case "A02":
                    handleMotorInfo(jsonData);
                    break;
                case "A03":
                    handleSensorInfo(jsonData);
                    break;
                default:
                    log.error("未知的标识: {}", keyId);
            }
        } catch (Exception e) {
            log.error("存储设备数据失败: " + e);
        }
    }

    private void handleIntegratedNavigationInfo(String jsonData) {
        try {
            IntegratedNavigationInfo integratedNavigationInfo = JSON.parseObject(jsonData, IntegratedNavigationInfo.class);
            if (integratedNavigationInfo == null) {
                log.error("redis转换实体后为空jsonData: {}", jsonData);
                return;
            }
            String boatDeviceId = integratedNavigationInfo.getBoatDeviceId();
            saveToRedis(boatDeviceId, RedisKeyConstants.INTEGRATED_NAVIGATION_INFO, jsonData);
        } catch (Exception e) {
            log.error("解析姿态数据失败: " + e);
        }
    }

    private void handleMotorInfo(String jsonData) {
        try {
            List<MotorInfo> motorInfos = JSON.parseObject(jsonData, new TypeReference<List<MotorInfo>>() {});
            if (motorInfos == null || motorInfos.isEmpty()) {
                log.error("redis转换实体后为空payload: {}", jsonData);
                return;
            }
            String boatDeviceId = motorInfos.get(0).getBoatDeviceId();
            saveToRedis(boatDeviceId, RedisKeyConstants.MOTOR_INFO, jsonData);
        } catch (Exception e) {
            log.error("解析发动机数据失败: {}", String.valueOf(e));
        }
    }

    private void handleSensorInfo(String jsonData) {
        try {
            SensorInfo sensorInfo = JSON.parseObject(jsonData, SensorInfo.class);
            if (sensorInfo == null) {
                log.error("redis转换实体后为空: {}", jsonData);
                return;
            }
            String deviceId = sensorInfo.getBoatDeviceId();
            saveToRedis(deviceId, RedisKeyConstants.SENSOR_INFO, jsonData);
        } catch (Exception e) {
            log.error("解析传感器数据失败: {}", String.valueOf(e));
        }
    }

    private void saveToRedis(String deviceId, String keySuffix, String jsonData) {
        String deviceTypeByBoatDeviceId = relationCatcheService.getDeviceTypeByBoatDeviceId(deviceId);
        if (deviceTypeByBoatDeviceId == null) {
            log.error("根据设备ID获取设备类型失败: {}", deviceId);
            return;
        }
        String redisKeyPrefix = deviceTypeByBoatDeviceId + ":" + keySuffix + ":" + deviceId;
        redisUtil.setByType(BoatType.valueOf(deviceTypeByBoatDeviceId), redisKeyPrefix, jsonData);
    }
}
