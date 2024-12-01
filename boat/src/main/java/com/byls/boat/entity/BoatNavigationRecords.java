package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("boat_navigation_records")
@Data
public class BoatNavigationRecords {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 船设备编号
     */
    @TableField(value = "boat_device_id")
    private String boatDeviceId;
    /**
     * 船设备名称
     */
    @TableField(value = "device_name")
    private String deviceName;
    /**
     * 经度
     */
    @TableField(value = "longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    private Double latitude;

    // 航向
    @TableField(value = "heading")
    private Double heading;
    // 航向角
    @TableField(value = "heading_angle")
    private Double headingAngle;
    // 航向角速度
    @TableField(value = "heading_angle_speed")
    private Double headingAngleSpeed;
    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
