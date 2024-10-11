package com.byls.boat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byls.boat.entity.UnmannedShip;
import org.apache.ibatis.annotations.Mapper;

/**
 * 无人船设备表Mapper接口
 */
@Mapper
public interface UnmannedShipMapper extends BaseMapper<UnmannedShip> {
}
