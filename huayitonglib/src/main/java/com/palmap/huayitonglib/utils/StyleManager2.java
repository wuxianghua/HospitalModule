package com.palmap.huayitonglib.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonPrimitive;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yibo.liu on 2017/10/24 11:06.
 */

public class StyleManager2 {
    public static final String TAG = StyleManager2.class.getSimpleName();

    public static final String NAME_CATOGORY = "category";
    public static final String NAME_DISPLAY = "display";

    public static final String NAME_COLOR = "huaxicolor";
    public static final String NAME_BORDER_COLOR = "huaxi_border_color";
    public static final String NAME_TEXT_SIZE = "huaxi_textsize";
    public static final String NAME_TEXT_COLOR = "huaxi_textcolor";
    public static final String NAME_HEIGHT = "huaxi_height";
    public static final String DEFAULT_HEIGHT = "DEFAULT_HEIGHT";


    public Position mPosition;

    public List<Integer> mIdList = new ArrayList<>();

    public StyleManager2() {

    }

    public StyleManager2(int... poiIds) {
        for (int i = 0; i < poiIds.length; i++) {
            mIdList.add(poiIds[i]);
        }
    }

    public void attach(FeatureCollection featureCollection) {
        long currentTime = 0;
        long before = System.currentTimeMillis();

        if (featureCollection == null) {
            return;
        }

        List<Feature> features = featureCollection.getFeatures();
        Log.i("TAG-Z", "addAreaLayer: ------------ features:" + (System.currentTimeMillis() - currentTime));
        currentTime = System.currentTimeMillis();
        for (Feature feature : features) {

            feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.DEFAULT_COLOR));
            feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.DEFAULT_COLOR_BORDER));
            feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.DEFAULT_COLOR_TEXT));
            feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.DEFAULT_TEXTSIZE));
            feature.addProperty(NAME_HEIGHT, new JsonPrimitive(HuaxiMapStyle.DEFAULT_TEXTSIZE));
            feature.addProperty(DEFAULT_HEIGHT, new JsonPrimitive(HuaxiMapStyle.DEFAULT_HEIGHT));
            if (feature.hasProperty(NAME_CATOGORY)) {
                int category = feature.getNumberProperty(NAME_CATOGORY).intValue();

                Log.d(TAG, "attach: " + feature.toJson());

                switch (category) {
                    case Category.ROAD:
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD_BORDER));
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD_TEXT));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_ROAD));
                        break;
                    case Category.ROAD_SMALL:
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD_BORDER));
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD_TEXT));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_ROAD_SMALL));
                        break;
                    case Category.GREENLAND:
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_GREENLAND));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_GREENLAND_BORDER));
                        break;
                    case Category.HOSPITAL:
                    case Category.BUILDING:
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_BUILDING_TEXT));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_BUILDING));
                        feature.addProperty(DEFAULT_HEIGHT, new JsonPrimitive(50f));
                        break;
                    default:
                        break;
                }

                if (category >= 21000000 && category <= 21051000) {
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_DEPARTMENT));
                    feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_DEPARTMENT_BORDER));
                    feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_DEPARTMENT));
                    feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_DEPARTMENT_TEXT));
                }

                if (feature.hasProperty(NAME_DISPLAY)) {
                    String display = feature.getStringProperty(NAME_DISPLAY);
                    if (display.contains("住院")) {
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_INPATIENTBUILDING));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle
                                .COLOR_INPATIENTBUILDING_BORDER));
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle
                                .COLOR_INPATIENTBUILDING_TEXT));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle
                                .TEXTSIZE_INPATIENTBUILDING));
                    }

