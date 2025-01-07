package com.byls.boat.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 左右推杆数据
 * @Date 2025/1/7 11:46
 * @Created by lxj
 */
@Data
public class BoatPushRodVO {
    // 船设备ID
    @NotBlank(message = "船设备ID不能为空")
    private String boatDeviceId;
    // 左推杆值
    @NotNull(message = "左推杆值不能为空")
    @Min(value = 0, message = "左推杆值不能小于0")
    @Max(value = 120, message = "左推杆值不能大于120")
    private Integer leftValue;

    // 右推杆值
    @NotNull(message = "右推杆值不能为空")
    @Min(value = 0, message = "右推杆值不能小于0")
    @Max(value = 120, message = "右推杆值不能大于120")
    private Integer rightValue;

    public BoatPushRodVO(String boatDeviceId, int leftValue, int rightValue) {
        this.boatDeviceId = boatDeviceId;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }
}
