package com.byls.boat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 请求入参
 * @Date 2025/1/9 09:16
 * @Created by lxj
 */
@Data
public class BoatRequestVO {
    // 船设备id
    @NotBlank(message = "船设备编号不能为空")
    private String boatDeviceId;
}
