package com.byls.boat.vo;

import com.byls.boat.entity.BoatCourseMaking;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description 保存航线
 * @Date 2024/9/23 17:00
 * @Created by lxj
 */
@Data
public class BoatRouteVO {
    //航线名称 必填
    @NotBlank(message = "航线名称不能为空")
    private String routeName;
    //船设备编号
    @NotBlank(message = "船设备编号不能为空")
    private String boatDeviceId;
    //航线描述
    private String description;
    //航线点集合
    private List<BoatCourseMaking> courseMakingList;
}
