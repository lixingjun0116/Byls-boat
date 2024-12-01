package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.Waypoint;

import java.util.List;

/**
 * 航路点表Service接口
 */
public interface IWaypointService extends IService<Waypoint> {

    //根据条件修改航点
    boolean updateWaypointByCondition(Waypoint waypoint);

    //根据条件删除航点
    boolean deleteWaypointByCondition(Waypoint waypoint);
    //根据条件查询航点
    List<Waypoint> getWaypointByCondition(Waypoint waypoint);

}
