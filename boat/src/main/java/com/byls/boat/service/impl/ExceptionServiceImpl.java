package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Exception;
import com.byls.boat.mapper.ExceptionMapper;
import com.byls.boat.service.IExceptionService;
import org.springframework.stereotype.Service;

/**
 * 异常信息表Service实现类
 */
@Service
public class ExceptionServiceImpl extends ServiceImpl<ExceptionMapper, Exception> implements IExceptionService {
}
