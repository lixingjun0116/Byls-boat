package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 无人船设备表实体类
 */
@TableName("unmanned_ship")
@Data
public class UnmannedShip {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 无人船编号
     */
    @TableField(value = "ship_code")
    private String shipCode;

    /**
     * 无人船名称
     */
    @TableField(value = "ship_name")
    private String shipName;

    /**
     * 无人船型号
     */
    @TableField(value = "ship_type")
    private String shipType;

    /**
     * 无人船的当前状态，航行中、停泊、维护
     */
    @TableField(value = "current_status")
    private Integer currentStatus;

    /**
     * 船当前位置坐标经度
     */
    @TableField(value = "current_longitude")
    private Double currentLongitude;

    /**
     * 船当前位置坐标纬度
     */
    @TableField(value = "current_latitude")
    private Double currentLatitude;


    /**
     * 删除状态，布尔值表示航线点是否已被逻辑删除。
     * true：已删除；false：未删除。
     */
    @TableField("delete_status")
    private Boolean deleteStatus;
}
