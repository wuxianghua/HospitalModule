package com.palmap.huayitonglib.navi.showroute;

import android.animation.TypeEvaluator;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by yibo.liu on 2017/12/21 17:23.
 */

public class LatlngEvaluator implements TypeEvaluator<LatLng> {

    private LatLng latLng = new LatLng();

    @Override
    public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
        latLng.setLatitude(startValue.getLatitude()
                + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
        latLng.setLongitude(startValue.getLongitude()
                + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
        return latLng;
    }
}
