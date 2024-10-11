package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.BoatNavigationRecords;

import java.util.List;

public interface IBoatNavigationRecordsService extends IService<BoatNavigationRecords> {

    /**
     * 添加航行记录
     * @param record 航行记录实体
     * @return 操作结果
     */
    boolean addNavigationRecord(BoatNavigationRecords record);

    /**
     * 更新航行记录
     * @param record 航行记录实体
     * @return 操作结果
     */
    boolean updateNavigationRecord(BoatNavigationRecords record);

    /**
     * 删除航行记录
     * @param id 航行记录ID
     * @return 操作结果
     */
    boolean deleteNavigationRecord(Integer id);

    /**
     * 根据ID获取航行记录
     * @param id 航行记录ID
     * @return 航行记录实体
     */
    BoatNavigationRecords getNavigationRecordById(Integer id);

    /**
     * 获取所有航行记录
     * @return 航行记录列表
     */
    List<BoatNavigationRecords> getAllNavigationRecords();
}
