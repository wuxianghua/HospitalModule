package com.palmap.huayitonglib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.services.commons.geojson.Feature;
import com.palmap.huayitonglib.R;


import java.util.ArrayList;
import java.util.List;

import static com.palmap.huayitonglib.utils.MapConfig2.LAYERID_BACKGROUND;


/**
 * Created by yunyun.zhou on 2017/11/17 16:46.
 */

public class MapUtils {
    //-------------------------------地图数值-----------------------------
    public static final String TAG = MapUtils.class.getSimpleName();

    //通过feature获取category，当值为-1的时候是err
    public static int getFeature2Category(Feature feature) {
        int category = -1;
        try {
            if (feature != null && feature.hasProperty("category")) {
                category = feature.getNumberProperty("category").intValue();
            }
        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return -1;
        }
        return category;
    }

    //通过feature获取Shape_level，当值为""的时候是err
    public static String getFeature2Shape_level(Feature feature) {
        String shape_level = "";
        try {
            if (feature != null && feature.hasProperty("shape_level")) {
                shape_level = feature.getStringProperty("shape_level");
            }
        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return "";
        }
        return shape_level;
    }

    //通过feature获取最高等级的poiId,唯一标识
    public static String getPoin2PoiIdMax(MapboxMap mapboxMap, LatLng point) {
        try {
            PointF pointF = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);
            return queryMaxFeature(features).getId();
        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return "";
        }
    }

    //通过feature获取最低等级的poiId,唯一标识
    public static String getPoin2PoiIdMin(MapboxMap mapboxMap, LatLng point) {
        try {
            PointF pointF = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);
            return queryMinFeature(features).getId();
        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return "";
        }
    }


    //通过Poin获取最上层的category，当值为-1的时候是err
    public static int getPoin2CategoryMax(LatLng point, MapboxMap mapboxMap) {
        try {
            PointF pointF = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);
            Feature feature = queryMaxFeature(features);
            if (feature != null && feature.hasProperty("category")) {
                int category = feature.getNumberProperty("category").intValue();
                return category;
            }

        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return -1;
        }
        return -1;
    }

    //通过Poin获取category第一最后，当值为-1的时候是err
    public static int getPoin2CategoryMin(LatLng point, MapboxMap mapboxMap) {
        try {
            PointF pointF = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);
            List<Integer> category_arr = new ArrayList<>();
            Feature feature = queryMinFeature(features);
            if (feature != null && feature.hasProperty("category")) {
                int category = feature.getNumberProperty("category").intValue();
                return category;
            }


        } catch (Exception e) {
            Log.e("MapUtils", "getCategory:---------- " + e);
            return -1;
        }
        return -1;
    }

    /**
     * 获取最低shape_level的feature
     *
     * @param features
     * @return
     */
    private static Feature queryMinFeature(List<Feature> features) {
        Feature selectfeature = null;
        int selecetLevel = -1;

        Log.d(TAG, "queryMinFeature: " + selecetLevel + "  size : " + features.size());
        if (features == null) {
            return null;
        } else if (features.isEmpty()) {
            return null;
        } else if (features.size() == 1) {
            selectfeature = features.get(0);
        } else {
            for (Feature feature : features) {
                if (selectfeature == null) {
                    selectfeature = feature;
                    if (feature.hasProperty("shape_level")) {
                        selecetLevel = feature.getNumberProperty("shape_level").intValue();
                    }
                } else {
                    int level = -1;
                    if (feature.hasProperty("shape_level")) {
                        level = feature.getNumberProperty("shape_level").intValue();
                    }
                    if (selecetLevel >= level) {
                        selectfeature = feature;
                        selecetLevel = level;
                    }
                }
            }
        }
        return selectfeature;
    }

    /**
     * 获取最高shape_level的feature
     *
     * @param features
     * @return
     */
    public static Feature queryMaxFeature(List<Feature> features) {

        Feature selectfeature = null;
        int selecetLevel = -1;

        Log.d(TAG, "queryMaxFeature: " + selecetLevel + "  size : " + features.size());
        if (features == null) {
            return null;
        } else if (features.isEmpty()) {
            return null;
        } else if (features.size() == 1) {
            selectfeature = features.get(0);
        } else {
            for (Feature feature : features) {
                if (selectfeature == null) {
                    selectfeature = feature;
                    if (feature.hasProperty("shape_level")) {
                        selecetLevel = feature.getNumberProperty("shape_level").intValue();
                    }
                } else {
                    int level = -1;
                    if (feature.hasProperty("shape_level")) {
                        level = feature.getNumberProperty("shape_level").intValue();
                    }
                    if (selecetLevel <= level) {
                        selectfeature = feature;
                        selecetLevel = level;
                    }
                }
            }
        }
        return selectfeature;
    }

    //--------------获取geojson load方式需要注意
    public static String getGeoJson(Context context, String fileName) {
        String geojson = FileUtils.loadFromAssets(context, fileName);
//        String geojson = FileUtils.loadFromSD(this, fileName);
        return geojson;
    }

    //--------------------------地图界面-----------------------------------
    public static void removeAllOriginalLayers(MapboxMap mMapboxMap) {
        for (Layer layer : mMapboxMap.getLayers()) {
            mMapboxMap.removeLayer(layer);
        }
    }

    //------------------切换楼层 把除backgroundlayer之外的layer全部清除-------------------------------------------
    public static void removeAllWithOutBackgroundlayer(MapboxMap mMapboxMap) {
        for (Layer layer : mMapboxMap.getLayers()) {
            if (!TextUtils.equals(layer.getId(), LAYERID_BACKGROUND)) {
                mMapboxMap.removeLayer(layer);
            }
        }
    }

    /**
     * 设置地图的设备图标
     */
    public static void setAllMapIcon(Context context, MapboxMap mapboxMap, Feature feature) {
        int category = 0;
        if (feature != null && feature.hasProperty("category")) {
            category = feature.getNumberProperty("category").intValue();
        }
        if (category == 23059000 || category == 23025000 || category == 23063000 || category ==
                23024000) { // 洗手间
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.cesuo, mapboxMap);
        } else if (category == 24093000) { // 扶梯
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.shoufudianti, mapboxMap);
        } else if (category == 24091000) { //电梯
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.diantitubiao, mapboxMap);
        } else if (category == 24097000 || category == 24098000) { // 楼梯
//                        addLocalIcon(feature.getStringProperty("logo"), R.mipmap.louti);
        } else if (category == 23043000) { // 大门
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.damen, mapboxMap);
        } else if (category == 23018000) { // 综合服务
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.gonggongfuwu, mapboxMap);
        } else if (category == 23041000) { // 出入口
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.churiukoutubiao, mapboxMap);
        }else if (category == 15001000){
            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.churiukoutubiao, mapboxMap);
        }
    }

    /**
     * 设置地图卫生间设备图标
     */
    public static void setRestRoomIcon(Context context, MapboxMap mapboxMap, Feature feature) {
        int category = 0;
//        if (feature != null && feature.hasProperty("category")) {
//            category = feature.getNumberProperty("category").intValue();
//        }
//        if (category == 23059000 || category == 23025000 || category == 23063000 || category ==
//                23024000) {
//            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.cesuodatu, mapboxMap);
//        }
    }

    /**
     * 设置地图扶梯设备图标
     */
    public static void setEscalatorIcon(Context context, MapboxMap mapboxMap, Feature feature) {
//        int category = 0;
//        if (feature != null && feature.hasProperty("category")) {
//            category = feature.getNumberProperty("category").intValue();
//        }
//        if (category == 24093000) {
//            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.shoufudiantidatu, mapboxMap);
//        }
    }

    /**
     * 设置地图电梯图标
     */
    public static void setElevatorIcon(Context context, MapboxMap mapboxMap, Feature feature) {
//        int category = 0;
//        if (feature != null && feature.hasProperty("category")) {
//            category = feature.getNumberProperty("category").intValue();
//        }
//        if (category == 24091000) {
//            addLocalIcon(context, feature.getStringProperty("logo"), R.mipmap.diantidatu, mapboxMap);
//        }
    }

    //加载本地图标-----
    public static void addLocalIcon(Context context, String logoUrl, int imgPath, MapboxMap mapboxMap) {
        Log.i(TAG, "3: ");
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgPath);
        mapboxMap.addImage(logoUrl, bitmap);
        if (mapboxMap.getLayer(Config.LAYERID_FACILITY) != null) {
            Log.i(TAG, "i: ");
            Layer layer = mapboxMap.getLayer(Config.LAYERID_FACILITY);
            layer.setProperties(
                    PropertyFactory.iconImage(Function.property("logo", Stops.<String>identity())),
                    PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM)
            );
        }
    }

    //------------------地图放大功能----------------------
    public static void setMapExtend(MapboxMap mapboxMap) {
        double zoom = mapboxMap.getCameraPosition().zoom;
        Log.d(TAG, "onClick: ZOOM-" + zoom);
        if (zoom <= mapboxMap.getMaxZoomLevel()) {
            zoom += 1;
            mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    //------------------地图缩小功能----------------------
    public static void setMapSmall(MapboxMap mapboxMap) {
        double zoom = mapboxMap.getCameraPosition().zoom;
        Log.d(TAG, "onClick: ZOOM-" + zoom);
        if (zoom >= mapboxMap.getMinZoomLevel()) {
            zoom -= 1;
            mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    /**
     * 根据经纬度获取名字
     * 当点击的时候才有效
     *
     * @param mapboxMap
     * @param latLng
     */
    public static String getName(MapboxMap mapboxMap, LatLng latLng) {

        PointF pointF = mapboxMap.getProjection().toScreenLocation(latLng);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);

        if (features != null && !features.isEmpty()) {
            return getName(queryMaxFeature(features));
        }
        Log.d(TAG, "getNamegetName: 获取不到名字");
        return "";
    }

    /**
     * 根据feature获取名字
     *
     * @param feature
     * @return
     */
    public static String getName(Feature feature) {
        if (feature != null && feature.hasProperty("display")) {
            String display = feature.getStringProperty("display");
            Log.d(TAG, "getName: 获取到的名字 " + display);
            return display;
        }
        Log.d(TAG, "getName: 获取不到名字 ");
        return "";
    }
}
