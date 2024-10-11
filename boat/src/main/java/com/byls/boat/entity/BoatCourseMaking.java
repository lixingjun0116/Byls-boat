package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("boat_course_making")
@Data
public class BoatCourseMaking {
    /**
     * 主键ID，唯一标识一个航线点。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 航线点名称，用于识别不同的航线点。
     */
    @TableField("route_point_name")
    private String routePointName;

    /**
     * 经度，航线点的地理坐标的一部分。
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度，航线点的地理坐标的一部分。
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 删除状态，布尔值表示航线点是否已被逻辑删除。
     * true：已删除；false：未删除。
     */
    @TableField("delete_status")
    private Boolean deleteStatus;

    /**
     * 顺序索引，表示航线点在航线中的顺序位置。
     */
    @TableField("order_index")
    private Integer orderIndex;

    /**
     * 描述，用于提供航线点的额外信息或备注。
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间，记录航线点被创建的时间点。
     */
    @TableField("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 更新时间，记录航线点最后一次被修改的时间点。
     */
    @TableField("updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;


}
