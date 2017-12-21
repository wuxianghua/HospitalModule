package com.palmap.huayitonglib.navi.showroute;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.palmap.huayitonglib.navi.NavigateFactory;
import com.palmap.huayitonglib.navi.NavigateManager;
import com.palmap.huayitonglib.navi.NavigateUpdateListener;
import com.palmap.huayitonglib.navi.entity.NaviInfo;
import com.palmap.huayitonglib.navi.entity.NodeInfo;
import com.palmap.huayitonglib.navi.entity.PartInfo;
import com.palmap.huayitonglib.navi.map.MapBoxImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yibo.liu on 2017/12/21 15:35.
 */

public class Navi {
    public static final String TAG = Navi.class.getSimpleName();

    private MapBoxImp mMapBoxImp;
    private MapboxMap mMapboxMap;
    private NavigateManager mNavigateManager;
    private ValueAnimator mValueAnimator;

    public void init(Context context, MapboxMap mapboxMap, int resId) {
        mMapBoxImp = new MapBoxImp();
        mMapBoxImp.setMapEngine(mapboxMap);
        mMapboxMap = mapboxMap;
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), resId, 100, 100);
        mMapBoxImp.addImageSource(IMAGENAE_LOCATION, bitmap);

        RouteManager.get().registerPlanRouteListener(mPlanRouteListener);

        mNavigateManager = new NavigateManager();
        mNavigateManager.setNavigateUpdateListener(mNavigateUpdateListener);
    }

    private PlanRouteListener mPlanRouteListener = new PlanRouteListener() {

        @Override
        public boolean onSuccess(RouteBean bean) {
            return false;
        }

        @Override
        public void onError() {

        }
    };

    private NavigateUpdateListener mNavigateUpdateListener = new NavigateUpdateListener() {
        @Override
        public void onNavigateUpdate(NaviInfo naviInfo) {

        }
    };

    private List<LatLng> mPoints = new ArrayList<>();

    /**
     * 开始模拟导航
     */
    public void startSimulateNavi(RouteBean bean) {
        stopSimulateNavi();

        mNavigateManager.setAStarPath(bean.getRoutes());
        List<PartInfo> partInfos = mNavigateManager.getPartInfos();
        handleNodeInfo(partInfos);

        if (mPoints.isEmpty()) {
            addOrUpdateLocationMark(null);
        } else {
            addOrUpdateLocationMark(mPoints.get(0));
        }

        mHandler.sendEmptyMessage(MSG_SIMULATE_NAVI);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SIMULATE_NAVI) {
                start();
            }
        }
    };

    public static final int MSG_SIMULATE_NAVI = 1000;

    private int mIndex = 0;
    private double mPreBearing = 0;
    private static final int ZOOM_NAVI = 18;

    private void start() {

        if (mIndex < (mNodeInfos.size() - 1)) {

            NodeInfo nodeInfo = mNodeInfos.get(mIndex);
            NodeInfo nextNodeInfo = mNodeInfos.get(mIndex + 1);

            LatLng from = new LatLng(nodeInfo.getLngLat().getLat(), nodeInfo.getLngLat().getLng());
            LatLng to = new LatLng(nextNodeInfo.getLngLat().getLat(), nextNodeInfo.getLngLat().getLng());

            double bearing = CoordinateUtils.bearing(Position.fromCoordinates(from.getLongitude(), from.getLatitude()
            ), Position.fromCoordinates(to.getLongitude(), to.getLatitude()));

            Log.d(TAG, "start: bearing " + bearing + " preBearing " + mPreBearing + " Math.abs(bearing - mPreBearing)" +
                    " " + Math.abs(bearing - mPreBearing));

            if (Math.abs(bearing - mPreBearing) > 10) {
                if (mValueAnimator != null) {
                    mValueAnimator.cancel();
                    mValueAnimator.end();
                }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(from).zoom(ZOOM_NAVI).bearing
                        (bearing)
                        .build();
                mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), TIMES);
                mPreBearing = bearing;
            } else {
                animateLocation(from, to);
                mIndex++;
            }

            mHandler.sendEmptyMessageDelayed(MSG_SIMULATE_NAVI, TIMES);
        }
    }

    private List<NodeInfo> mNodeInfos;

    private void handleNodeInfo(List<PartInfo> partInfos) {

        mNodeInfos = NavigateFactory.makeMockPointArray(5, partInfos);
        if (!mPoints.isEmpty()) {
            mPoints.clear();
        }

        for (NodeInfo nodeInfo : mNodeInfos) {
            LatLng latLng = new LatLng(nodeInfo.getLngLat().getLat(), nodeInfo.getLngLat().getLng());
            mPoints.add(latLng);
        }
    }

    private LatlngEvaluator mLatlngEvaluator = new LatlngEvaluator();

    /**
     * 直线移动图标
     *
     * @param startLatLng
     * @param endLatLng
     */
    private void animateLocation(final LatLng startLatLng, final LatLng endLatLng) {

        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }

        mValueAnimator = ValueAnimator.ofFloat(0, 1f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d(TAG, "onAnimationUpdate: ");
                LatLng evaluatelatlng = mLatlngEvaluator.evaluate(valueAnimator.getAnimatedFraction(), startLatLng,
                        endLatLng);
                addOrUpdateLocationMark(evaluatelatlng);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(evaluatelatlng).build();
                mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), TIMES);
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(TIMES);
        mValueAnimator.start();
    }

    public static final int TIMES = 1000;

    public static final String SOURCEID_LOCATION = "source-id-location";
    public static final String LAYERID_LOCATION = "layerid-location";
    public static final String IMAGENAE_LOCATION = "imagename-location";

    private void addOrUpdateLocationMark(LatLng latLng) {
        mMapBoxImp.drawImage(latLng, SOURCEID_LOCATION, LAYERID_LOCATION, RouteManager.LAYERID__ROUTE,
                IMAGENAE_LOCATION, Property.ICON_ANCHOR_CENTER);
    }

    private void stopSimulateNavi() {
        mIndex = 0;
        mHandler.removeCallbacksAndMessages(null);
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }



}
