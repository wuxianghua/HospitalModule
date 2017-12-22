package com.palmap.huayitonglib.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.IdentityStops;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.BackgroundLayer;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.palmap.huayitonglib.adapter.PulseMarkerViewAdapter;
import com.palmap.huayitonglib.bean.FloorBean;

import java.util.Arrays;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;

/**
 * Created by yunyun.zhou on 2017/11/17 17:58.
 */

public class MapInitUtils {
    public static void initMapSetting(Context context, MapboxMap mMapboxMap) {
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Config.CENTER_LAT, Config.CENTER_LNG),
                15));
        mMapboxMap.getUiSettings().setLogoEnabled(false);
        mMapboxMap.getUiSettings().setAttributionEnabled(false);
        // 指南针位置
        mMapboxMap.getUiSettings().setCompassMargins(0, DisplayUtil.dip2px(context, 110), DisplayUtil.dip2px(context, 20), 0);
        //切换指南针的图片
//        mMapboxMap.getUiSettings().setCompassImage(context.getResources().getDrawable(R.drawable.zhinanzhen));
        mMapboxMap.setMaxZoomPreference(25.5);
        mMapboxMap.setMinZoomPreference(18);
        mMapboxMap.getMarkerViewManager().addMarkerViewAdapter(new PulseMarkerViewAdapter((Activity) context));
    }

    public static void addBackgroudLayer(Context context, MapboxMap mMapboxMap) {
        Log.i("addBackgroudLayer", "addBackgroudLayer: ");
        if (mMapboxMap.getLayer(Config.LAYERID_BACKGROUND) != null) {
            mMapboxMap.removeLayer(Config.LAYERID_BACKGROUND);
        }
        BackgroundLayer backgroundLayer = new BackgroundLayer(Config.LAYERID_BACKGROUND);
        backgroundLayer.setProperties(PropertyFactory.backgroundColor(HuaxiMapStyle.COLOR_BACKGROUND));
        mMapboxMap.addLayer(backgroundLayer);
    }

    //楼层概念----- floorParameter = 15代表平面层
    public static void addFrameLayer(Context context, MapboxMap mMapboxMap, FloorBean mFloorBean, int floorParameter) {
        Log.i("MapInitUtils", "addFrameLayer:------------- ");
        String sourceId = mFloorBean.getFrameFilename();
        Log.i("MapInitUtils", "addFrameMapSource:------------- 22sourceId:" + sourceId);
        String fileName = mFloorBean.getFrameFilename();
        Log.i("MapInitUtils", "addFrameMapSource:------------- 33:" + fileName);
        addFrameMapSource(context, mMapboxMap, sourceId, fileName, floorParameter);
        Log.i("MapInitUtils", "addFrameMapSource:------------- ");
        //frame的面部去除
        if (mMapboxMap.getLayer(Config.LAYERID_FRAME) != null) {
            mMapboxMap.removeLayer(Config.LAYERID_FRAME);
        }
        Log.i("MapInitUtils", "removeLayer:------------- ");
        FillLayer mFrameLayer = new FillLayer(Config.LAYERID_FRAME, sourceId);
        mFrameLayer.setProperties(PropertyFactory.fillColor(HuaxiMapStyle.COLOR_FRAME));
        mMapboxMap.addLayer(mFrameLayer);
        Log.i("MapInitUtils", "addLayer :------------- ");
        //frame的线去除
        //frame的面部去除
//        if (mMapboxMap.getLayer(Config.LAYERID_FRAME_LINE) != null) {
//            mMapboxMap.removeLayer(Config.LAYERID_FRAME_LINE);
//        }
//        LineLayer mFrameLineLayer = new LineLayer(Config.LAYERID_FRAME_LINE, sourceId);
//        mFrameLineLayer.setProperties(PropertyFactory.fillColor(HuaxiMapStyle.FRAME_COLOR), PropertyFactory
// .lineWidth(0.2f));
//        mMapboxMap.addLayer(mFrameLineLayer);

    }

    public static void addFrameMapSource(Context context, MapboxMap mMapboxMap, String mSouceId, String fileName, int
            floorParameter) {

        String geojson = MapUtils.getGeoJson(context, fileName);
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
//        // 计算中心点
//        if (mSouceId.contains("frame")) {
//            Log.i("addFrameMapSource", "addFrameMapSource: ------------1");
//            //平均值获取中心点------
//            LatLng latlng = MapBoxUtils.getCenterPoint(featureCollection);
//            if (latlng != null) {
//                setUpCamera(mMapboxMap, floorParameter, latlng.getLatitude(), latlng.getLongitude());
//            }
//        }
//      121.386708605    31.226606342
//       二楼 31.22676432880897 121.38657903201154
//     三楼   31.22660050273095 121.38668204541842
        if (mSouceId.contains("F1")) {
            Log.i("addFrameMapSource", "addFrameMapSource: ------------1");
            setUpCamera(mMapboxMap, "F1", 31.226606342, 121.386708605);
        } else if (mSouceId.contains("F2")) {
            setUpCamera(mMapboxMap, "F2", 31.22676432880897, 121.38657903201154);
            Log.i("addFrameMapSource", "addFrameMapSource: ------------2");
        } else if (mSouceId.contains("F3")) {
            setUpCamera(mMapboxMap, "F2", 31.22660050273095, 121.38668204541842);
            Log.i("addFrameMapSource", "addFrameMapSource: ------------3");
        } else if (mSouceId.contains("F4")) {
            setUpCamera(mMapboxMap, "F4", 31.2271836, 121.3867058);
        }
        Log.i("addFrameMapSource", "addFrameMapSource:------- fileName：" + fileName);
        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);
        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
        Log.i("MapInitUtils", "addLayer :-------------6666 ");
    }

