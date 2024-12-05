package com.byls.boat.entity.hardware;

import com.byls.boat.constant.BoatType;
import lombok.Data;

/**
 * @Description 接收船端硬件数据
 * @Date 2024/11/5 9:57
 * @Created by lxj
 */
@Data
public class BoatHardWareContext {
    //标识符
    private String keyId;
    //船类型枚举
    private BoatType boatType;
    //数据
    private String jsonData;
}
