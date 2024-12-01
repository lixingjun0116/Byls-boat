package com.byls.boat.entity.hardware;

import lombok.Data;

/**
 * @Description 组合导航信息
 * @Date 2024/11/5 10:02
 * @Created by lxj
 */
@Data
public class IntegratedNavigationInfo {
    // 设备id
    private String boatDeviceId;
    // 设备名称
    private String deviceName;
    // 航向
    private Double heading;
    // 航向角
    private Double headingAngle;
    // 航向角速度
    private Double headingAngleSpeed;
    // 经度
    private Double longitude;
    // 纬度
    private Double latitude;
}
