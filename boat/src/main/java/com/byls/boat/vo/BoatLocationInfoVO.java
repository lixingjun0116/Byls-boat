package com.byls.boat.vo;

import lombok.Data;

/**
 * @Description 船实时位置
 * @Date 2024/10/11 9:15
 * @Created by lxj
 */
@Data
public class BoatLocationInfoVO {
    //船设备编号
    private String boatDeviceId;
    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
}
