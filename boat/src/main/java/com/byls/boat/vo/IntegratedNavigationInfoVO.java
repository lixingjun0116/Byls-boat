package com.byls.boat.vo;

import com.byls.boat.constant.BoatType;
import lombok.Data;

/**
 * @Description 组合导航信息页面响应对象
 * @Date 2024/11/5 10:02
 * @Created by lxj
 */
@Data
public class IntegratedNavigationInfoVO {
    //船设备编号
    private String boatDeviceId;
    //船设备名称
    private String boatDeviceName;
    //船设备类型
    private BoatType boatDeviceType;
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
