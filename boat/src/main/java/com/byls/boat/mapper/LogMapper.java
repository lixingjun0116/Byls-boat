package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志记录表Mapper接口
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
