package com.byls.boat.service;

import com.byls.boat.vo.BoatLocationInfoVO;

/**
 * @Description 船控页面服务
 * @Date 2024/10/11 9:06
 * @Created by lxj
 */
public interface BoatHullService {

    //获取船实时坐标
    BoatLocationInfoVO getCurrentLocation();
}
