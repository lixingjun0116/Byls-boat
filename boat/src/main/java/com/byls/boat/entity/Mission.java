package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 执行任务表实体类
 */
@TableName("mission")
@Data
public class Mission {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务编号
     */
    @TableField(value = "mission_code")
    private String missionCode;

    /**
     * 关联航线编号
     */
    @TableField(value = "route_code")
    private String routeCode;

    /**
     * 无人船编号
     */
    @TableField(value = "ship_code")
    private String shipCode;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 任务的状态，待执行、执行中、已完成、取消
     */
    @TableField(value = "status")
    private Integer status;
}
