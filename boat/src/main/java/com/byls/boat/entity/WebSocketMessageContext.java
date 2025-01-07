package com.byls.boat.entity;

import lombok.Data;

@Data
public class WebSocketMessageContext{
    private String function;
    private String mode;
    private String status;
    private String jsonData;
    private String action;
    private String left_joystick;
    private String right_joystick;
    private String routeData;
}