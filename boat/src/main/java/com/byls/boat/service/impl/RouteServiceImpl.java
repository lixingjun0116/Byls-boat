package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Route;
import com.byls.boat.mapper.RouteMapper;
import com.byls.boat.service.IRouteService;
import org.springframework.stereotype.Service;

/**
 * 航线表Service实现类
 */
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements IRouteService {
}
