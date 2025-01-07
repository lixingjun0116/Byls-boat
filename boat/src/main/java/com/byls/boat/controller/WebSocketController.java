package com.byls.boat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.BoatCourseMaking;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.entity.WebSocketMessageContext;
import com.byls.boat.service.IBoatCourseMakingService;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.service.catchhandler.CacheCenter;
import com.byls.boat.util.RateLimiter;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class WebSocketController extends TextWebSocketHandler {

    private final IUnmannedShipService unmannedShipService;
    private final CacheCenter cacheCenter;
    private final RedisUtil redisUtil;
    private final IBoatCourseMakingService courseMakingService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> boatWebSocketUrls = new ConcurrentHashMap<>();
    // 先暂定每秒最多允许有1个请求，后期如果需要不同类型数据做不同的限流就再改吧
    private final RateLimiter rateLimiter = new RateLimiter(1000);

    private final ExecutorService executorService = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    @Autowired
    public WebSocketController(IUnmannedShipService unmannedShipService, CacheCenter cacheCenter, RedisUtil redisUtil, IBoatCourseMakingService courseMakingService) {
        this.unmannedShipService = unmannedShipService;
        this.cacheCenter = cacheCenter;
        this.redisUtil = redisUtil;
        this.courseMakingService = courseMakingService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        sessions.put(uri, session);
        log.info("建立WebSocket连接: {}", session);
        String boatDeviceId = getBoatDeviceIdFromUri(uri);
        if (boatDeviceId != null) {
            boatWebSocketUrls.put(boatDeviceId, uri);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        sessions.remove(uri);
        log.info("WebSocket连接已关闭: {}", session);
        String boatDeviceId = getBoatDeviceIdFromUri(uri);
        if (boatDeviceId != null) {
            boatWebSocketUrls.remove(boatDeviceId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String clientId = session.getId();

        log.info("接收到消息: {}", payload);

        executorService.submit(() -> {
            try {
                // 解析JSON消息
                WebSocketMessageContext msg = JSON.parseObject(payload, WebSocketMessageContext.class);
                String boatDeviceId = getBoatDeviceIdFromUri(Objects.requireNonNull(session.getUri()).toString());
                String function = msg.getFunction();
                // 限流逻辑
                if (!rateLimiter.shipPushDataLimit(clientId, function)) {
                    log.warn("高频请求被限流: clientId={}, function={}, boatDeviceId={}", clientId, function, boatDeviceId);
                    return;
                }
                switch (msg.getFunction()) {
                    case "U1001":
                        // 处理船控状态
                        handleControlStatus(boatDeviceId, msg);
                        break;
                    case "U1002":
                        // 录制的航路信息
                        handleRouteData(boatDeviceId, msg);
                        break;
                    case "U1003":
                        // 组合导航信息
                        handleNavigationData(boatDeviceId, msg);
                        break;
                    case "U1004":
                        // 电机信息
                        handleMotorData(boatDeviceId, msg);
                        break;
                    case "U1005":
                        // 毫米波雷达信息
                        handleRadarData(boatDeviceId, msg);
                        break;
                    case "U1006":
                        // 传感器信息
                        handleSensorData(boatDeviceId, msg);
                        break;
                    case "G1001":
                        // 同步船控模式
                        handleGetControlMode(boatDeviceId, session);
                        break;
                    default:
                        log.error("未知的function: {}", msg.getFunction());
                }
            } catch (JSONException e) {
                log.error("JSON 解析错误: {}", e.getMessage(), e);
            } catch (IOException e) {
                log.error("IO 异常: {}", e.getMessage(), e);
            } catch (Exception e) {
                log.error("处理消息失败: {}", e.getMessage(), e);
            }
        });
    }

    public void sendMessageToSession(String uri, String message) throws IOException {
        WebSocketSession session = sessions.get(uri);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        } else {
            log.error("当前回话已经关闭，无法发送消息: {}", uri);
        }
    }

    public WebSocketSession getSessionByUri(String uri) {
        return sessions.get(uri);
    }

    public String getWebSocketUrlByBoatDeviceId(String boatId) {
        return boatWebSocketUrls.get(boatId);
    }

    // 解析获取船设备id
    private String getBoatDeviceIdFromUri(String uri) {
        try {
            String[] parts = uri.split("/");
            if (parts.length > 1) {
                return parts[parts.length - 1];
            }
        } catch (Exception e) {
            log.error("解析URI失败: {}", e.getMessage(), e);
        }
        return null;
    }

    // 解析船控的状态
    private void handleControlStatus(String boatDeviceId, WebSocketMessageContext msg) {
        saveToRedis(boatDeviceId, RedisKeyConstants.STATE_INFO, msg.getAction());
    }

    // 处理录制的航路信息，保存本地
    private void handleRouteData(String boatDeviceId, WebSocketMessageContext msg) {
        try {
            List<BoatCourseMaking> navigationRecords = JSON.parseArray(msg.getJsonData(), BoatCourseMaking.class);
            if (CollectionUtils.isNotEmpty(navigationRecords)) {
                List<BoatCourseMaking> courses = new ArrayList<>();
                for (int i = 0; i < navigationRecords.size(); i++) {
                    BoatCourseMaking course = new BoatCourseMaking();
                    course.setBoatDeviceId(boatDeviceId);
                    course.setLatitude(navigationRecords.get(i).getLatitude());
                    course.setLongitude(navigationRecords.get(i).getLongitude());
                    course.setOrderIndex(i + 1);
                    course.setCreatedTime(new Date());
                    course.setUpdatedTime(new Date());
                    course.setDeleteStatus(false);
                    courses.add(course);

                    if (courses.size() == 500) {
                        courseMakingService.saveBatch(courses);
                        courses.clear();
                    }
                }

                if (!courses.isEmpty()) {
                    courseMakingService.saveBatch(courses);
                }
            }
        } catch (JSONException e) {
            log.error("JSON 解析错误: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("解析航线点信息失败: {}", e.getMessage(), e);
        }
    }

    // 处理组合导航信息
    private void handleNavigationData(String boatDeviceId, WebSocketMessageContext msg) {
        saveToRedis(boatDeviceId, RedisKeyConstants.INTEGRATED_NAVIGATION_INFO, msg.getJsonData());
    }

    // 处理电机信息
    private void handleMotorData(String boatDeviceId, WebSocketMessageContext msg) {
        saveToRedis(boatDeviceId, RedisKeyConstants.MOTOR_INFO, msg.getJsonData());
    }

    // 处理毫米波雷达信息
    private void handleRadarData(String boatDeviceId, WebSocketMessageContext msg) {
        saveToRedis(boatDeviceId, RedisKeyConstants.RADAR_INFO, msg.getJsonData());
    }

    // 处理传感器信息
    private void handleSensorData(String boatDeviceId, WebSocketMessageContext msg) {
        saveToRedis(boatDeviceId, RedisKeyConstants.SENSOR_INFO, msg.getJsonData());
    }

    // 处理同步船控模式
    private void handleGetControlMode(String boatDeviceId, WebSocketSession session) throws IOException {
        UnmannedShip boat = unmannedShipService.getById(boatDeviceId);
        if (boat != null) {
            String mode = String.valueOf(boat.getCurrentMode());
            String response = String.format("{\"mode\": \"%s\"}", mode);
            sendMessageToSession(Objects.requireNonNull(session.getUri()).toString(), response);
        } else {
            log.error("未找到船ID: {}", boatDeviceId);
        }
    }

    // 存储缓存数据
    private void saveToRedis(String boatDeviceId, String keySuffix, String jsonData) {
        String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
        if (deviceType == null) {
            log.error("根据设备ID获取设备类型失败: {}", boatDeviceId);
            return;
        }
        String redisKeyPrefix = deviceType + ":" + keySuffix + ":" + boatDeviceId;
        redisUtil.setByType(BoatType.fromType(deviceType), redisKeyPrefix, jsonData);
    }

    // 关闭线程池
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
