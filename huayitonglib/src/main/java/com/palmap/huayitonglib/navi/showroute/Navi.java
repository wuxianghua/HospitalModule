package com.palmap.huayitonglib.navi.showroute;

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

    public static final String SOURCEID_LOCATION = "source-id-location";
    public static final String LAYERID_LOCATION = "layerid-location";
    public static final String IMAGENAE_LOCATION = "imagename-location";

    public static final int MSG_SIMULATE_NAVI = 1000;
    public static final int MSG_SIMULATE_NAVI_FINISH_ONE_FLOOR = 1001;

    private static final int ZOOM_NAVI = 18;
    public static final int TIMES = 600;

    private MapBoxImp mMapBoxImp;
    private MapboxMap mMapboxMap;

    private NavigateManager mNavigateManager;

    private ValueAnimator mValueAnimator;
    private long mCurrentFloorId;
    private int mIndex = 0;
    private double mPreBearing = 0;
    private RouteBean mRouteBean;
    private LatlngEvaluator mLatlngEvaluator = new LatlngEvaluator();

    private List<LatLng> mPoints = new ArrayList<>();
    private List<NodeInfo> mUsedNoInfos = new ArrayList<>();
    private List<NodeInfo> mFromNodeInfos = new ArrayList<>();
    private List<NodeInfo> mToNodeInfos = new ArrayList<>();

    private SimulateNaviStateListener mSimulateNaviStateListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SIMULATE_NAVI:
                    repeat();
                    break;
                case MSG_SIMULATE_NAVI_FINISH_ONE_FLOOR:
                    if (mSimulateNaviStateListener != null) {
                        if (mCurrentFloorId != mRouteBean.getToFloorId()) {
                            mSimulateNaviStateListener.onSwitchFloor(mRouteBean.getToFloorId());
                            startInner(mRouteBean.getToFloorId(), mToNodeInfos);
                        } else {
                            mSimulateNaviStateListener.onFinish();
                        }
                    }
                    break;
            }
        }
    };

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
            Log.d(TAG, "onNavigateUpdate:  info " + naviInfo.getNaviTip() + "\t" + (int)naviInfo.getTotalRemainLength());
            if (mSimulateNaviStateListener != null) {
                mSimulateNaviStateListener.onInfo(naviInfo.getNaviTip());
            }
        }
    };

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

    /**
     * 添加监听器
     *
     * @param listener
     */
    public void setSimulateStateListener(SimulateNaviStateListener listener) {
        mSimulateNaviStateListener = listener;
    }

    /**
     * 开始模拟导航
     */
    public void startSimulateNavi(RouteBean bean) {
        mRouteBean = bean;
        if (mRouteBean == null) {
            return;
        }
        if (mSimulateNaviStateListener != null) {
            mSimulateNaviStateListener.onPre(bean.getFromFloorId());
        }

        mNavigateManager.setAStarPath(bean.getRoutes());
        List<PartInfo> partInfos = mNavigateManager.getPartInfos();
        handleNodeInfo(partInfos, bean.getFromFloorId(), bean.getToFloorId());

        if (mPoints.isEmpty()) {
            addOrUpdateLocationMark(null);
        } else {
            addOrUpdateLocationMark(mPoints.get(0));
        }

        if (mSimulateNaviStateListener != null) {
            mSimulateNaviStateListener.onStart(bean.getFromFloorId());
        }

        startInner(mRouteBean.getFromFloorId(), mFromNodeInfos);
    }

    /**
     * 内部开始调用的方法
     *
     * @param floorId
     * @param nodeInfos
     */
    private void startInner(long floorId, List<NodeInfo> nodeInfos) {

        clearSimulateNaviInfo();

        mCurrentFloorId = floorId;
        mUsedNoInfos.addAll(nodeInfos);
        mHandler.sendEmptyMessage(MSG_SIMULATE_NAVI);
    }

    private void repeat() {
        Log.d(TAG, "repeat: mIndex " + mIndex + " mUsedNoInfos.size() " + mUsedNoInfos.size());

        if (mIndex < (mUsedNoInfos.size() - 1)) {

            NodeInfo nodeInfo = mUsedNoInfos.get(mIndex);
            NodeInfo nextNodeInfo = mUsedNoInfos.get(mIndex + 1);

            LatLng from = new LatLng(nodeInfo.getLngLat().getLat(), nodeInfo.getLngLat().getLng());
            LatLng to = new LatLng(nextNodeInfo.getLngLat().getLat(), nextNodeInfo.getLngLat().getLng());

            double bearing = CoordinateUtils.bearing(Position.fromCoordinates(from.getLongitude(), from.getLatitude()
            ), Position.fromCoordinates(to.getLongitude(), to.getLatitude()));

            Log.d(TAG, "start: bearing " + bearing + " preBearing " + mPreBearing + " Math.abs(bearing - mPreBearing)" +
                    " " + Math.abs(bearing - mPreBearing));

            if (Math.abs(bearing - mPreBearing) > 10) {
                Log.d(TAG, "repeat: 转");
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
                Log.d(TAG, "repeat: 不转");
                animateLocation(from, to);
                mNavigateManager.updatePosition(mCurrentFloorId,nodeInfo.getX(),nodeInfo.getY(),0);
                mIndex++;
            }
            mHandler.sendEmptyMessageDelayed(MSG_SIMULATE_NAVI, TIMES);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_SIMULATE_NAVI_FINISH_ONE_FLOOR, TIMES);
        }
    }

    private void handleNodeInfo(List<PartInfo> partInfos, long fromFloorId, long toFloorId) {

        List<NodeInfo> nodeInfos = NavigateFactory.makeMockPointArray(5, partInfos);

        if (!mFromNodeInfos.isEmpty()) {
            mFromNodeInfos.clear();
        }
        if (!mToNodeInfos.isEmpty()) {
            mToNodeInfos.clear();
        }

        for (NodeInfo nodeInfo : nodeInfos) {
            if (nodeInfo.getZ() == fromFloorId) {
                mFromNodeInfos.add(nodeInfo);
            } else if (nodeInfo.getZ() == toFloorId) {
                mToNodeInfos.add(nodeInfo);
            }
        }

        if (!mPoints.isEmpty()) {
            mPoints.clear();
        }

        for (NodeInfo nodeInfo : nodeInfos) {
            LatLng latLng = new LatLng(nodeInfo.getLngLat().getLat(), nodeInfo.getLngLat().getLng());
            mPoints.add(latLng);
        }
    }

    /**
     * 直线移动图标 动画
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
                CameraPosition cameraPosition = new CameraPosition.Builder().target(evaluatelatlng).zoom(ZOOM_NAVI)
                        .build();
                mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), TIMES);
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(TIMES);
        mValueAnimator.start();
    }

    private void addOrUpdateLocationMark(LatLng latLng) {
        mMapBoxImp.drawImage(latLng, SOURCEID_LOCATION, LAYERID_LOCATION, RouteManager.LAYERID__ROUTE,
                IMAGENAE_LOCATION, Property.ICON_ANCHOR_CENTER);
    }

    private void clearLocationMark() {
        mMapBoxImp.drawImage(null, SOURCEID_LOCATION, LAYERID_LOCATION, RouteManager.LAYERID__ROUTE,
                IMAGENAE_LOCATION, Property.ICON_ANCHOR_CENTER);
    }

    private void clearSimulateNaviInfo() {
        if (!mUsedNoInfos.isEmpty()) {
            mUsedNoInfos.clear();
        }
        mIndex = 0;
        mHandler.removeCallbacksAndMessages(null);
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mPreBearing = 0;
    }

    /**
     * 停止模拟导航
     */
    public void stopSimulateNavi() {
        clearSimulateNaviInfo();
        clearLocationMark();
    }
}