//    public static void addAreaLayer(Context context, MapboxMap mapboxMap, FloorBean mFloorBean, StyleManager2
// mStyleManager2 ,MapStyleManager2 styleManager) {
//        Log.i("addAreaLayer", "addAreaLayer: ");
//        String sourceId = mFloorBean.getAreaFilename();
//        String fileName = mFloorBean.getAreaFilename();
//        addMapSource(context, mapboxMap, mStyleManager2, sourceId, fileName,styleManager);
//        if (mapboxMap.getLayer(Config.LAYERID_AREA) != null) {
//            mapboxMap.removeLayer(Config.LAYERID_AREA);
//        }
//        //3D
//        FillExtrusionLayer arealayer = new FillExtrusionLayer(Config.LAYERID_AREA, sourceId);
//
//        arealayer.setProperties(PropertyFactory.fillExtrusionColor(Function.property("color", new
// IdentityStops<String>())), PropertyFactory.fillExtrusionHeight(0.2f));
//        mapboxMap.addLayer(arealayer);
//
//        if (mapboxMap.getLayer(Config.LAYERID_AREA_LINE) != null) {
//            mapboxMap.removeLayer(Config.LAYERID_AREA_LINE);
//        }
//        LineLayer areaLineLayer = new LineLayer(Config.LAYERID_AREA_LINE, sourceId);
//        areaLineLayer.setProperties(PropertyFactory.lineColor(Function.property(StyleManager2.NAME_BORDER_COLOR,
// new IdentityStops<String>())), PropertyFactory.fillExtrusionHeight(0.2f));
//        //-----3D-----(3D的时候线要放在面上面才能显示出来)
//        mapboxMap.addLayerAbove(areaLineLayer, Config.LAYERID_AREA);
////                mMapboxMap.addLayer(areaLineLayer);
//        if (mapboxMap.getLayer(Config.LAYERID_AREA_TEXT) != null) {
//            mapboxMap.removeLayer(Config.LAYERID_AREA_TEXT);
//        }
//        SymbolLayer areatextlayer = new SymbolLayer(Config.LAYERID_AREA_TEXT, sourceId);
//        areatextlayer.setProperties(PropertyFactory.textField("{display}"), PropertyFactory.textColor(Function
// .property(StyleManager2.NAME_TEXT_COLOR, new IdentityStops<String>())), PropertyFactory.textSize(Function.property
// (StyleManager2.NAME_TEXT_SIZE, new IdentityStops<Float>())), PropertyFactory.fillExtrusionHeight(20f));
//        mapboxMap.addLayerAbove(areatextlayer, Config.LAYERID_AREA_LINE);
////      mMapboxMap.addLayer(areatextlayer);
//    }


    //-----------json-area

    public static void addAreaLayer(Context context, MapboxMap mapboxMap, FloorBean mFloorBean, StyleManager2
            mStyleManager2, MapStyleManager2 styleManager) {
        Log.i("addAreaLayer", "addAreaLayer: ");
        String sourceId = mFloorBean.getAreaFilename();
        String fileName = mFloorBean.getAreaFilename();
        addMapSource(context, mapboxMap, mStyleManager2, sourceId, fileName, styleManager);
        if (mapboxMap.getLayer(Config.LAYERID_AREA) != null) {
            mapboxMap.removeLayer(Config.LAYERID_AREA);
        }
//        //3D
//        FillExtrusionLayer arealayer = new FillExtrusionLayer(Config.LAYERID_AREA, sourceId);
//
//        arealayer.setProperties(PropertyFactory.fillExtrusionColor(Function.property("color", new
// IdentityStops<String>())), PropertyFactory.fillExtrusionHeight(0.2f));


        //Area 区域覆盖颜色
        FillExtrusionLayer arealayer = new FillExtrusionLayer(Config.LAYERID_AREA, sourceId);
        arealayer.setProperties(
                PropertyFactory.fillExtrusionColor(Function.property("color", new IdentityStops<String>())),
//                PropertyFactory.fillOpacity(Function.property("opacity", new IdentityStops<Float>())), // 透明度
                PropertyFactory.fillExtrusionOpacity(0.5f), // 透明度
                fillExtrusionHeight(Function.property("3DHeight", new IdentityStops<Float>())),
//                        fillExtrusionHeight() // 高度拉伸
//                        fillExtrusionBase() // 阴影
//                        fillExtrusionColor() // 颜色
//                        fillExtrusionOpacity() //不透明度
//                        fillExtrusionPattern() //模式;   花样，样品;   图案;   榜样，典范;
//                        fillExtrusionTranslate() //转化
//                        fillExtrusionTranslateAnchor()
                PropertyFactory.fillExtrusionBase(0.0f) // 阴影大小
        );
        arealayer.setFillExtrusionBaseTransition(new TransitionOptions(10000,10000));
        mapboxMap.addLayer(arealayer);

        if (mapboxMap.getLayer(Config.LAYERID_AREA_LINE) != null) {
            mapboxMap.removeLayer(Config.LAYERID_AREA_LINE);
        }
//        LineLayer areaLineLayer = new LineLayer(Config.LAYERID_AREA_LINE, sourceId);
////        areaLineLayer.setProperties(PropertyFactory.lineColor(Function.property(StyleManager2.NAME_BORDER_COLOR,
/// new IdentityStops<String>())), fillExtrusionHeight(0.2f));
//
//        areaLineLayer.setProperties(
//                PropertyFactory.lineOpacity(.1f),
//                PropertyFactory.lineColor(Function.property("outLineColor", new IdentityStops<String>()))
////                        PropertyFactory.lineBlur()// 模糊
////                        PropertyFactory.lineCap()
////                        PropertyFactory.lineRoundLimit()
//        );
//
//        //-----3D-----(3D的时候线要放在面上面才能显示出来)
//        mapboxMap.addLayerAbove(areaLineLayer, Config.LAYERID_AREA);


        LineLayer areaLineLayer = new LineLayer(Config.LAYERID_AREA_LINE, sourceId);
        areaLineLayer.setProperties(
                PropertyFactory.lineWidth(0.5f),
                PropertyFactory.lineColor(Function.property(StyleManager2.NAME_BORDER_COLOR, new
                        IdentityStops<String>())),
//                PropertyFactory.lineColor(Function.property("outLineColor", new IdentityStops<String>()))
                PropertyFactory.fillExtrusionHeight(0.2f)
        );
        //-----3D-----(3D的时候线要放在面上面才能显示出来)
        // TODO 现在不要线了
//        mapboxMap.addLayerAbove(areaLineLayer, Config.LAYERID_AREA);


//                mMapboxMap.addLayer(areaLineLayer);
        if (mapboxMap.getLayer(Config.LAYERID_AREA_TEXT) != null) {
            mapboxMap.removeLayer(Config.LAYERID_AREA_TEXT);
        }
//        SymbolLayer areatextlayer = new SymbolLayer(Config.LAYERID_AREA_TEXT, sourceId);
//        areatextlayer.setProperties(PropertyFactory.textField("{display}"), PropertyFactory.textColor(Function
// .property(StyleManager2.NAME_TEXT_COLOR, new IdentityStops<String>())), PropertyFactory.textSize(Function.property
// (StyleManager2.NAME_TEXT_SIZE, new IdentityStops<Float>())), fillExtrusionHeight(20f));
//        mapboxMap.addLayerAbove(areatextlayer, Config.LAYERID_AREA_LINE);
        //Area_Text
        SymbolLayer areaTextLayer = new SymbolLayer(Config.LAYERID_AREA_TEXT, sourceId);
        areaTextLayer.setProperties(
                PropertyFactory.textField("{display}"),
                PropertyFactory.textColor("#475266"),
                PropertyFactory.textSize(10.f),
                PropertyFactory.iconSize(.5f),
                PropertyFactory.textAnchor(Property.TEXT_JUSTIFY_LEFT),
                PropertyFactory.iconOffset(new Float[]{-10.f, 0.f}),
                PropertyFactory.iconImage(Function.property("logo", Stops.<String>identity()))

        );
        areaTextLayer.setFilter(Filter.has("display"));
        mapboxMap.addLayer(areaTextLayer);
    }


    public static void initFacilityData(Context context, MapboxMap mMapboxMap, FloorBean mFloorBean) {
        Log.i("initFacilityData", "initFacilityData: ");
        String sourceId = mFloorBean.getFacilityFilename();
        String fileName = mFloorBean.getFacilityFilename();
        addMapFacilitySource(context, mMapboxMap, sourceId, fileName);
        if (mMapboxMap.getLayer(Config.LAYERID_FACILITY) != null) {
            mMapboxMap.removeLayer(Config.LAYERID_FACILITY);
        }
        SymbolLayer facilityLayer = new SymbolLayer(Config.LAYERID_FACILITY, sourceId);
        facilityLayer.setProperties(PropertyFactory.fillExtrusionHeight(1f));
        mMapboxMap.addLayer(facilityLayer);
    }

    public static void addMapSource(Context context, MapboxMap mMapboxMap, StyleManager2 mStyleManager2, String
            mSouceId, String fileName, MapStyleManager2 styleManager) {
        String geojson = MapUtils.getGeoJson(context, fileName);
//        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
//        // 计算中心点
//        if (mSouceId.contains("area")) {
//            //刘艺博的方式
////            mStyleManager2.attach(featureCollection);
//
//            //------json——style的方式
//            styleManager.attachStyle(featureCollection, mSouceId);
//        }
//        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);

        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, geojson);

        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
    }

    public static void addMapFacilitySource(Context context, MapboxMap mMapboxMap, String mSouceId, String fileName) {
        String geojson = MapUtils.getGeoJson(context, fileName);
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);
        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
    }

    //    private void addMapSource(String mSouceId, String fileName) {
