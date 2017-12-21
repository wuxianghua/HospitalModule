package com.palmap.huayitonglib.utils;

import com.mapbox.mapboxsdk.geometry.LatLng;

import static java.lang.Math.atan;
import static java.lang.Math.exp;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class CoordinateUtil {

    /**
     * 墨卡托坐标转经纬度
     * @param x x坐标
     * @param y y坐标
     * @return 经纬度坐标，下标0为经度，下标1为纬度
     */
    private double[] webMercator2lonLat(double x, double y) {
        x = x / 20037508.34 * 180;
        y = y / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * atan(exp(y * Math.PI / 180)) - Math.PI / 2);
        return new double[]{x, y};
    }

    /**
     * 经纬度转墨卡托坐标
     * @param lng 经度
     * @param lat 纬度
     * @return 墨卡托投影，索引0为x，索引1为y。
     */
    private double[] lonLat2WebMercator(double lng, double lat) {
        double x = lng * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        return new double[]{x, y};
    }

    public static LatLng webMercator2LatLng(double x1, double y1) {
        double x = x1 / 20037508.34 * 180.0;
        double y = y1 / 20037508.34 * 180.0;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        return new LatLng(y, x);
    }
}
