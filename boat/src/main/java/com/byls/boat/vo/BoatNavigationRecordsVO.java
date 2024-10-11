package com.byls.boat.vo;

import lombok.Data;

@Data
public class BoatNavigationRecordsVO {
    /**
     * 航线点名称
     */
    private String routePointName;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 船体形态，当前状态
     */
    private String boatForm;

    /**
     * 船设备编号
     */
    private String boatDeviceId;

    /**
     * 生成时间，该位置信息被采集的具体时间。
     */
    private String recordTime;
}
