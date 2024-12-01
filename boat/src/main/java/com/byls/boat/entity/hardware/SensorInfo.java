package com.byls.boat.entity.hardware;

import lombok.Data;

/**
 * @Description 传感器信息
 * @Date 2024/11/5 10:10
 * @Created by lxj
 */
@Data
public class SensorInfo {
    /**
     * 船设备编号
     */
    private String boatDeviceId;
    // 设备id
    private String deviceId;
    // 设备名称
    private String deviceName;

}
