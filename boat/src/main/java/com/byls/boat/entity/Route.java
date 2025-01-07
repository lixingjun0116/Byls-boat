package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 航线表实体类
 */
@Data
@TableName("route")
public class Route {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 航线编号
     */
    @TableField(value = "route_code")
    private String routeCode;

    /**
     * 航线名称
     */
    @TableField(value = "route_name")
    private String routeName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;


    /**
     * 航线的状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 航线是否启用
     * true：启用；false：未启用。
     */
    @TableField("enable_status")
    private Boolean enableStatus;

    //船设备编号
    @TableField(value = "boat_device_id")
    private String boatDeviceId;
    //船航线描述
    @TableField(value = "description")
    private String description;
}
