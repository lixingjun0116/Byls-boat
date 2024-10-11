package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.UnmannedShip;

import java.util.List;

/**
 * 无人船设备表Service接口
 */
public interface IUnmannedShipService extends IService<UnmannedShip> {
    // 获取设备列表
    List<UnmannedShip> getUnmannedShipList();

    // 获取设备
    UnmannedShip getUnmannedShip(String deviceId);

    // 添加设备
    boolean addUnmannedShip(UnmannedShip unmannedShip);

    // 更新设备
    boolean updateUnmannedShip(UnmannedShip unmannedShip);

    // 删除设备
    boolean deleteUnmannedShip(String deviceId);
}
