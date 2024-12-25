package com.byls.boat.service.devicehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.hardware.MotorInfo;

import java.util.List;

public class MotorHandler implements DeviceHandler {
    @Override
    public Object parse(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<List<MotorInfo>>() {
        });
    }

    @Override
    public String getKeySuffix() {
        return RedisKeyConstants.MOTOR_INFO;
    }
}