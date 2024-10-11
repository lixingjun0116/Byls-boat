package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.mapper.WaypointMapper;
import com.byls.boat.service.IWaypointService;
import org.springframework.stereotype.Service;

/**
 * 航路点表Service实现类
 */
@Service
public class WaypointServiceImpl extends ServiceImpl<WaypointMapper, Waypoint> implements IWaypointService {
}
