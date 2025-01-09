package com.byls.boat.config;

import com.byls.boat.controller.WebSocketController;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.service.IUnmannedShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import reactor.util.annotation.NonNull;

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
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {

        List<UnmannedShip> boats = unmannedShipService.getUnmannedShipList();
        for (UnmannedShip boat : boats) {
            String websocketUrl = boat.getWsAddress();
            if (websocketUrl != null && !websocketUrl.isEmpty()) {
                registry.addHandler(webSocketController, websocketUrl).setAllowedOrigins("*");
            }
        }
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 在此处设置bufferSize
        container.setMaxTextMessageBufferSize(512000);
        container.setMaxBinaryMessageBufferSize(512000);
        /*container.setMaxSessionIdleTimeout(15 * 60000L);*/
        return container;
    }
}
