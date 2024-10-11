package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 异常信息表实体类
 */
@TableName("exception")
@Data
public class Exception {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 无人船编号
     */
    private String shipCode;

    /**
     * 航线编号
     */
    private String routeCode;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;

    /**
     * 异常的具体情况
     */
    private String description;

    /**
     * 异常处理状态
     */
    private Integer handlingStatus;
}
