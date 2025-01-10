package com.byls.boat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class SwitchModeRequest {
       @NotBlank(message = "boatDeviceId 不能为空")
       private String boatDeviceId;

       @NotBlank(message = "state 不能为空")
       @Pattern(regexp = "^([01])$", message = "state 必须为 0 或 1")
       private String state;
   }
   