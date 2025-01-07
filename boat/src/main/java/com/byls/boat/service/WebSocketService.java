package com.byls.boat.service;

import com.byls.boat.common.WebSocketHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class WebSocketService {

    @Autowired
    private WebSocketHelper webSocketHelper;

    public void sendMessageToBoat(String boatDeviceId, String message) throws IOException {
        webSocketHelper.sendMessageToBoat(boatDeviceId, message);
    }

    public WebSocketSession getSessionByBoat(String boatDeviceId) {
        return webSocketHelper.getSessionByBoat(boatDeviceId);
    }
}
