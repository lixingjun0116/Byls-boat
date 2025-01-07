package com.byls.boat.common;

import com.byls.boat.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebSocketHelper {
    private final WebSocketController webSocketController;

    @Autowired
    public WebSocketHelper(@Lazy WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    public void sendMessageToBoat(String boatId, String message) throws IOException {
        String websocketUrl = webSocketController.getWebSocketUrlByBoatDeviceId(boatId);
        if (websocketUrl != null) {
            webSocketController.sendMessageToSession(websocketUrl, message);
        } else {
            throw new IOException("未找到船的WebSocket URL");
        }
    }

    public WebSocketSession getSessionByBoat(String boatId) {
        String websocketUrl = webSocketController.getWebSocketUrlByBoatDeviceId(boatId);
        if (websocketUrl != null) {
            return webSocketController.getSessionByUri(websocketUrl);
        }
        return null;
    }
}
