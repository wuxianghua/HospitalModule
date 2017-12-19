package com.palmap.huayitonglib.bean;

import android.text.TextUtils;

/**
 * Created by yibo.liu on 2017/10/24 19:43.
 */

public class FloorBean {

    private String areaFilename;
    private String frameFilename;
    private String facilityFilename;

    public String getAreaFilename() {
        return areaFilename;
    }

    public void setAreaFilename(String areaFilename) {
        this.areaFilename = areaFilename;
    }

    public String getFrameFilename() {
        return frameFilename;
    }

    public void setFrameFilename(String frameFilename) {
        this.frameFilename = frameFilename;
    }

    public String getFacilityFilename() {
        return facilityFilename;
    }

    public void setFacilityFilename(String facilityFilename) {
        this.facilityFilename = facilityFilename;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloorBean) {
            FloorBean floorBean = (FloorBean) obj;
            if (TextUtils.equals(this.areaFilename, floorBean.getAreaFilename()) && TextUtils.equals(this
                    .frameFilename, floorBean.getFrameFilename()) && TextUtils.equals(this.facilityFilename, floorBean
                    .getFacilityFilename())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FloorBean{" +
                "areaFilename='" + areaFilename + '\'' +
                ", frameFilename='" + frameFilename + '\'' +
                ", facilityFilename='" + facilityFilename + '\'' +
                '}';
    }
}
