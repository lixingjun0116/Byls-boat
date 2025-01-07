package com.byls.boat.service;

import com.byls.boat.entity.Waypoint;
import com.byls.boat.vo.BoatDynamicsInfoVO;
import com.byls.boat.vo.BoatPushRodVO;
import com.byls.boat.vo.IntegratedNavigationInfoVO;

import java.util.List;

/**
 * @Description 船控页面服务
 * @Date 2024/10/11 9:06
 * @Created by lxj
 */
public interface BoatHullService {
    //获取船实时状态
    String getBoatStatus(String boatDeviceId);
    // 获取船当前控制模式
    String getBoatControlMode(String boatDeviceId);
    // 切换船控模式
    void switchBoatControlMode(String boatDeviceId, String controlMode);
    //获取船实时坐标
    IntegratedNavigationInfoVO getCurrentLocation(String boatDeviceId);
    //控制左右推杆
    void controlLeftRightPushRod(BoatPushRodVO boatPushRodVO);
    //展示船实时信息
    BoatDynamicsInfoVO showBoatDynamicsInfo(String boatDeviceId);
    //查询预跑航线
    List<Waypoint> getPreRunRoute(String boatDeviceId);
    // 获取左右推杆值
    BoatPushRodVO getLeftRightPushRodValue(String boatDeviceId);
}
