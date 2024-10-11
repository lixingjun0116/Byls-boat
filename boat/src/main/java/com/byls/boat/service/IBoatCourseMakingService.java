package com.byls.boat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byls.boat.entity.BoatCourseMaking;
import com.byls.boat.vo.BoatRouteVO;

import java.util.List;

public interface IBoatCourseMakingService extends IService<BoatCourseMaking> {

    /**
     * 添加航线点
     * @param course 航线点实体
     * @return 操作结果
     */
    Integer addCoursePointAndReturnId(BoatCourseMaking course);

    /**
     * 更新航线点
     * @param course 航线点实体
     * @return 操作结果
     */
    boolean updateCoursePoint(BoatCourseMaking course);

    /**
     * 删除航线点
     * @param id 航线点ID
     * @return 操作结果
     */
    boolean deleteCoursePoint(Integer id);

    /**
     * 根据ID获取航线点
     * @param id 航线点ID
     * @return 航线点实体
     */
    BoatCourseMaking getCoursePointById(Integer id);

    /**
     * 获取所有航线点
     * @return 航线点列表
     */
    List<BoatCourseMaking> getAllCoursePoints();

    //删除所有航线点
    boolean deleteAllCoursePoints();

    //航线采集
    BoatCourseMaking collectCourseMaking();

    //批量更新
    boolean updateBatch(List<BoatCourseMaking> entityList);

    //保存航线-制作完成
    boolean saveRoute(BoatRouteVO boatRouteVO);

    //船是否存在有效航线
    boolean isExistValidRoute(String boatDeviceId);
}
