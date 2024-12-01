package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.mapper.UnmannedShipMapper;
import com.byls.boat.service.IUnmannedShipService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 无人船设备表Service实现类
 */
@Service
public class UnmannedShipServiceImpl extends ServiceImpl<UnmannedShipMapper, UnmannedShip> implements IUnmannedShipService {
    @Override
    public List<UnmannedShip> getUnmannedShipList() {
        try {
            return this.list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UnmannedShip getUnmannedShip(String boatDeviceId) {
        //条件查询 根据设备编号查询
        return this.getOne(new QueryWrapper<UnmannedShip>().eq("ship_code",boatDeviceId));
    }

    @Override
    public boolean addUnmannedShip(UnmannedShip unmannedShip) {
        return false;
    }

    @Override
    public boolean updateUnmannedShip(UnmannedShip unmannedShip) {
        return false;
    }

    @Override
    public boolean deleteUnmannedShip(String deviceId) {
        return false;
    }
}
