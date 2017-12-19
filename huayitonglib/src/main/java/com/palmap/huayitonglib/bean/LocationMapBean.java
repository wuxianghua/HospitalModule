package com.palmap.huayitonglib.bean;

/**
 * Created by yunyun.zhou on 2017/10/30 11:37.
 */

public class LocationMapBean {
    int poiId;
    String floorName;
    String name;
    double longitude;
    double latitude;
    int floorId;
    int imag;

    public int getImag() {
        return imag;
    }

    public void setImag(int imag) {
        this.imag = imag;
    }

    public int getPoiId() {
        return poiId;
    }

    public void setPoiId(int poiId) {
        this.poiId = poiId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    @Override
    public String toString() {
        return "LocationMapBean{" +
                "poiId=" + poiId +
                ", floorName='" + floorName + '\'' +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", floorId=" + floorId +
                ", imag=" + imag +
                '}';
    }
}
