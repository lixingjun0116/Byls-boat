package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.Route;
import org.apache.ibatis.annotations.Mapper;

/**
 * 航线表Mapper接口
 */
@Mapper
public interface RouteMapper extends BaseMapper<Route> {
}
