package com.byls.boat.service.devicehandler;

import com.alibaba.fastjson.JSON;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.hardware.SensorInfo;

public class SensorHandler implements DeviceHandler {
    @Override
    public Object parse(String jsonData) {
        return JSON.parseObject(jsonData, SensorInfo.class);
    }

    @Override
    public String getKeySuffix() {
        return RedisKeyConstants.SENSOR_INFO;
    }
}