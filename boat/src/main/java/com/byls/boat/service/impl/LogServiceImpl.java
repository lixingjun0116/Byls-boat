package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Log;
import com.byls.boat.mapper.LogMapper;
import com.byls.boat.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * 日志记录表Service实现类
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
}
