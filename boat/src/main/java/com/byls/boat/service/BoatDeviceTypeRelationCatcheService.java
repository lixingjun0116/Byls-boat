package com.byls.boat.service;

import com.byls.boat.entity.BoatDeviceTypeRelation;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoatDeviceTypeRelationCatcheService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BoatDeviceTypeRelationService boatDeviceTypeRelationService;

    private static final String DEVICE_TYPE_TO_BOAT_DEVICE_IDS_MAP_KEY = "device_type_to_boat_device_ids_map";
    private static final String BOAT_DEVICE_ID_TO_DEVICE_TYPE_MAP_KEY = "boat_device_id_to_device_type_map";

    private static final ConcurrentHashMap<String, List<String>> deviceTypeToBoatDeviceIdsMapCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> boatDeviceIdToDeviceTypeMapCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        cacheBoatTypeRelations();
    }

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
}
