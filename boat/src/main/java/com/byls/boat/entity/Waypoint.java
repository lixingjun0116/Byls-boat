package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 航路点表实体类
 */
@TableName("waypoint")
@Data
public class Waypoint {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 航线点名称，用于识别不同的航线点。
     */
    @TableField("route_point_name")
    private String routePointName;

    /**
     * 关联航线编号
     */
    @TableField("route_code")
    private String routeCode;

    /**
     * 航路点的地理坐标经度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 航路点的地理坐标纬度
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 在航线中的顺序位置
     */
    @TableField("sequence")
    private Integer sequence;

    /**
     * 到达条件，速度限制、停留时间
     */
    @TableField("arrival_conditions")
    private String arrivalConditions;

    /**
     * 删除状态，布尔值表示航线点是否已被逻辑删除。
     * true：已删除；false：未删除。
     */
    @TableField("delete_status")
    private Boolean deleteStatus;
}
