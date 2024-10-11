package com.byls.boat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 日志记录表实体类
 */
@TableName("log")
@Data
public class Log {

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
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;


    /**
     * 日志事件类型，位置更新、任务开始/结束、故障报警
     */
    private Integer eventType;

    /**
     * 事件的详细描述
     */
    private String details;
}