//        String geojson = MapUtils.getGeoJson(getBaseContext(), fileName);
//        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
//        // 计算中心点
//        if (mSouceId.contains("frame")) {
//            Log.d(TAG, "addMapSource: isFromOther" + isFromOther + "设置地图中心点");
//            if (isFromOther) {
//                MapInitUtils.setOtherUpCamera(mMapboxMap, mLatitude, mLngtitude, poid);
//                isFromOther = false;
//            } else {
//                //平均值获取中心点------
//                LatLng latlng = MapBoxUtils.getCenterPoint(featureCollection);
//                if (latlng != null) {
//                    MapInitUtils.setUpCamera(mMapboxMap, floorParameter, latlng.getLatitude(), latlng.getLongitude());
//                }
//            }
//        }
//        if (mSouceId.contains("area")) {
//            mStyleManager2.attach(featureCollection);
//        }
//        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);
//        if (mMapboxMap.getSource(mSouceId) == null) {
//            mMapboxMap.addSource(geoJsonSource);
//        }
//    }
    // 设置本界面展示或切换楼层camera(现有数据只针对于华西医院)
    public static void setUpCamera(MapboxMap mMapboxMap, String floorParameter, double latitude, double longitude) {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(latitude - 0.0035, longitude + 0.0035))
                .include(new LatLng(latitude + 0.0035, longitude - 0.0035))
                .build();
        // TODO 设置边界
        mMapboxMap.setLatLngBoundsForCameraTarget(bounds);
        Log.i("MapInitUtils", "setUpCamera: floorParameter" + floorParameter);
        double zoom = 16.5;
        //当楼层的position是3  和4 的时候叫楼层的缩放小一点，别的楼层进行都放比例增大 F0  F1
        if (floorParameter.equals("F3") || floorParameter.equals("F4")) {
            zoom = 16;
        } else if (floorParameter.equals("B1") || floorParameter.equals("B2") || floorParameter.equals("F15")) {  // B1  B2  F15
            zoom = 17;
        } else if (floorParameter.equals("F10") || floorParameter.equals("F11") || floorParameter.equals("F12") || floorParameter.equals("F13") ||
                floorParameter.equals("F14")) { // F10 到 F14
            zoom = 16.8;
        } else {
            zoom = 16.5;
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .tilt(0) // 倾斜角度
                .build();
        // 使用一个动画来调整地图
        mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
//        if (floorParameter.equals("F1")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.2268634617768,
//                    121.38649602857757), 17.3));
//        } else if (floorParameter.equals("F2")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.22676432880897,
//                    121.38657903201154), 18));
//        } else if (floorParameter.equals("F3")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.22660050273095,
//                    121.38668204541842), 18));
//        }

//        if (floorParameter.equals("F1")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//                    longitude), 19));
//        } else if (floorParameter.equals("F2")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//                    longitude), 19));
//        } else if (floorParameter.equals("F3")) {
//            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//                    longitude), 19));
//        }

//        double zoom = mapboxMap.getCameraPosition().zoom;
//        LatLng latLng = mapboxMap.getCameraPosition().target;
        // 因左侧可侧滑，所以中心点向右偏移一点
//        longitude = longitude - 0.00008;
//        if (!floorParameter.equals("F4")){
//            CameraPosition position = new CameraPosition.Builder()
//                    .target(new LatLng(latitude, longitude))
//                    .zoom(19)
//                    .tilt(42) // 倾斜角度
//                    .build();
//            // 使用一个动画来调整地图
//            mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
//        }else {
//            CameraPosition position = new CameraPosition.Builder()
//                    .target(new LatLng(latitude, longitude))
//                    .zoom(19)
//                    .tilt(0) // 倾斜角度
//                    .build();
//            // 使用一个动画来调整地图
//            mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
//        }



    }

    // 从其他地方跳转到地图设置(现有数据只针对于华西医院)
    public static void setOtherUpCamera(MapboxMap mMapboxMap, double latitude, double longitude, int mPoiId) {
        try {
            Log.d("setOtherUpCamera", "setUpCamera: latitude = " + latitude + "+===" + latitude + "++++++++++" +
                    mPoiId);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(new LatLng(latitude - 0.005, longitude + 0.005))
                    .include(new LatLng(latitude + 0.005, longitude - 0.005))
                    .build();
            //设置地图放大比例
            double zoom = 18.5;
            List<Integer> a20 = Arrays.asList(Constant.poiId20);
            List<Integer> a17 = Arrays.asList(Constant.poiId17);
            if (a17.contains(mPoiId)) {
                zoom = 17;
            } else if (a20.contains(mPoiId)) {
                zoom = 20;
            }
            mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
            mMapboxMap.setLatLngBoundsForCameraTarget(bounds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置2D模式地图（旋转地图 + 设置area层高度）
    public static void setUp2DMap(MapboxMap mapboxMap) {
        double zoom = mapboxMap.getCameraPosition().zoom;
        LatLng latLng = mapboxMap.getCameraPosition().target;
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .tilt(0) // 倾斜角度
                .build();
        // 设置area层对应2D高度，此高度对应style中的2DHeight
        Layer layer = mapboxMap.getLayer(Config.LAYERID_AREA);
        layer.setProperties(
                fillExtrusionHeight(Function.property("3DHeight", new IdentityStops<Float>()))
        );
        // 添加线 areaLine
        Layer lineLayer = mapboxMap.getLayer(Config.LAYERID_AREA_LINE);
        if (lineLayer != null) {
            lineLayer.setProperties(PropertyFactory.visibility
                    (Property.NONE));
        }
        // 使用一个动画来调整地图
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    // 设置3D模式地图（旋转地图 + 设置area层高度 + areaLine线条不显示）
    public static void setUp3DMap(MapboxMap mapboxMap) {
        double zoom = mapboxMap.getCameraPosition().zoom;
        LatLng latLng = mapboxMap.getCameraPosition().target;
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .tilt(50) // 倾斜角度
                .build();
        // 设置area层对应3D高度，此高度对应style中的3DHeight
        Layer layer = mapboxMap.getLayer(Config.LAYERID_AREA);
        layer.setProperties(
                fillExtrusionHeight(Function.property("3DHeight", new IdentityStops<Float>()))
        );
        // 去掉线条，3D添加线条后太难看了,此处对线条进行了隐藏
        Layer lineLayer = mapboxMap.getLayer(Config.LAYERID_AREA_LINE);
        if (lineLayer != null) {
            lineLayer.setProperties(PropertyFactory.visibility
                    (Property.NONE));
        }
        // 使用一个动画来调整地图
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
