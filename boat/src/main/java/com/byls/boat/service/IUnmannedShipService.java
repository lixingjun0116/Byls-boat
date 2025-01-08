package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.constant.BoatType;
import com.byls.boat.entity.UnmannedShip;
import com.byls.boat.vo.BoatLocationAllVO;

import java.util.List;

/**
 * 无人船设备表Service接口
 */
public interface IUnmannedShipService extends IService<UnmannedShip> {
    // 获取设备列表
    List<UnmannedShip> getUnmannedShipList();

    // 获取设备
    UnmannedShip getUnmannedShip(String boatDeviceId);

    // 添加设备
    boolean addUnmannedShip(UnmannedShip unmannedShip);

    // 更新设备
    boolean updateUnmannedShip(UnmannedShip unmannedShip);

    // 删除设备
    boolean deleteUnmannedShip(String deviceId);

    //根据船类型查询船设备
    List<UnmannedShip> getUnmannedShipsByType(BoatType boatType);

    //获取所有船当前坐标信息
    List<BoatLocationAllVO> getAllBoatLocation();
}
