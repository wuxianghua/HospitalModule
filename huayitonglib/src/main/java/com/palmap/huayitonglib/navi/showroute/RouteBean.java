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

    //起点信息
    private long fromFloorId;
    private LatLng fromLatlng;
    private FeatureCollection fromFeatureCollection;
    private LatLng fromConnection;

    //终点信息
    private long toFloorId;
    private LatLng toLatLng;
    private FeatureCollection toFeatureCollection;
    private LatLng toConnection;

    public List<AStarPath> getRoutes() {
        return routes;
    }

    public void setRoutes(List<AStarPath> routes) {
        this.routes = routes;
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
}
