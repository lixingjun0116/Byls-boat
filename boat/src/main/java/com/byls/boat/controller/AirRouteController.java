package com.byls.boat.controller;

import com.byls.boat.common.ResponseResult;
import com.byls.boat.entity.BoatCourseMaking;
import com.byls.boat.service.IBoatCourseMakingService;
import com.byls.boat.util.ResponseUtil;
import com.byls.boat.vo.BoatRouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 制作航线页面控制层
 * @Date 2024/8/1 14:41
 * @Created by lxj
 */
@Slf4j
@RestController
@RequestMapping("/api/airRoute/v1")
public class AirRouteController {
    @Autowired
    private IBoatCourseMakingService boatCourseMakingService;

    // 航线准备
    @DeleteMapping("/prepareCourseMaking")
    public ResponseResult<?> prepareCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            boatCourseMakingService.deleteAllCoursePoints(boatDeviceId);
        } catch (Exception e) {
            log.error("航线准备失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
        return ResponseUtil.successResponse();
    }

    // 航线采集
    @PostMapping("/collectCourseMaking")
    public ResponseResult<?> collectCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            BoatCourseMaking boatCourseMaking = boatCourseMakingService.collectCourseMaking(boatDeviceId);
            if (boatCourseMaking == null) {
                log.error("航线采集失败: 未找到航线数据");
                return ResponseUtil.failResponse();
            }
            return ResponseUtil.successResponse(boatCourseMaking);
        } catch (Exception e) {
            log.error("航线采集失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 航线保存-制作完成
    @PostMapping("/saveRoute")
    public ResponseResult<?> saveRoute(@Validated @RequestBody BoatRouteVO boatRouteVO) {
        if (CollectionUtils.isEmpty(boatRouteVO.getCourseMakingList())) {
            log.error("航线保存失败: 课程制作列表为空");
            return ResponseUtil.failResponse();
        }
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.saveRoute(boatRouteVO));
        } catch (Exception e) {
            log.error("航线保存失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 查询航线制作表数据
    @GetMapping("/courseMakingList")
    public ResponseResult<?> queryCourseMaking(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.getAllCoursePoints(boatDeviceId));
        } catch (Exception e) {
            log.error("查询航线制作表数据失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 修改航线制作表数据
    @PutMapping("/updateCourseMaking")
    public ResponseResult<?> updateCourseMaking(@RequestBody BoatCourseMaking course) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.updateCoursePoint(course));
        } catch (Exception e) {
            log.error("修改航线制作表数据失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 批量修改航线制作表数据
    @PutMapping("/updateCourseMakings")
    public ResponseResult<?> updateCourseMakings(@RequestBody List<BoatCourseMaking> courseList) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.updateBatch(courseList));
        } catch (Exception e) {
            log.error("批量修改航线制作表数据失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 删除航线制作表数据
    @DeleteMapping("/deleteCourseMaking")
    public ResponseResult<?> deleteCourseMaking(@RequestParam Integer id) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.deleteCoursePoint(id));
        } catch (Exception e) {
            log.error("删除航线制作表数据失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 该船是否存在有效航线 入参：船设备id
    @GetMapping("/isExistRoute")
    public ResponseResult<?> isExistRoute(@RequestParam("boatDeviceId") String boatDeviceId, @RequestParam("routeName") String routeName) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.isExistValidRoute(boatDeviceId, routeName));
        } catch (Exception e) {
            log.error("查询有效航线失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 当前是否正在录制航点
    @GetMapping("/isRecording")
    public ResponseResult<?> isRecording(@RequestParam("boatDeviceId") String boatDeviceId) {
        try {
            return ResponseUtil.successResponse(boatCourseMakingService.isRecording(boatDeviceId));
        } catch (Exception e) {
            log.error("查询录制状态失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 设置录制航线状态
    @PutMapping("/setRecordingStatus")
    public ResponseResult<?> setRecordingStatus(@RequestParam("boatDeviceId") String boatDeviceId, @RequestParam("status") Integer status) {
        try {
            if (status != 0 && status != 1) {
                log.error("设置录制航线状态失败-参数错误: {}", status);
                return ResponseUtil.failResponse();
            }
            boolean setResult = boatCourseMakingService.setRecordingStatus(boatDeviceId, status);
            if (!setResult) {
                log.error("设置录制航线状态失败: 船设备id {}", boatDeviceId);
                return ResponseUtil.failResponse("设置录制航线状态失败");
            }
            return ResponseUtil.successResponse();
        } catch (Exception e) {
            log.error("设置录制航线状态失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }

    // 通过socket发送航路信息到船控
    @PostMapping("/sendRouteInfo")
    public ResponseResult<?> sendRouteInfo(@RequestParam String routeCode) {
        try {
            boolean sendResult = boatCourseMakingService.sendRouteInfo(routeCode);
            if (!sendResult) {
                log.error("发送航路信息失败: 路由代码 {}", routeCode);
                return ResponseUtil.failResponse();
            }
            return ResponseUtil.successResponse();
        } catch (Exception e) {
            log.error("发送航路信息失败: {}", e.getMessage(), e);
            return ResponseUtil.failResponse();
        }
    }
}
