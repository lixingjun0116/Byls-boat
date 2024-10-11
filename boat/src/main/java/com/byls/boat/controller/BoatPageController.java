package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.service.IUnmannedShipService;
import com.byls.boat.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 页面控制、加载
 * @Date 2024/8/1 14:39
 * @Created by lxj
 */
@RestController
@RequestMapping("/api/page/v1")
public class BoatPageController {

    @Autowired
    private IUnmannedShipService unmannedShipService;

    //船设备列表
    @GetMapping("/boatDeviceList")
    public ResponseResult<?> boatDeviceList() {
        try {
            return ResponseUtil.successResponse(unmannedShipService.getUnmannedShipList());
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }
}
