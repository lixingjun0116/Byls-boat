package com.byls.boat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byls.boat.entity.BoatNavigationRecords;
import com.byls.boat.mapper.BoatNavigationRecordsMapper;
import com.byls.boat.service.IBoatNavigationRecordsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BoatNavigationRecordsServiceImpl extends ServiceImpl<BoatNavigationRecordsMapper, BoatNavigationRecords> implements IBoatNavigationRecordsService {

    /**
     * 添加航行记录
     *
     * @param record 航行记录实体
     */
    @Override
    public boolean addNavigationRecord(BoatNavigationRecords record) {
        return this.save(record);
    }

    /**
     * 更新航行记录
     *
     * @param record 航行记录实体
     */
    @Override
    public boolean updateNavigationRecord(BoatNavigationRecords record) {
        return this.updateById(record);
    }

    /**
     * 删除航行记录
     *
     * @param id 航行记录ID
     */
    @Override
    public boolean deleteNavigationRecord(Integer id) {
        return this.removeById(id);
    }

    /**
     * 根据ID获取航行记录
     *
     * @param id 航行记录ID
     * @return 航行记录实体
     */
    @Override
    public BoatNavigationRecords getNavigationRecordById(Integer id) {
        return this.getById(id);
    }

    /**
     * 获取所有航行记录
     *
     * @return 航行记录列表
     */
    @Override
    public List<BoatNavigationRecords> getAllNavigationRecords() {
        return this.list();
    }
}
