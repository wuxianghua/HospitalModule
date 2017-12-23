package com.palmap.huayitonglib.navi.showroute;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by yibo.liu on 2017/12/23 11:17.
 */

public interface INavi {

    void init(Context context, MapboxMap mapboxMap, int resId);

    void setSimulateStateListener(SimulateNaviStateListener listener);

    void startSimulateNavi(RouteBean bean);

    void pauseSimulateNavi();

    void stopSimulateNavi();
}
