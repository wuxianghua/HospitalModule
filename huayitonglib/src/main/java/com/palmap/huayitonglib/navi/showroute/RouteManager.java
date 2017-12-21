package com.palmap.huayitonglib.navi.showroute;

import android.content.Context;
import android.graphics.Color;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.palmap.huayitonglib.navi.astar.navi.AStarPath;
import com.palmap.huayitonglib.navi.route.INavigateManager;
import com.palmap.huayitonglib.navi.route.MapBoxNavigateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yibo.liu on 2017/12/19 17:38.
 */

public class RouteManager implements IRoute<MapboxMap, FeatureCollection> {
    public static final String TAG = RouteManager.class.getSimpleName();

    private static final String CONNECTION_IMAGE_NAME = "connection_image";
    public static final String LAYERID__ROUTE = "layerid-route";
    private static final String SOURCEID__ROUTE = "sourceid-route";
    private static final String LAYERID_CONNECTION = "layerid-connection";
    private static final String SOURCEID_CONNECTION = "sourceid-connection";

    private static RouteManager sInstance;

    private MapboxMap mMapboxMap;
    private MapBoxNavigateManager mNavigateManager;

    private RouteBean mRouteBean;
    private List<String> mLayerIds;
    private String mAboveId;

    //    private PlanRouteListener mPlanRouteListener;
    private List<PlanRouteListener> mPlanRouteListeners;

    private RouteManager() {

    }

    public static RouteManager get() {
        if (sInstance == null) {
            synchronized (RouteManager.class) {
                if (sInstance == null) {
                    sInstance = new RouteManager();
                }
            }
        }
        return sInstance;
    }

    //astar算法计算路线的监听器
    private INavigateManager.Listener<FeatureCollection> mListener = new INavigateManager.Listener<FeatureCollection>
            () {
        @Override
        public void onNavigateComplete(INavigateManager.NavigateState state, List<AStarPath> routes, double fromX,
                                       double fromY, long fromPlanargraph, FeatureCollection from, double fromConX,
                                       double fromConY, double toX, double toY, long toPlanargraph, FeatureCollection
                                               to, double toConX, double toConY) {

            if (state == INavigateManager.NavigateState.OK) {
                mRouteBean.setRoutes(routes);
                mRouteBean.setFromFloorId(fromPlanargraph);
                LatLng fromConlatLng = null;
                if (fromConX != 0) {
                    fromConlatLng = CoordinateUtils.webMercator2LatLng(fromConX, fromConY);
                }
                mRouteBean.setFromConnection(fromConlatLng);
                mRouteBean.setFromFeatureCollection(from);
                LatLng fromLatlng = CoordinateUtils.webMercator2LatLng(fromX, fromY);
                mRouteBean.setFromLatlng(fromLatlng);

                mRouteBean.setToFloorId(toPlanargraph);
                LatLng toConlatLng = null;
                if (toConX != 0) {
                    toConlatLng = CoordinateUtils.webMercator2LatLng(toConX, toConY);
                }
                mRouteBean.setToConnection(toConlatLng);
                mRouteBean.setToFeatureCollection(to);
                LatLng toLatLng = CoordinateUtils.webMercator2LatLng(toX, toY);
                mRouteBean.setToLatLng(toLatLng);

                notifyPlanRouteSuccess();

            } else {
                notifyPlanRouteError();
            }
        }
    };

    private void notifyPlanRouteError() {
        for (PlanRouteListener planRouteListener : mPlanRouteListeners) {
            if (planRouteListener != null) {
                planRouteListener.onError();
            }
        }
    }

    private void notifyPlanRouteSuccess() {
        for (PlanRouteListener planRouteListener : mPlanRouteListeners) {
            if (planRouteListener != null) {
                planRouteListener.onSuccess(mRouteBean);
            }
        }
    }


    /**
     * 初始化
     *
     * @param context
     * @param mapboxMap     地图引擎
     * @param routeDataPath 地图数据路径
     * @param resId         连接点图标资源id
     * @param aboveId       导航线要在那个层之上
     */
    @Override
    public void init(Context context, MapboxMap mapboxMap, String routeDataPath, int resId, String aboveId) {

        mMapboxMap = mapboxMap;
        mMapboxMap.addImage(CONNECTION_IMAGE_NAME, BitmapUtils.decodeSampledBitmapFromResource(context.getResources()
                , resId, 100, 100));

        mNavigateManager = new MapBoxNavigateManager(context, routeDataPath);
        mNavigateManager.setNavigateListener(mListener);
        mRouteBean = new RouteBean();
        mLayerIds = new ArrayList<>();
        mAboveId = aboveId;

        mPlanRouteListeners = new ArrayList<>();
    }

    /**
     * 设置规划路线的监听器
     *
     * @param listener
     */
    @Override
    public void registerPlanRouteListener(PlanRouteListener listener) {
        if (!mPlanRouteListeners.contains(listener)) {
            mPlanRouteListeners.add(listener);
        }
    }

    @Override
    public void planRoute(double fromlong, double fromlat, long fromPlanargraph, double tolong, double tolat, long
            toPlanargraph) {

        Position fromPosition = CoordinateUtils.latlng2WebMercator(fromlat, fromlong);
        Position toPosition = CoordinateUtils.latlng2WebMercator(tolat, tolong);

        mNavigateManager.navigation(fromPosition.getLongitude(), fromPosition.getLatitude(), fromPlanargraph,
                toPosition.getLongitude(), toPosition.getLatitude(), toPlanargraph);
    }

