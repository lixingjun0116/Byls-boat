package com.byls.boat.service.impl;

import com.alibaba.fastjson.JSON;
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
    public BoatLocationInfoVO getCurrentLocation() {
        try {
            String gpsData = redisUtil.get(RedisKeyConstants.BOAT_COURSE_FLAG);
            if (gpsData == null){
                return new BoatLocationInfoVO();
            }
            return JSON.parseObject(gpsData, BoatLocationInfoVO.class);
        }catch (Exception e){
            return new BoatLocationInfoVO();
        }


    }
}
