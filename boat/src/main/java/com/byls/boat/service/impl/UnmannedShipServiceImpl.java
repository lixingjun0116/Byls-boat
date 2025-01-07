package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.entity.hardware.IntegratedNavigationInfo;
import com.byls.boat.mapper.UnmannedShipMapper;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.service.catchhandler.CacheCenter;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 无人船设备表Service实现类
 */
@Slf4j
@Service
public class UnmannedShipServiceImpl extends ServiceImpl<UnmannedShipMapper, UnmannedShip> implements IUnmannedShipService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheCenter cacheCenter;

    @Override
    public List<UnmannedShip> getUnmannedShipList() {
        try {
            return this.list();
        } catch (Exception e) {
            log.error("获取设备列表失败: " + e);
        }
        return Collections.emptyList();
    }

    @Override
    public UnmannedShip getUnmannedShip(String boatDeviceId) {
        // 条件查询 根据设备编号查询
        return this.getOne(new QueryWrapper<UnmannedShip>().eq("ship_code", boatDeviceId));
    }

    @Override
    public boolean addUnmannedShip(UnmannedShip unmannedShip) {
        return this.save(unmannedShip);
    }

    @Override
    public boolean updateUnmannedShip(UnmannedShip unmannedShip) {
        return this.updateById(unmannedShip);
    }

    @Override
    public boolean deleteUnmannedShip(String deviceId) {
        return this.removeById(deviceId);
    }

    @Override
    public List<UnmannedShip> getUnmannedShipsByType(BoatType boatType) {
        if (boatType == null) {
            return Collections.emptyList();
        }
        try {
            QueryWrapper<UnmannedShip> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ship_type", boatType.getType());
            return this.list(queryWrapper);
        } catch (Exception e) {
            log.error("根据设备类型获取船设备列表失败: " + e);
        }
        return Collections.emptyList();
    }

    // 获取所有船的位置信息
    @Override
    public Map<String, List<Double>> getAllBoatLocation() {
        Map<String, List<Double>> result = new HashMap<>();
        try {
            // 获取所有船类型
            for (BoatType boatType : BoatType.values()) {
                log.info("处理船类型: {}", boatType.getType());

                // 获取该船类型的无人船列表
                List<String> boatDeviceIds = cacheCenter.getBoatDeviceIdsByDeviceType(boatType.getType());
                if (boatDeviceIds.isEmpty()) {
                    log.warn("没有找到 {} 类型的无人船", boatType.getType());
                    continue;
                }

                for (String boatDeviceId : boatDeviceIds) {
                    String redisKey = boatType.getType() + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + boatDeviceId;
                    // 获取导航数据
                    String navigationData = redisUtil.getByType(boatType, redisKey);
                    if (StringUtils.isEmpty(navigationData)) {
                        log.error("redis中无组合导航数据船类型: {}, 设备id: {}", boatType.getType(), boatDeviceId);
                        continue;
                    }

                    // 解析导航数据
                    IntegratedNavigationInfo integratedNavigationInfo = JSON.parseObject(navigationData, IntegratedNavigationInfo.class);
                    if (integratedNavigationInfo == null) {
                        log.error("redis转换实体后为空船类型: {}, 设备id: {}", boatType.getType(), boatDeviceId);
                        continue;
                    }

                    // 组织返回数据
                    List<Double> location = Arrays.asList(integratedNavigationInfo.getLongitude(), integratedNavigationInfo.getLatitude());
                    result.put(boatDeviceId, location);
                }
            }
        } catch (Exception e) {
            log.error("获取所有船的位置信息失败: " + e);
        }
        return result;
    }

}
