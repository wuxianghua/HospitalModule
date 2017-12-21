package com.palmap.huayitonglib.navi.entity;

import static java.lang.Math.atan;
import static java.lang.Math.exp;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class NodeInfo {

    private double mX;
    private double mY;
    private double mZ;
    private LngLat mLngLat;

    public class LngLat {
        private double mLat;
        private double mLng;
        private double mAlt;

        public LngLat(double lat, double lng, double alt) {
            mLat = lat;
            mLng = lng;
            mAlt = alt;
        }

        public double getLat() {
            return mLat;
        }

        public double getLng() {
            return mLng;
        }

        public double getAlt() {
            return mAlt;
        }
    }

    public NodeInfo() {
    }

    public NodeInfo(double x, double y, double z) {
        mX = x;
        mY = y;
        mZ = z;
        webMercator2lonLat();
    }

    public NodeInfo(LngLat lngLat) {
        mLngLat = lngLat;
        lonLat2WebMercator();
    }

    public double getX() {
        return mX;
    }

    public void setX(double x) {
        this.mX = x;
        mLngLat = null;
    }

    public double getY() {
        return mY;
    }

    public void setY(double y) {
        this.mY = y;
        mLngLat = null;
    }

    public double getZ() {
        return mZ;
    }

    public void setZ(double z) {
        this.mZ = z;
        mLngLat = null;
    }

    public LngLat getLngLat() {
        if (mLngLat == null) {
            webMercator2lonLat();
        }
        return mLngLat;
    }

    public void setLngLat(LngLat lngLat) {
        this.mLngLat = lngLat;
        lonLat2WebMercator();
    }

    // Web墨卡托转经纬度
    private void webMercator2lonLat() {
        double x = mX / 20037508.34 * 180;
        double y = mY / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * atan(exp(y * Math.PI / 180)) - Math.PI / 2);
        mLngLat = new LngLat(y, x, mZ);
    }

    // 经纬度转Wev墨卡托
    private void lonLat2WebMercator() {
        double x = mLngLat.mLng * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + mLngLat.mLat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        mX = x;
        mY = y;
        mZ = mLngLat.mAlt;
    }

}
