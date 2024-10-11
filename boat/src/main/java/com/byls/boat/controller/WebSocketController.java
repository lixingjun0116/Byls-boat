package com.byls.boat.controller;

import com.alibaba.fastjson.JSON;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.BoatNavigationRecords;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        if (session.getUri().getPath().endsWith("/hardware")) {
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

        // 硬件设备发送的消息，存储GPS数据
        if (session.equals(hardwareSession)) {
            storeGpsData(payload);
        }
    }

    private void storeGpsData(String gpsData) {
        try {
            BoatNavigationRecords boatNavigationRecords = JSON.parseObject(gpsData, BoatNavigationRecords.class);
            boatNavigationRecordsService.addNavigationRecord(boatNavigationRecords);
            //添加redis
            redisUtil.set(RedisKeyConstants.BOAT_COURSE_FLAG, gpsData);
        } catch (Exception e) {
            log.error("存储GPS数据失败: " + e);
        }
        log.info("硬件传过来的设备数据，保存本地数据库: " + gpsData);
    }

}
