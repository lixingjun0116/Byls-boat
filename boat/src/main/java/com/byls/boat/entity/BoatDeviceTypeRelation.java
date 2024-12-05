package com.byls.boat.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 实体类，对应数据库表 `boat_device_type_relations`
 */
@TableName("boat_device_type_relations")
public class BoatDeviceTypeRelation {

    /**
     * 主键，自增ID
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Long relationId;

    /**
     * 设备类型，外键关联 `boat_device_types` 表的 `device_type` 字段
     */
    @TableField(value = "device_type")
    private String deviceType;

    /**
     * 船设备ID
     */
    @TableField(value = "boat_device_id")
    private String boatDeviceId;

    // 默认构造函数
    public BoatDeviceTypeRelation() {
    }

    // 全参构造函数
    public BoatDeviceTypeRelation(String deviceType, String boatDeviceId) {
        this.deviceType = deviceType;
        this.boatDeviceId = boatDeviceId;
    }

    // Getter 和 Setter 方法

    /**
     * 获取主键ID
     *
     * @return 主键ID
     */
    public Long getRelationId() {
        return relationId;
    }

    /**
     * 设置主键ID
     *
     * @param relationId 主键ID
     */
    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 获取船设备ID
     *
     * @return 船设备ID
     */
    public String getBoatDeviceId() {
        return boatDeviceId;
    }

    /**
     * 设置船设备ID
     *
     * @param boatDeviceId 船设备ID
     */
    public void setBoatDeviceId(String boatDeviceId) {
        this.boatDeviceId = boatDeviceId;
    }

    // toString 方法，方便调试
    @Override
    public String toString() {
        return "BoatDeviceTypeRelation{" +
                "relationId=" + relationId +
                ", deviceType='" + deviceType + '\'' +
                ", boatDeviceId='" + boatDeviceId + '\'' +
                '}';
    }
}
