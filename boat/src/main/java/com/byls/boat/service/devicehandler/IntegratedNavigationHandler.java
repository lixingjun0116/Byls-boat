package com.byls.boat.service.devicehandler;

import com.alibaba.fastjson.JSON;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.hardware.IntegratedNavigationInfo;

public class IntegratedNavigationHandler implements DeviceHandler {
    @Override
    public Object parse(String jsonData) {
        return JSON.parseObject(jsonData, IntegratedNavigationInfo.class);
    }

    @Override
    public String getKeySuffix() {
        return RedisKeyConstants.INTEGRATED_NAVIGATION_INFO;
    }
}