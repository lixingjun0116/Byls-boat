package com.byls.boat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.byls.boat.constant.BoatType;
import com.byls.boat.constant.RedisKeyConstants;
import com.byls.boat.service.BoatHullService;
import com.byls.boat.util.RedisUtil;
import com.byls.boat.vo.BoatLocationInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 船控页面服务实现类
 * @Date 2024/10/11 9:08
 * @Created by lxj
 */
@Service
public class BoatHullServiceImpl implements BoatHullService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BoatLocationInfoVO getCurrentLocation(String shipCode) {
        try {
            String redisKey = BoatType.TOURIST_BOAT.getType() + ":" + RedisKeyConstants.INTEGRATED_NAVIGATION_INFO + ":" + shipCode;
            String navigationData = redisUtil.getByType(BoatType.TOURIST_BOAT, redisKey);
            if (navigationData == null) {
                return new BoatLocationInfoVO();
            }
            return JSON.parseObject(navigationData, BoatLocationInfoVO.class);
        } catch (Exception e) {
            return new BoatLocationInfoVO();
        }


    }
}
