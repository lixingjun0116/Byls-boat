package com.byls.boat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 航线表响应数据
 */
@Data
public class RouteRespVO {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 航线编号
     */
    private String routeCode;

    /**
     * 航线名称
     */
    private String routeName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;


    /**
     * 航线的状态
     */
    private Integer status;

    //船设备编号
    private String boatDeviceId;
    //船设备名称
    private String boatDeviceName;
    //船航线描述
    private String description;
}
