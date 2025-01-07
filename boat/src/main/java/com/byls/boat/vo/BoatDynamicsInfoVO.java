package com.byls.boat.vo;

import com.byls.boat.entity.hardware.MillimeterWaveRadar;
import com.byls.boat.entity.hardware.MotorInfo;
import lombok.Data;

import java.util.List;

/**
 * @Description 船端实时动态信息
 * @Date 2025/1/4 15:17
 * @Created by lxj
 */
@Data
public class BoatDynamicsInfoVO {
    //组合导航信息
    private IntegratedNavigationInfoVO navigationInfoVO;
    //电机信息
    private List<MotorInfo> motorInfoList;
    //毫米波雷达数据
    private List<MillimeterWaveRadar> millimeterWaveRadarList;
}
