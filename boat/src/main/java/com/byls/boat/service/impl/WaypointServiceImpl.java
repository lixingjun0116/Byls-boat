package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.Waypoint;
import com.byls.boat.mapper.WaypointMapper;
import com.byls.boat.service.IWaypointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 航路点表Service实现类
 */
@Service
public class WaypointServiceImpl extends ServiceImpl<WaypointMapper, Waypoint> implements IWaypointService {

    /**
     * 根据条件更新航路点
     * @author lixingjun
     * @date 2024/11/6 10:37
     * @param waypoint
     * @return boolean
     */
    @Override
    public boolean updateWaypointByCondition(Waypoint waypoint) {
        return baseMapper.updateById(waypoint) > 0;
    }

    /**
     * 根据条件删除航路点
     * @author lixingjun
     * @date 2024/11/6 10:37
     * @param way
     * @return boolean
     */
    @Override
    public boolean deleteWaypointByCondition(Waypoint way) {
        LambdaUpdateWrapper<Waypoint> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Waypoint::getDeleteStatus, true);
        if (way.getId() != null){
            updateWrapper.eq(Waypoint::getId, way.getId());
        }
        if (way.getRouteCode() != null){
            updateWrapper.eq(Waypoint::getRouteCode, way.getRouteCode());
        }
        return this.update(null, updateWrapper);
    }

    /**
     * 根据条件查询航路点
     * @author lixingjun
     * @date 2024/11/6 10:38
     * @param waypoint
     * @return List<Waypoint>
     */
    @Override
    public List<Waypoint> getWaypointByCondition(Waypoint waypoint) {
        waypoint.setDeleteStatus(false);
        return baseMapper.selectList(new QueryWrapper<>(waypoint));
    }
}
