package com.byls.boat.service.catchhandler;

import com.alibaba.fastjson2.JSON;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.BoatDeviceTypeRelation;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.service.BoatDeviceTypeRelationService;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.util.RedisUtil;
import com.byls.boat.vo.BoatPushRodVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description 缓存管理器
 * @Date 2025/1/3 15:08
 * @Created by lxj
 */
@Slf4j
@Component
public class CacheCenter {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BoatDeviceTypeRelationService boatDeviceTypeRelationService;
    @Autowired
    private IUnmannedShipService unmannedShipService;

    // 船只和船类型关联关系缓存
    private static final String DEVICE_TYPE_TO_BOAT_DEVICE_IDS_MAP_KEY = "device_type_to_boat_device_ids_map";
    private static final String BOAT_DEVICE_ID_TO_DEVICE_TYPE_MAP_KEY = "boat_device_id_to_device_type_map";

    private static final ConcurrentHashMap<String, List<String>> deviceTypeToBoatDeviceIdsMapCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> boatDeviceIdToDeviceTypeMapCache = new ConcurrentHashMap<>();
    // 无人船列表内存缓存
    private static final String UNMANNED_SHIP_LIST = "unmanned_ship_list";
    // 推杆值缓存
    private static final String PUSH_ROD_VALUE = "push_rod_value";
    private static ConcurrentHashMap<String, BoatPushRodVO> pushRodValueCache = new ConcurrentHashMap<>();

    private static List<UnmannedShip> unmannedShipListCache = new ArrayList<>();

    //初始化缓存内存
    public void initCache() {
        // 初始化并缓存船只和船类型的关联关系
        cacheBoatTypeRelations();
        // 初始化并缓存无人船列表
        catchUnmannedShipList();

    }


    // 初始化并缓存船只和船类型的关联关系
    public void cacheBoatTypeRelations() {
        // 获取所有 BoatDeviceTypeRelation 记录
        List<BoatDeviceTypeRelation> boatDeviceTypeRelations = boatDeviceTypeRelationService.getBoatDeviceTypeRelations();

        if (boatDeviceTypeRelations == null || boatDeviceTypeRelations.isEmpty()) {
            log.error("未找到船类型与船只关联关系");
            return;
        }

        // 构建 deviceTypeToBoatDeviceIdsMap，键是 deviceType，值是 boatDeviceId 的列表
        Map<String, List<String>> deviceTypeToBoatDeviceIdsMap = boatDeviceTypeRelations.stream()
                .collect(Collectors.groupingBy(
                        BoatDeviceTypeRelation::getDeviceType,
                        Collectors.mapping(BoatDeviceTypeRelation::getBoatDeviceId, Collectors.toList())
                ));

        // 构建 boatDeviceIdToDeviceTypeMap，键是 boatDeviceId，值是 deviceType
        Map<String, String> boatDeviceIdToDeviceTypeMap = boatDeviceTypeRelations.stream()
                .collect(Collectors.toMap(
                        BoatDeviceTypeRelation::getBoatDeviceId,
                        BoatDeviceTypeRelation::getDeviceType
                ));

        // 更新缓存
        deviceTypeToBoatDeviceIdsMapCache.clear();
        deviceTypeToBoatDeviceIdsMapCache.putAll(deviceTypeToBoatDeviceIdsMap);

        boatDeviceIdToDeviceTypeMapCache.clear();
        boatDeviceIdToDeviceTypeMapCache.putAll(boatDeviceIdToDeviceTypeMap);

        // 缓存到 Redis
        redisUtil.setHash(DEVICE_TYPE_TO_BOAT_DEVICE_IDS_MAP_KEY, deviceTypeToBoatDeviceIdsMap);
        redisUtil.setHash(BOAT_DEVICE_ID_TO_DEVICE_TYPE_MAP_KEY, boatDeviceIdToDeviceTypeMap);
    }

    /**
     * 根据船设备类型获取船只列表集合
     *
     * @param deviceType 设备类型
     * @return 船只ID列表
     */
    public List<String> getBoatDeviceIdsByDeviceType(String deviceType) {
        return deviceTypeToBoatDeviceIdsMapCache.getOrDefault(deviceType, Collections.emptyList());
    }

