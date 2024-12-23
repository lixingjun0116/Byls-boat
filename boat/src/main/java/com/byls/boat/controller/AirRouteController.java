package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.entity.BoatCourseMaking;
import com.byls.boat.service.IBoatCourseMakingService;
import com.byls.boat.util.ResponseUtil;
import com.byls.boat.vo.BoatRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Description  页面航线处理控制层
 * &#064;Date  2024/8/1 14:41
 * &#064;Created  by lxj
 */
@RestController
@RequestMapping("/api/airRoute/v1")
public class AirRouteController {
    @Autowired
    private IBoatCourseMakingService boatCourseMakingService;

    //航线准备
    @GetMapping("/prepareCourseMaking")
    public ResponseResult<?> prepareCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            boatCourseMakingService.deleteAllCoursePoints(boatDeviceId);
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
        return ResponseUtil.successResponse();
    }

    //航线采集
    @GetMapping("/collectCourseMaking")
    public ResponseResult<?> collectCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            BoatCourseMaking boatCourseMaking = boatCourseMakingService.collectCourseMaking(boatDeviceId);
            if (boatCourseMaking == null) {
                return ResponseUtil.failResponse();
            }
            return ResponseUtil.successResponse(boatCourseMaking);
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //航线保存-制作完成
    @PostMapping("/saveRoute")
    public ResponseResult<?> saveRoute(@Validated @RequestBody BoatRouteVO boatRouteVO) {
        if (CollectionUtils.isEmpty(boatRouteVO.getCourseMakingList())) {
            return ResponseUtil.failResponse();
        }
        //保存航线数据   获取航线制作表数据   存入航线表、航线航点表
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.saveRoute(boatRouteVO));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //查询航线制作表数据
    @GetMapping("/courseMakingList")
    public ResponseResult<?> queryCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.getAllCoursePoints(boatDeviceId));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //修改航线制作表数据
    @PostMapping("/updateCourseMaking")
    public ResponseResult<?> updateCourseMaking(@RequestBody BoatCourseMaking course) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.updateCoursePoint(course));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //批量修改航线制作表数据
    @PostMapping("/updateCourseMakings")
    public ResponseResult<?> updateCourseMakings(@RequestBody List<BoatCourseMaking> courseList) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.updateBatch(courseList));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //删除航线制作表数据
    @GetMapping("/deleteCourseMaking")
    public ResponseResult<?> deleteCourseMaking(@RequestParam Integer id) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.deleteCoursePoint(id));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }

    //该船是否存在有效航线 入参：船设备id
    @GetMapping("/isExistRoute")
    public ResponseResult<?> isExistRoute(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.isExistValidRoute(boatDeviceId));
        } catch (Exception e) {
            return ResponseUtil.failResponse();
        }
    }
}
