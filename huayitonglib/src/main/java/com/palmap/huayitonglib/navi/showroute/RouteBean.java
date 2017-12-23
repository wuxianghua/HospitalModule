package com.palmap.huayitonglib.navi.showroute;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.palmap.huayitonglib.navi.astar.navi.AStarPath;

import java.util.List;

/**
 * Created by yibo.liu on 2017/12/19 18:24.
 */

public class RouteBean {
    //astart算法算出来的数据
    private List<AStarPath> routes;
    private double totalDistance;
    private int time;

    //起点信息
    private long fromFloorId;
    private LatLng fromLatlng;
    private FeatureCollection fromFeatureCollection;
    private LatLng fromConnection;
    private double fromDistance;

    //终点信息
    private long toFloorId;
    private LatLng toLatLng;
    private FeatureCollection toFeatureCollection;
    private LatLng toConnection;
    private double toDistance;

    public List<AStarPath> getRoutes() {
        return routes;
    }

    public void setRoutes(List<AStarPath> routes) {
        this.routes = routes;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public long getFromFloorId() {
        return fromFloorId;
    }

    public void setFromFloorId(long fromFloorId) {
        this.fromFloorId = fromFloorId;
    }

    public LatLng getFromLatlng() {
        return fromLatlng;
    }

    public void setFromLatlng(LatLng fromLatlng) {
        this.fromLatlng = fromLatlng;
    }

    public FeatureCollection getFromFeatureCollection() {
        return fromFeatureCollection;
    }

    public void setFromFeatureCollection(FeatureCollection fromFeatureCollection) {
        this.fromFeatureCollection = fromFeatureCollection;
    }

    public LatLng getFromConnection() {
        return fromConnection;
    }

    public void setFromConnection(LatLng fromConnection) {
        this.fromConnection = fromConnection;
    }

    public double getFromDistance() {
        return fromDistance;
    }

    public void setFromDistance(double fromDistance) {
        this.fromDistance = fromDistance;
    }

    public long getToFloorId() {
        return toFloorId;
    }

    public void setToFloorId(long toFloorId) {
        this.toFloorId = toFloorId;
    }

    public LatLng getToLatLng() {
        return toLatLng;
    }

    public void setToLatLng(LatLng toLatLng) {
        this.toLatLng = toLatLng;
    }

    public FeatureCollection getToFeatureCollection() {
        return toFeatureCollection;
    }

    public void setToFeatureCollection(FeatureCollection toFeatureCollection) {
        this.toFeatureCollection = toFeatureCollection;
    }

    public LatLng getToConnection() {
        return toConnection;
    }

    public void setToConnection(LatLng toConnection) {
        this.toConnection = toConnection;
    }

    public double getToDistance() {
        return toDistance;
    }

    public void setToDistance(double toDistance) {
        this.toDistance = toDistance;
    }

    public int getTime() {
        if (this.time == 0) {
            double time = 0;
            if (totalDistance != 0 && RouteConfig.VELOCITY_SIMULATE != 0) {
                time = totalDistance / RouteConfig.VELOCITY_SIMULATE;
            }
            return ((int) time);
        }
        return time;
    }

    @Override
    public String toString() {
        return "RouteBean{" +
                "routes=" + routes +
                ", totalDistance=" + totalDistance +
                ", time=" + time +
                ", fromFloorId=" + fromFloorId +
                ", fromLatlng=" + fromLatlng +
                ", fromFeatureCollection=" + fromFeatureCollection +
                ", fromConnection=" + fromConnection +
                ", fromDistance=" + fromDistance +
                ", toFloorId=" + toFloorId +
                ", toLatLng=" + toLatLng +
                ", toFeatureCollection=" + toFeatureCollection +
                ", toConnection=" + toConnection +
                ", toDistance=" + toDistance +
                '}';
    }
}
