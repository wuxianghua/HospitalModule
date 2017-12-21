package com.palmap.huayitonglib.navi.showroute;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.models.Position;

import static com.mapbox.services.commons.models.Position.fromCoordinates;

/**
 * Created by yibo.liu on 2017/12/21 11:24.
 */

public class CoordinateUtils {

    /**
     * 经纬度转墨卡托
     *
     * @param lat
     * @param lng
     * @return
     */
    public static Position latlng2WebMercator(double lat, double lng) {
        double x = lng * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        Position position = fromCoordinates(x, y);
        return position;
    }

    /**
     * 墨卡托转经纬度
     *
     * @param x
     * @param y
     * @return
     */
    public static LatLng webMercator2LatLng(double x, double y) {
        double x1 = x / 20037508.34 * 180.0;
        double y1 = y / 20037508.34 * 180.0;
        y1 = 180 / Math.PI * (2 * Math.atan(Math.exp(y1 * Math.PI / 180)) - Math.PI / 2);
        return new LatLng(y1, x1);
    }

    /**
     * 计算角度
     *
     * @param coordinates1
     * @param coordinates2
     * @return
     */
    public static double bearing(Position coordinates1, Position coordinates2) {
        double degrees2radians = Math.PI / 180;
        double radians2degrees = 180 / Math.PI;

        double lon1 = degrees2radians * coordinates1.getLongitude();
        double lon2 = degrees2radians * coordinates2.getLongitude();
        double lat1 = degrees2radians * coordinates1.getLatitude();
        double lat2 = degrees2radians * coordinates2.getLatitude();
        double a = Math.sin(lon2 - lon1) * Math.cos(lat2);
        double b = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);

        return radians2degrees * Math.atan2(a, b);
    }
}
