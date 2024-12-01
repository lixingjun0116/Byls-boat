package com.byls.boat.entity.hardware;

import lombok.Data;

/**
 * @Description 电机信息
 * @Date 2024/11/5 10:08
 * @Created by lxj
 */
@Data
public class MotorInfo {
    /**
     * 船设备编号
     */
    private String boatDeviceId;
    //设备编号
    private String deviceId;
    //设备名称
    private String deviceName;
    //速度
    private Double speed;
    //功率
    private Double power;
    //转速
    private Double rpm;

}
