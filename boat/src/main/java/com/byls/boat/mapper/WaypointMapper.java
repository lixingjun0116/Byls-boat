package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.Waypoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 航路点表Mapper接口
 */
@Mapper
public interface WaypointMapper extends BaseMapper<Waypoint> {
}
