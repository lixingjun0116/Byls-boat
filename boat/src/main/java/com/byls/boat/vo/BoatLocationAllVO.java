package com.byls.boat.vo;

import lombok.Data;

import java.util.List;
/*首页所有船地址*/
@Data
public class BoatLocationAllVO {
    private String boatDeviceId;
    private String boatDeviceName;
    private String boatDeviceTypeName;
    //经纬度
    private List<Double> locationValue;
}
