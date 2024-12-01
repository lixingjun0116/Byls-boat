package com.byls.boat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.hardware.BoatHardWareContext;
import com.byls.boat.entity.hardware.MotorInfo;
import com.byls.boat.entity.hardware.SensorInfo;
import com.byls.boat.service.IBoatNavigationRecordsService;
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
    private IBoatNavigationRecordsService boatNavigationRecordsService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private WebSocketSession hardwareSession;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        log.info("建立WebSocket连接: " + session.getId());

        // 如果是硬件设备连接，保存会话
        if (Objects.requireNonNull(session.getUri()).getPath().endsWith("/hardware")) {
            hardwareSession = session;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
        log.info("WebSocket连接已关闭: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("接收到消息: " + payload);

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
                    redisUtil.set(RedisKeyConstants.INTEGRATED_NAVIGATION_INFO, jsonData);
                    break;
                case "A02":
                    List<MotorInfo> motorInfos = JSON.parseObject(jsonData, new TypeReference<List<MotorInfo>>(){});
                    redisUtil.set(RedisKeyConstants.MOTOR_INFO, jsonData);
                    break;
                case "A03":
                    SensorInfo sensorInfo = JSON.parseObject(jsonData, SensorInfo.class);
                    redisUtil.set(RedisKeyConstants.SENSOR_INFO, jsonData);
                    break;
                default:
                    log.error("未知的标识: " + keyId);
            }
        } catch (Exception e) {
            log.error("存储设备数据失败: " + e);
        }
    }

}
