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
    boolean updateRoute(Route route);

}
