package com.byls.boat.constant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BoatTypeSerializer.class)
public enum BoatType {
    TOURIST_BOAT("tourist_boat", 0, "旅游船"),
    DOLPHIN("dolphin", 2, "海豚"),
    TARGET_BOAT("target_boat", 3, "靶船");

    private final String type;
    private final int dbIndex;
    private final String boatTypeName;

    BoatType(String type, int dbIndex, String boatTypeName) {
        this.type = type;
        this.dbIndex = dbIndex;
        this.boatTypeName = boatTypeName;
    }

    public String getType() {
        return type;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public String getBoatTypeName() {
        return boatTypeName;
    }

    public static BoatType fromType(String type) {
        for (BoatType boatType : BoatType.values()) {
            if (boatType.getType().equals(type)) {
                return boatType;
            }
        }
        throw new IllegalArgumentException("未知船型: " + type);
    }
}
   