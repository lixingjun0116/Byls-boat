package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.Mission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行任务表Mapper接口
 */
@Mapper
public interface MissionMapper extends BaseMapper<Mission> {
}
