package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.Route;
import com.byls.boat.vo.RouteRespVO;

import java.util.List;

/**
 * 航线表Service接口
 */
public interface IRouteService extends IService<Route> {

    //查询航线列表
    List<RouteRespVO> getRoutes();
    //条件查询航线列表
    List<Route> getRoutesByCondition(Route route);
    //修改航线
    boolean updateRouteByCondition(Route route);
    // 通过id更新航线
    boolean updateRouteById(Route route);

    //通过航线编号获取航线信息
    Route getRouteByRouteCode(String routeCode);
    // 通过船设备id 获取航线集合
    List<Route> getRoutesByBoatDeviceId(String boatDeviceId);


}
