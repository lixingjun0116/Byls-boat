package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.BoatDeviceTypeRelation;
import com.byls.boat.mapper.BoatDeviceTypeRelationMapper;
import com.byls.boat.service.BoatDeviceTypeRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 船设备类型与船只的关联关系Service实现类
 */
@Slf4j
@Service
public class BoatDeviceTypeRelationServiceImpl extends ServiceImpl<BoatDeviceTypeRelationMapper, BoatDeviceTypeRelation> implements BoatDeviceTypeRelationService {

    @Override
    public List<BoatDeviceTypeRelation> getBoatDeviceTypeRelations() {
        // 获取全部列表
        return list();
    }
}
