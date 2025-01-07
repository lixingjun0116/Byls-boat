package com.byls.boat.config;

import com.byls.boat.controller.WebSocketController;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.service.IUnmannedShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

@Configuration
@MessageMapping
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private IUnmannedShipService unmannedShipService;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketController, "/ws/hardware").setAllowedOrigins("*");

        List<UnmannedShip> boats = unmannedShipService.getUnmannedShipList();
        for (UnmannedShip boat : boats) {
            String websocketUrl = boat.getWsAddress();
            if (websocketUrl != null && !websocketUrl.isEmpty()) {
                registry.addHandler(webSocketController, websocketUrl).setAllowedOrigins("*");
            }
        }
    }
}
