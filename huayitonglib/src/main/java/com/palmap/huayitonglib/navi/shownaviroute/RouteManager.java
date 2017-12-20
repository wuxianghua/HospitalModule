package com.palmap.huayitonglib.navi.shownaviroute;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
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

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by yibo.liu on 2017/12/19 17:38.
 */

public class RouteManager implements IRoute<MapboxMap, FeatureCollection> {
    public static final String TAG = RouteManager.class.getSimpleName();

    private static final String CONNECTION_IMAGE_NAME = "connection_image";
    private static final String LAYERID__ROUTE = "layerid-route";
    private static final String SOURCEID__ROUTE = "sourceid-route";
    private static final String LAYERID_CONNECTION = "layerid-connection";
    private static final String SOURCEID_CONNECTION = "sourceid-connection";

    private static RouteManager sInstance;

    private MapboxMap mMapboxMap;
    private MapBoxNavigateManager mNavigateManager;

    private PlanRouteListener mPlanRouteListener;
    private RouteBean mRouteBean;
    private List<String> mLayerIds;

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


    private INavigateManager.Listener<FeatureCollection> mListener = new INavigateManager.Listener<FeatureCollection>
            () {
        @Override
        public void onNavigateComplete(INavigateManager.NavigateState state, List<AStarPath> routes, double fromX,
                                       double fromY, long fromPlanargraph, FeatureCollection from, double fromConX,
                                       double fromConY, double toX, double toY, long toPlanargraph, FeatureCollection
                                               to, double toConX, double toConY) {

            if (state == INavigateManager.NavigateState.OK) {
                mRouteBean.setFromFloorId(fromPlanargraph);
                mRouteBean.setFromConnection(new LatLng(fromConY, fromConX));
                mRouteBean.setFromFeatureCollection(from);
                mRouteBean.setFromLatlng(new LatLng(fromY, fromX));

                mRouteBean.setToFloorId(toPlanargraph);
                mRouteBean.setToConnection(new LatLng(toConY, toConX));
                mRouteBean.setToFeatureCollection(to);
                mRouteBean.setToLatLng(new LatLng(toY, toX));

                if (mPlanRouteListener != null) {
                    mPlanRouteListener.onSuccess(mRouteBean);
                }

            } else {
                if (mPlanRouteListener != null) {
                    mPlanRouteListener.onError();
                }
            }
        }
    };

    @Override
    public void init(Context context, MapboxMap mapboxMap, String routeDataPath, String resId) {

        mMapboxMap = mapboxMap;
        mMapboxMap.addImage(CONNECTION_IMAGE_NAME, BitmapUtils.decodeSampledBitmapFromResource(context.getResources()
                , 1, 100, 100));

        mNavigateManager = new MapBoxNavigateManager(context, routeDataPath);
        mNavigateManager.setNavigateListener(mListener);
        mRouteBean = new RouteBean();
        mLayerIds = new Vector<>();
    }

    @Override
    public void setListener(PlanRouteListener listener) {
        mPlanRouteListener = listener;
    }

    @Override
    public void planRoute(double fromlong, double fromlat, long fromPlanargraph, double tolong, double tolat, long
            toPlanargraph) {
        mNavigateManager.navigation(fromlong, fromlat, fromPlanargraph, tolong, tolat, toPlanargraph);
    }

    @Override
    public void showNaviRoute(FeatureCollection source) {
        drawLine(source, SOURCEID__ROUTE, LAYERID__ROUTE);
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
        drawLine(mRouteBean.getFromFeatureCollection(), SOURCEID__ROUTE, LAYERID__ROUTE);
        if (canDrawConPoint()) {
            drawPoint(mRouteBean.getFromConnection(), SOURCEID_CONNECTION, LAYERID_CONNECTION);
        }
    }

    /**
     * 是否需要展示连接点
     *
     * @return
     */
    private boolean canDrawConPoint() {
        if (mRouteBean.getFromFloorId() == mRouteBean.getToFloorId()) return false;
        return true;
    }

    private void showEndRoute() {
        drawLine(mRouteBean.getToFeatureCollection(), SOURCEID__ROUTE, LAYERID__ROUTE);
        if (canDrawConPoint()) {
            drawPoint(mRouteBean.getToConnection(), SOURCEID_CONNECTION, LAYERID_CONNECTION);
        }
    }

    /**
     * 清除路线
     */
    @Override
    public void clearRoute() {
        mRouteBean = new RouteBean();
        for (String layerId : mLayerIds) {
            if (mMapboxMap.getLayer(layerId) != null) {
                mMapboxMap.removeLayer(layerId);
            }
        }
    }

    /**
     * 绘制一条线
     *
     * @param source
     * @param sourceId
     * @param layerId
     */
    private void drawLine(FeatureCollection source, String sourceId, String layerId) {
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

                FillExtrusionLayer startLayer = new FillExtrusionLayer(layerId, sourceId);
                mLayerIds.add(layerId);
                mMapboxMap.addLayer(startLayer);

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
            if (mMapboxMap.getLayer(layerId) != null) {

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
                symbolLayer.setProperties(PropertyFactory.iconImage(""), PropertyFactory
                        .iconAnchor(Property.ICON_ANCHOR_BOTTOM));
                mMapboxMap.addLayer(symbolLayer);
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