    /**
     * @param source
     */
    @Override
    public void showNaviRoute(FeatureCollection source) {
        drawLine(source, SOURCEID__ROUTE, LAYERID__ROUTE, mAboveId);
    }

    /**
     * 显示导航路线
     *
     * @param currentFloorId 当前楼层
     */
    @Override
    public void showNaviRoute(long currentFloorId) {
        if (currentFloorId == mRouteBean.getFromFloorId() && mRouteBean.getFromFeatureCollection() != null) {
            showStartRoute();
        } else if (currentFloorId == mRouteBean.getToFloorId() && mRouteBean.getToFeatureCollection() != null) {
            showEndRoute();
        }
    }

    private void showStartRoute() {
        drawLine(mRouteBean.getFromFeatureCollection(), SOURCEID__ROUTE, LAYERID__ROUTE, mAboveId);
        drawPoint(mRouteBean.getFromConnection(), SOURCEID_CONNECTION, LAYERID_CONNECTION);
    }

    /**
     * 是否跨楼层
     *
     * @return
     */
    private boolean isAcrossTheFloor() {
        if (mRouteBean.getFromFloorId() == mRouteBean.getToFloorId()) return false;
        return true;
    }

    /**
     * 显示终点楼层的路线
     */
    private void showEndRoute() {
        drawLine(mRouteBean.getToFeatureCollection(), SOURCEID__ROUTE, LAYERID__ROUTE, mAboveId);
        drawPoint(mRouteBean.getToConnection(), SOURCEID_CONNECTION, LAYERID_CONNECTION);
    }

    /**
     * 清除显示路线
     */
    @Override
    public void clearRoute() {
        for (String layerId : mLayerIds) {
            if (mMapboxMap.getLayer(layerId) != null) {
                mMapboxMap.removeLayer(layerId);
            }
        }
    }

    /**
     * 将保存的路线信息清除，请谨慎调用
     */
    @Override
    public void clearRouteRecord() {
        mRouteBean = new RouteBean();
    }

    /**
     * 绘制一条线
     *
     * @param source
     * @param sourceId
     * @param layerId
     */
    private void drawLine(FeatureCollection source, String sourceId, String layerId, String aboveId) {
        try {
            if (mMapboxMap.getLayer(layerId) == null) {
                GeoJsonSource jsonSource;

                if (source == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    jsonSource = new GeoJsonSource(sourceId, featureCollection);
                } else {
                    jsonSource = new GeoJsonSource(sourceId, source);
                }

                if (mMapboxMap.getSource(sourceId) != null) {
                    mMapboxMap.removeSource(sourceId);
                }
                mMapboxMap.addSource(jsonSource);

                LineLayer startLayer = new LineLayer(layerId, sourceId);
                startLayer.setProperties(
                        //加虚线- - - - - - - - - - - -
                        PropertyFactory.lineDasharray(new Float[]{3f, 1f}),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineWidth(2.5f),
                        PropertyFactory.lineColor(Color.parseColor("#ff3333")));

                mLayerIds.add(layerId);
                if (mMapboxMap.getLayer(aboveId) != null) {
                    mMapboxMap.addLayerAbove(startLayer, aboveId);
                } else {
                    mMapboxMap.addLayer(startLayer);
                }

            } else {
                GeoJsonSource geoJsonSource = (GeoJsonSource) mMapboxMap.getSourceAs(sourceId);
                if (source == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource.setGeoJson(featureCollection);
                } else {
                    geoJsonSource.setGeoJson(source);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制一个点
     *
     * @param latLng
     * @param sourceId
     * @param layerId
     */
    private void drawPoint(LatLng latLng, String sourceId, String layerId) {
        try {
            if (mMapboxMap.getLayer(layerId) == null) {

                GeoJsonSource geoJsonSource;

                if (latLng == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource = new GeoJsonSource(sourceId, featureCollection);
                } else {
                    Point point = Point.fromCoordinates(Position.fromCoordinates(latLng.getLongitude(), latLng
                            .getLatitude()));
                    geoJsonSource = new GeoJsonSource(sourceId, point);
                }

                if (mMapboxMap.getSource(sourceId) != null) {
                    mMapboxMap.removeSource(sourceId);
                }

                mMapboxMap.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer(layerId, sourceId);
                symbolLayer.setProperties(PropertyFactory.iconImage(CONNECTION_IMAGE_NAME), PropertyFactory
                        .iconAnchor(Property.ICON_ANCHOR_BOTTOM));
                mLayerIds.add(layerId);
                if (mMapboxMap.getLayer(LAYERID__ROUTE) != null) {
                    mMapboxMap.addLayerAbove(symbolLayer, LAYERID__ROUTE);
                } else {
                    mMapboxMap.addLayer(symbolLayer);
                }
            } else {

                GeoJsonSource geoJsonSource = (GeoJsonSource) mMapboxMap.getSourceAs(sourceId);
                if (latLng == null) {
                    FeatureCollection featureCollection = FeatureCollection.fromFeatures(Collections.EMPTY_LIST);
                    geoJsonSource.setGeoJson(featureCollection);
                } else {
                    geoJsonSource.setGeoJson(Point.fromCoordinates(Position.fromCoordinates(latLng.getLongitude(),
                            latLng.getLatitude())));
                }
            }
        } catch (Exception e) {

        }
    }
}
