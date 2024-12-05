package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.BoatDeviceTypeRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 船类型与船只关系Mapper接口
 */
@Mapper
public interface BoatDeviceTypeRelationMapper extends BaseMapper<BoatDeviceTypeRelation> {
}
