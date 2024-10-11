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
     * 航线点名称
     */
    @TableField(value = "route_point_name")
    private String routePointName;

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

    /**
     * 船体形态，当前状态
     */
    @TableField(value = "boat_form")
    private String boatForm;

    /**
     * 船设备编号
     */
    @TableField(value = "boat_device_id")
    private String boatDeviceId;

    /**
     * 生成时间，该位置信息被采集的具体时间。
     */
    @TableField(value = "record_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 冗余字段，存储相关的额外信息
     */
    @TableField(value = "additional_info")
    private String additionalInfo;
}
