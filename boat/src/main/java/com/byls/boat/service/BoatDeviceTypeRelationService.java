package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.BoatDeviceTypeRelation;

import java.util.List;

/**
 * 船类型与船只关联关系表Service接口
 */
public interface BoatDeviceTypeRelationService extends IService<BoatDeviceTypeRelation> {
    // 获取船类型与船只关联关系列表
    List<BoatDeviceTypeRelation> getBoatDeviceTypeRelations();
}
