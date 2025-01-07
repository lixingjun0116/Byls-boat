package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.BoatNavigationRecords;
import com.byls.boat.entity.hardware.IntegratedNavigationInfo;
import com.byls.boat.service.IBoatNavigationRecordsService;
import com.byls.boat.service.catchhandler.CacheCenter;
import com.byls.boat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 定时存储导航记录
 * @Date 2024/11/5 10:57
 * @Created by lxj
 */
@Slf4j
@Component
public class BoatNavigationRecordsTiming implements Runnable {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBoatNavigationRecordsService boatNavigationRecordsService;

    @Autowired
    private CacheCenter cacheCenter;


    @Override
    public void run() {
        while (true) {
            try {
                // 获取所有船类型
                for (BoatType boatType : BoatType.values()) {
                    log.info("处理船类型: {}", boatType.getType());

                    // 获取该船类型的无人船列表
                    List<String> boatIds = cacheCenter.getBoatDeviceIdsByDeviceType(boatType.getType());
                    if (boatIds.isEmpty()) {
                        log.warn("没有找到 {} 类型的无人船", boatType.getType());
                        continue;
                    }

                    for (String boatDeviceId : boatIds) {
                        String redisKey = boatType.getType() + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + boatDeviceId;

                        // 获取导航数据
                        String navigationData = redisUtil.getByType(boatType, redisKey);
                        if (StringUtils.isEmpty(navigationData)) {
                            log.error("redis中无组合导航数据 for boatType: {}, deviceId: {}", boatType.getType(), boatDeviceId);
                            continue;
                        }

                        // 解析导航数据
                        IntegratedNavigationInfo integratedNavigationInfo = JSON.parseObject(navigationData, IntegratedNavigationInfo.class);
                        if (integratedNavigationInfo == null) {
                            log.error("redis转换实体后为空 for boatType: {}, deviceId: {}", boatType.getType(), boatDeviceId);
                            continue;
                        }

                        // 保存导航记录
                        saveNavigationRecord(boatDeviceId, integratedNavigationInfo);
                    }
                }
            } catch (Exception e) {
                log.error("定时存储导航记录失败: {}", e.getMessage(), e);
            }

            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                log.error("定时存储导航记录线程处理异常: {}", e.getMessage(), e);
                // 重新设置中断状态
                Thread.currentThread().interrupt();
            }
        }
    }

    //保存导航记录的条件
    private void saveNavigationRecord(String boatDeviceId, IntegratedNavigationInfo integratedNavigationInfo) {
        BoatNavigationRecords record = new BoatNavigationRecords();
        record.setBoatDeviceId(boatDeviceId);
        BoatNavigationRecords navigationRecord = boatNavigationRecordsService.getNavigationRecordsByCondition(record);

        if (navigationRecord == null) {
            log.error("当前设备编号：{}，没有航点数据", boatDeviceId);
            record.setBoatDeviceId(boatDeviceId);
            // todo 后续再处理船名称问题
            /*record.setDeviceName(integratedNavigationInfo.getDeviceName());*/
            record.setHeading(integratedNavigationInfo.getHeading());
            record.setHeadingAngle(integratedNavigationInfo.getHeadingAngle());
            record.setHeadingAngleSpeed(integratedNavigationInfo.getHeadingAngleSpeed());
            record.setLongitude(integratedNavigationInfo.getLongitude());
            record.setLatitude(integratedNavigationInfo.getLatitude());
            record.setCreatedTime(new java.util.Date());
            if (boatNavigationRecordsService.addNavigationRecord(record)) {
                log.info("当前设备编号：{}，第一次保存航点数据成功!", boatDeviceId);
            }
            return;
        }


        double latitudeDelta = Math.abs(navigationRecord.getLatitude() - integratedNavigationInfo.getLatitude());
        double longitudeDelta = Math.abs(navigationRecord.getLongitude() - integratedNavigationInfo.getLongitude());
        // 经纬度在2米的误差范围
        double distanceThreshold = 0.00002;
        // 航向角速度在0.05m/s以内-就当做停船了
        double speedThreshold = 0.05;

        // 航点数据和表中最后一条新数据经纬度在2米以内，或者航点数据速度小于0.05，不保存
        if (latitudeDelta <= distanceThreshold && longitudeDelta <= distanceThreshold || navigationRecord.getHeadingAngleSpeed() < speedThreshold) {
            log.info("设备编号：{}，航点数据未发生变化,可能停船不需要保存!!!。当前经纬度：({}, {})，将要记录的经纬度：({}, {})，航向角速度：{}",
                    boatDeviceId,
                    integratedNavigationInfo.getLatitude(),
                    integratedNavigationInfo.getLongitude(),
                    navigationRecord.getLatitude(),
                    navigationRecord.getLongitude(),
                    navigationRecord.getHeadingAngleSpeed());
        } else {
            record.setBoatDeviceId(boatDeviceId);
            // todo 后续再处理船名称问题
            /*record.setDeviceName(integratedNavigationInfo.getDeviceName());*/
            record.setHeading(integratedNavigationInfo.getHeading());
            record.setHeadingAngle(integratedNavigationInfo.getHeadingAngle());
            record.setHeadingAngleSpeed(integratedNavigationInfo.getHeadingAngleSpeed());
            record.setLongitude(integratedNavigationInfo.getLongitude());
            record.setLatitude(integratedNavigationInfo.getLatitude());
            record.setCreatedTime(new java.util.Date());
            if (boatNavigationRecordsService.addNavigationRecord(record)) {
                log.info("设备编号：{}，定时保存航点数据成功!", boatDeviceId);
            }
        }
    }

}