//                    门诊楼，急诊科，信息楼，感染性疾病中心，健康检查中心
                    else if (display.contains("门诊") || display.contains("急诊科") || display.contains("信息楼") || display
                            .contains("感染性疾病中心") || display.contains("健康检查中心")) {
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_DEPARTMENT));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle
                                .COLOR_DEPARTMENT_BORDER));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_DEPARTMENT));
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_DEPARTMENT_TEXT));
                    } else if (display.contains("教学楼") || display.contains("水塔楼")) {
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_OTHERS));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle
                                .COLOR_OTHERS_BORDER));
                        feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(HuaxiMapStyle.TEXTSIZE_OTHERS));
                        feature.addProperty(NAME_TEXT_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_OTHERS_TEXT));
                    }

                }
            } else {
                if (feature.hasProperty(NAME_DISPLAY)) {
                    String display = feature.getStringProperty(NAME_DISPLAY);
                    if (TextUtils.isEmpty(display)) {
                        feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_NONAME));
                        feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_NONAME_BORDER));
                    }
                } else {
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_NONAME));
                    feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_NONAME_BORDER));
                }
            }

            if (mPosition == null && TextUtils.equals("Polygon", feature.getGeometry().getType())) {
                Polygon polygon = ((Polygon) feature.getGeometry());
                mPosition = polygon.getCoordinates().get(0).get(0);
            }

            if (!mIdList.isEmpty() && mIdList.contains(Integer.valueOf(feature.getId()))) {
                feature.addProperty(NAME_COLOR, new JsonPrimitive("#DC143C"));
                feature.addProperty(NAME_TEXT_SIZE, new JsonPrimitive(12f));
                if (TextUtils.equals("Polygon", feature.getGeometry().getType())) {
                    Polygon polygon = ((Polygon) feature.getGeometry());
                    mPosition = polygon.getCoordinates().get(0).get(0);

                    if (feature.hasProperty("display")) {
                        String name = feature.getStringProperty("display");
                        mName = name;
                    }

                    int size = 0;
                    for (List<Position> positions : polygon.getCoordinates()) {
                        for (Position position : positions) {
                            calcCenterLatLng(position);
                            size++;
                        }
                    }

                    double lng = mLngSum / size;
                    double lat = mLatSum / size;

                    mLatLng = new LatLng(lat, lng);
                }
            }

            //去掉路上的文字表框
            String id = feature.getId();
            if (TextUtils.equals(id, "2571255") ||TextUtils.equals(id, "2571254")||TextUtils.equals(id, "2571253")
                    ||TextUtils.equals(id, "2571252")||TextUtils.equals(id, "2571235")||TextUtils.equals(id, "2571234")
                    ||TextUtils.equals(id, "2571233")||TextUtils.equals(id, "2571229")||TextUtils.equals(id, "2571228")
                    ||TextUtils.equals(id, "2571227")||TextUtils.equals(id, "2571226")||TextUtils.equals(id, "2571219")
                    ||TextUtils.equals(id, "2571218")||TextUtils.equals(id, "2571216")||TextUtils.equals(id, "2571213")
                    ||TextUtils.equals(id, "2571214")||TextUtils.equals(id, "2571215")||TextUtils.equals(id, "2571220")
                    ||TextUtils.equals(id, "2571210")||TextUtils.equals(id, "2571262")||TextUtils.equals(id, "2571263")
                    ||TextUtils.equals(id, "2571264")||TextUtils.equals(id, "2571223")||TextUtils.equals(id, "2571224")
                    ||TextUtils.equals(id, "2571225")||TextUtils.equals(id, "2571256")||TextUtils.equals(id, "2571257")
                    ||TextUtils.equals(id, "2571257")||TextUtils.equals(id, "2571258")||TextUtils.equals(id, "2571260")
                    ||TextUtils.equals(id, "2571217")||TextUtils.equals(id, "2571211")||TextUtils.equals(id, "2571261")
                    ||TextUtils.equals(id, "2571259")
                    ) {
                feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_ROAD_CONTAINS_TEXT_BORDER));
            }
        }

        Log.i("TAG-Z", "addAreaLayer: ------------ features:" + (System.currentTimeMillis() - currentTime));
        currentTime = System.currentTimeMillis();
        long after = System.currentTimeMillis();

        Log.d(TAG, "attach: 耗时 " + ((after - before) / 1000) + " 秒");
    }

    private double mLatSum = 0;
    private double mLngSum = 0;
    private LatLng mLatLng;
    private String mName;

    private void calcCenterLatLng(Position position) {
        mLatSum += position.getLatitude();
        mLngSum += position.getLongitude();
    }

    public LatLng getLatlng() {
        return mLatLng;
    }

    public Position getPosition() {
        return mPosition;
    }

    public String getName() {
        return mName;
    }
}