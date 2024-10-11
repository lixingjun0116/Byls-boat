package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.Exception;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常信息表Mapper接口
 */
@Mapper
public interface ExceptionMapper extends BaseMapper<Exception> {
}
