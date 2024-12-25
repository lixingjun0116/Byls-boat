package com.byls.boat.service.devicehandler;

import com.byls.boat.entity.hardware.IntegratedNavigationInfo;
import com.byls.boat.entity.hardware.MotorInfo;
import com.byls.boat.entity.hardware.SensorInfo;

import java.util.List;

@FunctionalInterface
public interface DeviceHandler {
    Object parse(String jsonData) throws Exception;

    default String getDeviceId(Object deviceInfo) {
        if (deviceInfo instanceof IntegratedNavigationInfo) {
            return ((IntegratedNavigationInfo) deviceInfo).getBoatDeviceId();
        } else if (deviceInfo instanceof List<?>) {
            List<?> list = (List<?>) deviceInfo;
            if (!list.isEmpty() && list.get(0) instanceof MotorInfo) {
                return ((MotorInfo) list.get(0)).getBoatDeviceId();
            }
        } else if (deviceInfo instanceof SensorInfo) {
            return ((SensorInfo) deviceInfo).getBoatDeviceId();
        }
        return null;
    }

    default String getKeySuffix() {
        throw new UnsupportedOperationException("获取前缀必须由子类实现");
    }
}