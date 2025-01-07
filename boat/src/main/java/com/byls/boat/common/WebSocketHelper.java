package com.byls.boat.common;

import com.byls.boat.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class WebSocketHelper {

    @Autowired
    private WebSocketController webSocketController;

    public void sendMessageToBoat(String boatDeviceId, String message) throws IOException {
        String websocketUrl = webSocketController.getWebSocketUrlByBoatDeviceId(boatDeviceId);
        if (websocketUrl != null) {
            webSocketController.sendMessageToSession(websocketUrl, message);
        } else {
            throw new IOException("未找到船的WebSocket URL");
        }
    }

    public WebSocketSession getSessionByBoat(String boatDeviceId) {
        String websocketUrl = webSocketController.getWebSocketUrlByBoatDeviceId(boatDeviceId);
        if (websocketUrl != null) {
            return webSocketController.getSessionByUri(websocketUrl);
        }
        return null;
    }
}