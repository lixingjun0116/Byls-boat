package com.byls.boat.entity.hardware;

import lombok.Data;

/**
 * @Description 毫米波雷达数据
 * @Date 2025/1/4 15:19
 * @Created by lxj
 */
@Data
public class MillimeterWaveRadar {
    //设备编号
    private String deviceId;
    //设备名称
    private String deviceName;
    //雷达数值
    private Double radarValue;
    //距离
    private Double distance;
    //角度
    private Double angle;
    //速度
    private Double speed;
    //功率
    private Double power;
    //转速
    private Double rpm;
}
