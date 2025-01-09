package com.byls.boat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * &#064;Description  请求入参
 * &#064;Date  2025/1/9 09:16
 * &#064;Created  by lxj
 */
@Data
public class BoatRequestVO {
    // 船设备id
    @NotBlank(message = "船设备编号不能为空")
    private String boatDeviceId;
}
