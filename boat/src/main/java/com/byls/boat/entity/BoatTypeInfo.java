package com.byls.boat.entity;

public class BoatTypeInfo {
    private String type;
    private String boatTypeName;

    public BoatTypeInfo(String type, String boatTypeName) {
        this.type = type;
        this.boatTypeName = boatTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoatTypeName() {
        return boatTypeName;
    }

    public void setBoatTypeName(String boatTypeName) {
        this.boatTypeName = boatTypeName;
    }
}
