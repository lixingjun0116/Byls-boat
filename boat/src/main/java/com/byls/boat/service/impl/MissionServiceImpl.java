package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Mission;
import com.byls.boat.mapper.MissionMapper;
import com.byls.boat.service.IMissionService;
import org.springframework.stereotype.Service;

/**
 * 执行任务表Service实现类
 */
@Service
public class MissionServiceImpl extends ServiceImpl<MissionMapper, Mission> implements IMissionService {
}
