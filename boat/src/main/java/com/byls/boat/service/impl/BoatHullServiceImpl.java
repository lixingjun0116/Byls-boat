package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.entity.Route;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.entity.hardware.MillimeterWaveRadar;
import com.byls.boat.entity.hardware.MotorInfo;
import com.byls.boat.service.*;
import com.byls.boat.service.catchhandler.CacheCenter;
import com.byls.boat.util.RedisUtil;
import com.byls.boat.vo.BoatDynamicsInfoVO;
import com.byls.boat.vo.BoatPushRodVO;
import com.byls.boat.vo.IntegratedNavigationInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Description 船控页面服务实现类
 * @Date 2024/10/11 9:08
 * @Created by lxj
 */
@Service
@Slf4j
public class BoatHullServiceImpl implements BoatHullService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheCenter cacheCenter;
    @Autowired
    private IUnmannedShipService unmannedShipService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private IRouteService routeService;
    @Autowired
    private IWaypointService waypointService;


    // 获取船实时状态
    @Override
    public String getBoatStatus(String boatDeviceId) {
        String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
        BoatType boatType = BoatType.fromType(deviceType);
        String redisKey = boatType.getType() + ":" + RedisKeyConstants.STATE_INFO + ":" + boatDeviceId;
        return redisUtil.getByType(boatType, redisKey);
    }

    // 获取船当前控制模式
    @Override
    public String getBoatControlMode(String boatDeviceId) {
        List<UnmannedShip> unmannedShipList = cacheCenter.getUnmannedShipList();
        if (CollectionUtils.isEmpty(unmannedShipList)) {
            log.error("获取无人船设备列表失败");
            return "";
        }

        for (UnmannedShip unmannedShip : unmannedShipList) {
            if (unmannedShip.getShipCode().equals(boatDeviceId)) {
                return String.valueOf(unmannedShip.getCurrentMode());
            }
        }
        return "";
    }

    // 切换船控模式
    @Override
    public void switchBoatControlMode(String boatDeviceId, String controlMode) {
        try {
            // 1.更新库中的船控模式
            List<UnmannedShip> unmannedShipList = cacheCenter.getUnmannedShipList();
            if (CollectionUtils.isEmpty(unmannedShipList)) {
                log.error("获取无人船设备列表失败-为空！！！");
                return;
            }

            for (UnmannedShip unmannedShip : unmannedShipList) {
                if (unmannedShip.getShipCode().equals(boatDeviceId)) {
                    unmannedShip.setCurrentMode(Integer.parseInt(controlMode));
                    unmannedShipService.updateUnmannedShip(unmannedShip);
                    cacheCenter.catchUnmannedShipList();
                    log.info("切换船控模式成功");
                    return;
                }
            }

            //2.通知船端控制模式切换了
            String message = "{\"function\":\"C1001\",\"mode\":\"" + controlMode + "\"}";
            webSocketService.sendMessageToBoat(boatDeviceId, message);
        } catch (Exception e) {
            log.error("切换船控模式失败: {}", e.getMessage(), e);
        }
    }

    // 获取船实时坐标
    @Override
    public IntegratedNavigationInfoVO getCurrentLocation(String boatDeviceId) {
        try {
            String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
            String redisKey = deviceType + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + boatDeviceId;
            String navigationData = redisUtil.getByType(BoatType.fromType(deviceType), redisKey);
            if (navigationData == null) {
                return new IntegratedNavigationInfoVO();
            }
            IntegratedNavigationInfoVO internalInfoVO = JSON.parseObject(navigationData, IntegratedNavigationInfoVO.class);
            internalInfoVO.setBoatDeviceId(boatDeviceId);

            //组织返回信息
            UnmannedShip unmannedShip = cacheCenter.getUnmannedShipByDeviceId(boatDeviceId);
            if (unmannedShip != null) {
                internalInfoVO.setBoatDeviceName(unmannedShip.getShipName());
                internalInfoVO.setBoatDeviceType(BoatType.fromType(unmannedShip.getShipType()));
            }
            return internalInfoVO;
        } catch (Exception e) {
            return new IntegratedNavigationInfoVO();
        }
    }

    // 控制左右推杆
    @Override
    public void controlLeftRightPushRod(BoatPushRodVO boatPushRodVO) {
        try {
            String message = "{\"function\":\"C1002\",\"left_joystick\":\"" + boatPushRodVO.getLeftValue() + "\",\"right_joystick\":\"" + boatPushRodVO.getRightValue() + "\"}";
            webSocketService.sendMessageToBoat(boatPushRodVO.getBoatDeviceId(), message);
            // 本地存储左右推杆值
            cacheCenter.updatePushRodValue(boatPushRodVO);
        } catch (Exception e) {
            log.error("控制左右推杆失败: {}", e.getMessage(), e);
        }
    }

    // 展示船实时动态数据
    @Override
    public BoatDynamicsInfoVO showBoatDynamicsInfo(String boatDeviceId) {
        BoatDynamicsInfoVO boatDynamicsInfoVO = new BoatDynamicsInfoVO();
        // 1.组织组合导航数据
        boatDynamicsInfoVO.setNavigationInfoVO(getIntegratedNavigationInfo(boatDeviceId));
        // 2.组织电机信息
        boatDynamicsInfoVO.setMotorInfoList(getMotorData(boatDeviceId));
        // 3.组织毫米波雷达数据
        boatDynamicsInfoVO.setMillimeterWaveRadarList(getMillimeterWaveRadarData(boatDeviceId));
        return boatDynamicsInfoVO;
    }

    // 查询预跑航线
    @Override
    public List<Waypoint> getPreRunRoute(String boatDeviceId) {
        Route route = new Route();
        route.setBoatDeviceId(boatDeviceId);
        route.setEnableStatus(true);
        List<Route> routesByCondition = routeService.getRoutesByCondition(route);
        if (CollectionUtils.isEmpty(routesByCondition)) {
            return Collections.emptyList();
        }
        if (routesByCondition.size() > 1) {
            log.error("查询到多条预跑航线,船编号:{}", boatDeviceId);
            return Collections.emptyList();
        }
        String routeCode = routesByCondition.get(0).getRouteCode();
        Waypoint waypoint = new Waypoint();
        waypoint.setRouteCode(routeCode);
        return waypointService.getWaypointByCondition(waypoint);
    }

    //本地存储左右推杆值
    @Override
    public BoatPushRodVO getLeftRightPushRodValue(String boatDeviceId) {
        return cacheCenter.getPushRodValue(boatDeviceId);
    }

    // 组织组合导航数据
    private IntegratedNavigationInfoVO getIntegratedNavigationInfo(String boatDeviceId) {
        String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
        String redisKey = deviceType + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + boatDeviceId;
        String navigationData = redisUtil.getByType(BoatType.fromType(deviceType), redisKey);
        if (navigationData == null) {
            log.error("获取缓存导航数据失败:{}", redisKey);
        }
        IntegratedNavigationInfoVO internalInfoVO = JSON.parseObject(navigationData, IntegratedNavigationInfoVO.class);
        //组织返回信息
        UnmannedShip unmannedShip = cacheCenter.getUnmannedShipByDeviceId(boatDeviceId);
        if (unmannedShip != null && internalInfoVO != null) {
            internalInfoVO.setBoatDeviceName(unmannedShip.getShipName());
            internalInfoVO.setBoatDeviceType(BoatType.fromType(unmannedShip.getShipType()));
        }
        return internalInfoVO;
    }

    // 组织电机信息
    private List<MotorInfo> getMotorData(String boatDeviceId) {
        String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
        String redisKey = deviceType + ":" + RedisKeyConstants.MOTOR_INFO + ":" + boatDeviceId;
        String motorData = redisUtil.getByType(BoatType.fromType(deviceType), redisKey);
        if (motorData == null) {
            log.error("获取缓存电机数据失败:{}", redisKey);
        }
        // 解析电机集合数据
        return JSON.parseArray(motorData, MotorInfo.class);
    }

    // 组织毫米波雷达数据
    private List<MillimeterWaveRadar> getMillimeterWaveRadarData(String boatDeviceId) {
        String deviceType = cacheCenter.getDeviceTypeByBoatDeviceId(boatDeviceId);
        String redisKey = deviceType + ":" + RedisKeyConstants.RADAR_INFO + ":" + boatDeviceId;
        String sensorData = redisUtil.getByType(BoatType.fromType(deviceType), redisKey);
        if (sensorData == null) {
            log.error("获取缓存毫米波雷达数据失败:{}", redisKey);
        }
        // 解析毫米波雷达集合数据
        return JSON.parseArray(sensorData, MillimeterWaveRadar.class);
    }
}
