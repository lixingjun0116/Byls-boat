// com.byls.boat.controller.WebSocketController.java
package com.byls.boat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.byls.boat.constant.BoatType;
import com.byls.boat.entity.hardware.BoatHardWareContext;
import com.byls.boat.service.BoatDeviceTypeRelationCatcheService;
import com.byls.boat.service.devicehandler.DeviceHandler;
import com.byls.boat.service.devicehandler.IntegratedNavigationHandler;
import com.byls.boat.service.devicehandler.MotorHandler;
import com.byls.boat.service.devicehandler.SensorHandler;
import com.byls.boat.util.RateLimiter;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class WebSocketController extends TextWebSocketHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BoatDeviceTypeRelationCatcheService relationCatcheService;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final AtomicReference<WebSocketSession> hardwareSession = new AtomicReference<>();

    private final DeviceHandler integratedNavigationHandler = new IntegratedNavigationHandler();
    private final DeviceHandler motorHandler = new MotorHandler();
    private final DeviceHandler sensorHandler = new SensorHandler();
    // 先暂定每秒最多允许有1个请求，后期如果需要不同类型数据做不同的限流就再改吧
    private final RateLimiter rateLimiter = new RateLimiter(1000);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        log.info("建立WebSocket连接: {}", session.getId());

        // 如果是硬件设备连接，保存会话
        if (Objects.requireNonNull(session.getUri()).getPath().endsWith("/hardware")) {
            hardwareSession.set(session);
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
        WebSocketSession currentHardwareSession = hardwareSession.get();
        if (session.equals(currentHardwareSession)) {
            storeHardWareData(session.getId(),payload);
        }
    }

    private void storeHardWareData(String clientId,String payload) {
        try {
            if (payload == null || payload.isEmpty()) {
                log.warn("payload 不能为空");
                return;
            }

            BoatHardWareContext hardWareContext = JSON.parseObject(payload, BoatHardWareContext.class);
            String keyId = hardWareContext.getKeyId();
            String jsonData = hardWareContext.getJsonData();

            if (keyId == null || keyId.isEmpty()) {
                log.warn("keyId数据为空");
                return;
            }
            if (jsonData == null || jsonData.isEmpty()) {
                log.warn("jsonData数据为空");
                return;
            }

            // 限流逻辑
            if (!rateLimiter.shipPushDataLimit(clientId, keyId)) {
                log.warn("高频请求被限流: clientId={}, keyId={}", clientId, keyId);
                return;
            }

            // 根据不同标识符keyId，解析存储不同设备信息
            switch (keyId) {
                case "A01":
                    handleDeviceInfo(jsonData, integratedNavigationHandler);
                    break;
                case "A02":
                    handleDeviceInfo(jsonData, motorHandler);
                    break;
                case "A03":
                    handleDeviceInfo(jsonData, sensorHandler);
                    break;
                default:
                    log.error("未知的标识: {}", keyId);
            }
        } catch (JSONException e) {
            log.error("JSON 解析错误: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("存储设备数据失败", e);
        }
    }

    private void handleDeviceInfo(String jsonData, DeviceHandler handler) {
        try {
            Object deviceInfo = handler.parse(jsonData);
            if (deviceInfo == null) {
                log.warn("解析设备数据失败: {}", jsonData);
                return;
            }
            String deviceId = handler.getDeviceId(deviceInfo);
            saveToRedis(deviceId, handler.getKeySuffix(), jsonData);
        } catch (Exception e) {
            log.error("处理设备数据失败", e);
        }
    }

    private void saveToRedis(String deviceId, String keySuffix, String jsonData) {
        String deviceTypeByBoatDeviceId = relationCatcheService.getDeviceTypeByBoatDeviceId(deviceId);
        if (deviceTypeByBoatDeviceId == null) {
            log.error("根据设备ID获取设备类型失败: {}", deviceId);
            return;
        }
        String redisKeyPrefix = deviceTypeByBoatDeviceId + ":" + keySuffix + ":" + deviceId;
        redisUtil.setByType(BoatType.fromType(deviceTypeByBoatDeviceId), redisKeyPrefix, jsonData);
    }
}