    /**
     * 根据船只ID获取对应的设备类型
     *
     * @param boatDeviceId 船只ID
     * @return 设备类型
     */
    public String getDeviceTypeByBoatDeviceId(String boatDeviceId) {
        return boatDeviceIdToDeviceTypeMapCache.get(boatDeviceId);
    }

    /**
     * 获取 deviceTypeToBoatDeviceIdsMap，如果缓存为空则从数据库中查询并更新 Redis（暂时没用上，后续看看有必要执行此操作吗）
     *
     * @return deviceTypeToBoatDeviceIdsMap
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<String>> getDeviceTypeToBoatDeviceIdsMap() {
        if (deviceTypeToBoatDeviceIdsMapCache.isEmpty()) {
            log.warn("deviceTypeToBoatDeviceIdsMap 缓存为空，从数据库中查询并更新 Redis");
            cacheBoatTypeRelations();
        }
        return deviceTypeToBoatDeviceIdsMapCache;
    }

    /**
     * 获取 boatDeviceIdToDeviceTypeMap，如果缓存为空则从数据库中查询并更新 Redis（暂时也没用上，后续看看有必要执行此操作吗）
     *
     * @return boatDeviceIdToDeviceTypeMap
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getBoatDeviceIdToDeviceTypeMap() {
        if (boatDeviceIdToDeviceTypeMapCache.isEmpty()) {
            log.warn("boatDeviceIdToDeviceTypeMap 缓存为空，从数据库中查询并更新 Redis");
            cacheBoatTypeRelations();
        }
        return boatDeviceIdToDeviceTypeMapCache;
    }

    // 初始化并缓存无人船列表
    public void catchUnmannedShipList() {
        try {
            List<UnmannedShip> unmannedShipList = unmannedShipService.getUnmannedShipList();

            // 清空内存中的数据
            unmannedShipListCache.clear();
            unmannedShipListCache.addAll(unmannedShipList);

            // 将数据保存到 Redis 中
            redisUtil.setList(UNMANNED_SHIP_LIST, unmannedShipList);
        } catch (Exception e) {
            log.error("获取无人船设备列表失败: {}", String.valueOf(e));
        }
    }

    // 获取无人船设备列表缓存
    public List<UnmannedShip> getUnmannedShipList() {
        if (unmannedShipListCache.isEmpty()) {
            catchUnmannedShipList();
        }
        return unmannedShipListCache;
    }

    // 获取无人船设备列表缓存通过船设备id
    public UnmannedShip getUnmannedShipByDeviceId(String boatDeviceId) {
        if (unmannedShipListCache.isEmpty()) {
            catchUnmannedShipList();
        }
        for (UnmannedShip unmannedShip : unmannedShipListCache) {
            if (unmannedShip.getShipCode().equals(boatDeviceId)) {
                return unmannedShip;
            }
        }
        return null;
    }

    //获取推杆值缓存
    public BoatPushRodVO getPushRodValue(String boatDeviceId) {
        BoatPushRodVO cachedValue = pushRodValueCache.get(boatDeviceId);
        if (cachedValue != null) {
            return cachedValue;
        }

        String pushRodValue = redisUtil.getValue(RedisKeyConstants.PUSH_ROD_VALUE + ":" + boatDeviceId);
        if (pushRodValue != null) {
            BoatPushRodVO boatPushRod = JSON.parseObject(pushRodValue, BoatPushRodVO.class);
            pushRodValueCache.put(boatDeviceId, boatPushRod);
            return boatPushRod;
        } else {
            BoatPushRodVO defaultPushRod = new BoatPushRodVO(boatDeviceId, 0, 0);
            pushRodValueCache.put(boatDeviceId, defaultPushRod);
            redisUtil.setValue(RedisKeyConstants.PUSH_ROD_VALUE + ":" + boatDeviceId, JSON.toJSONString(defaultPushRod));
            return defaultPushRod;
        }
    }

    //更新推杆值缓存
    public void updatePushRodValue(BoatPushRodVO pushRodValue) {
        try {
            String boatDeviceId = pushRodValue.getBoatDeviceId();
            pushRodValueCache.put(boatDeviceId, pushRodValue);
            redisUtil.setValue(RedisKeyConstants.PUSH_ROD_VALUE + ":" + boatDeviceId, JSON.toJSONString(pushRodValue));
        } catch (Exception e) {
            log.error("更新推杆值缓存失败，船设备ID {}: {}", pushRodValue.getBoatDeviceId(), e.getMessage(), e);
        }
    }

}
