package com.palmap.huayitonglib.utils;

import android.content.Context;
import android.text.TextUtils;
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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.bean.FloorBean;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;

/**
 * Created by GHW
 */

public class GuoMapUtils {

    public static void initMapSetting(Context context, MapboxMap mMapboxMap) {
        mMapboxMap.getUiSettings().setLogoEnabled(false);
        mMapboxMap.getUiSettings().setAttributionEnabled(false);
        // 指南针位置
        // 设置指南针
        mMapboxMap.getUiSettings().setCompassMargins(30, DisplayUtil.dip2px(110), DisplayUtil.getScreenWith(context) - 100, 20);
        // getActivity容易有空指针bug 切换指南针的图片
        mMapboxMap.getUiSettings().setCompassImage(context.getResources().getDrawable(R.mipmap.ic_map_compass));
        mMapboxMap.setMaxZoomPreference(20);
        mMapboxMap.setMinZoomPreference(16);
    }

    /**
     *  设置指南针距离顶部的距离
     * @param marginTop 单位dp
     */
    public static void setCampassMarTop(Context context, MapboxMap mMapboxMap,float marginTop){
        mMapboxMap.getUiSettings().setCompassMargins(30, DisplayUtil.dip2px(marginTop), DisplayUtil.getScreenWith(context) - 100, 20);
    }

    public static void addBackgroudLayer(Context context, MapboxMap mMapboxMap) {
        Log.i("addBackgroudLayer", "addBackgroudLayer: ");
        if (mMapboxMap.getLayer(MapConfig2.LAYERID_BACKGROUND) != null) {
            mMapboxMap.removeLayer(MapConfig2.LAYERID_BACKGROUND);
        }
        BackgroundLayer backgroundLayer = new BackgroundLayer(MapConfig2.LAYERID_BACKGROUND);
        backgroundLayer.setProperties(PropertyFactory.backgroundColor(MapConfig2.COLOR_BACKGROUND));
        mMapboxMap.addLayer(backgroundLayer);
    }

    //楼层概念----- floorParameter = 15代表平面层
    public static void addFrameLayer(Context context, MapboxMap mMapboxMap, FloorBean mFloorBean, String floorParameter) {
        String sourceId = mFloorBean.getFrameFilename();
        String fileName = mFloorBean.getFrameFilename();
        addFrameMapSource(context, mMapboxMap, sourceId, fileName, floorParameter);
        //frame的面部去除
        if (mMapboxMap.getLayer(MapConfig2.LAYERID_FRAME) != null) {
            mMapboxMap.removeLayer(MapConfig2.LAYERID_FRAME);
        }
        Log.i("MapInitUtils", "removeLayer:------------- ");
        FillLayer mFrameLayer = new FillLayer(MapConfig2.LAYERID_FRAME, sourceId);
        mFrameLayer.setProperties(PropertyFactory.fillColor(MapConfig2.COLOR_FRAME));
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

    public static void addFrameMapSource(Context context, MapboxMap mMapboxMap, String mSouceId, String fileName, String
            floorParameter) {

        String geojson = GuoMapUtilsTow.getGeoJson(context, fileName);
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
//        // 计算中心点
        if (mSouceId.contains("frame")) {
            Log.i("addFrameMapSource", "addFrameMapSource: ------------1");
            //平均值获取中心点------
            LatLng latlng = GuoMapUtilsTow.getCenterPoint(featureCollection);
            if (latlng != null) {
                setUpCamera(mMapboxMap, floorParameter, latlng.getLatitude(), latlng.getLongitude());
            }
        }

//        if (mSouceId.contains("F1")) {
//            Log.i("addFrameMapSource", "addFrameMapSource: ------------1");
//            setUpCamera(mMapboxMap, 31.226606342, 121.386708605);
//        } else if (mSouceId.contains("F2")) {
//            setUpCamera(mMapboxMap, 31.22676432880897, 121.38657903201154);
//            Log.i("addFrameMapSource", "addFrameMapSource: ------------2");
//        } else if (mSouceId.contains("F3")) {
//            setUpCamera(mMapboxMap, 31.22660050273095, 121.38668204541842);
//            Log.i("addFrameMapSource", "addFrameMapSource: ------------3");
//        }
        Log.i("addFrameMapSource", "addFrameMapSource:------- fileName：" + fileName);
        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);
        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
        Log.i("MapInitUtils", "addLayer :-------------6666 ");
    }

    //-----------json-area

    public static void addAreaLayer(Context context, MapboxMap mapboxMap, FloorBean mFloorBean) {
        Log.i("addAreaLayer", "addAreaLayer: ");
        String sourceId = mFloorBean.getAreaFilename();
        String fileName = mFloorBean.getAreaFilename();
        addMapSource(context, mapboxMap,sourceId, fileName);
        if (mapboxMap.getLayer(MapConfig2.LAYERID_AREA) != null) {
            mapboxMap.removeLayer(MapConfig2.LAYERID_AREA);
        }

        //Area 区域覆盖颜色
        FillExtrusionLayer arealayer = new FillExtrusionLayer(MapConfig2.LAYERID_AREA, sourceId);
        arealayer.setProperties(
//                PropertyFactory.fillExtrusionColor(Function.property(MapConfig2.NAME_AREA_COLOR, new IdentityStops<String>())),
                PropertyFactory.fillExtrusionColor(Function.property("color", new IdentityStops<String>())),
//                PropertyFactory.fillOpacity(Function.property("opacity", new IdentityStops<Float>())), // 透明度
//                PropertyFactory.fillExtrusionOpacity(0.5f), // 透明度
//                fillExtrusionHeight(Function.property(MapConfig2.NAME_AREA_HEIGHT, new IdentityStops<Float>()))
                fillExtrusionHeight(Function.property("height", new IdentityStops<Float>())),
//                        fillExtrusionHeight() // 高度拉伸
//                        fillExtrusionBase() // 阴影
//                        fillExtrusionColor() // 颜色
//                        fillExtrusionOpacity() //不透明度
//                        fillExtrusionPattern() //模式;   花样，样品;   图案;   榜样，典范;
//                        fillExtrusionTranslate() //转化
//                        fillExtrusionTranslateAnchor()
                PropertyFactory.fillExtrusionBase(0.0f) // 阴影大小
        );
        mapboxMap.addLayer(arealayer);

        if (mapboxMap.getLayer(MapConfig2.LAYERID_AREA_LINE) != null) {
            mapboxMap.removeLayer(MapConfig2.LAYERID_AREA_LINE);
        }

        LineLayer areaLineLayer = new LineLayer(MapConfig2.LAYERID_AREA_LINE, sourceId);
        areaLineLayer.setProperties(
                PropertyFactory.lineWidth(0.3f),
                PropertyFactory.lineColor(Function.property("outLineColor", new
                        IdentityStops<String>())),
//                PropertyFactory.lineColor(Function.property("outLineColor", new IdentityStops<String>()))
                PropertyFactory.fillExtrusionHeight(Function.property("lineHeight", new IdentityStops<Float>()))
        );
        //-----3D-----(3D的时候线要放在面上面才能显示出来)
        // TODO 现在不要线了
//        mapboxMap.addLayerAbove(areaLineLayer, MapConfig2.LAYERID_AREA);

        mapboxMap.addLayer(areaLineLayer);
        if (mapboxMap.getLayer(MapConfig2.LAYERID_AREA_TEXT) != null) {
            mapboxMap.removeLayer(MapConfig2.LAYERID_AREA_TEXT);
        }

        //Area_Text
        SymbolLayer areaTextLayer = new SymbolLayer(MapConfig2.LAYERID_AREA_TEXT, sourceId);
        areaTextLayer.setProperties(
                PropertyFactory.textField("{display}"),
//                PropertyFactory.textColor("#475266"),
//                PropertyFactory.textSize(10.f),
                PropertyFactory.textColor(Function.property(MapConfig2.NAME_TEXT_COLOR, new IdentityStops<String>())),
                PropertyFactory.textSize(Function.property(MapConfig2.NAME_TEXT_SIZE, new IdentityStops<Float>())),
                PropertyFactory.iconSize(.5f),
                PropertyFactory.textAnchor(Property.TEXT_JUSTIFY_LEFT),
                PropertyFactory.iconOffset(new Float[]{-10.f, 0.f}),
                PropertyFactory.iconImage(Function.property("logo", Stops.<String>identity())),
                PropertyFactory.textPadding(8f)
//                ,PropertyFactory.textAllowOverlap(allowShow)

        );

        areaTextLayer.setFilter(Filter.has("display"));
        mapboxMap.addLayer(areaTextLayer);

        SymbolLayer areaHighlightTextLayer = new SymbolLayer("hightLightText", sourceId);
        areaHighlightTextLayer.setProperties(
                PropertyFactory.textField("{allowShowName}"),
//                PropertyFactory.textColor("#475266"),
//                PropertyFactory.textSize(10.f),
                PropertyFactory.textColor(Function.property(MapConfig2.NAME_TEXT_COLOR, new IdentityStops<String>())),
                PropertyFactory.textSize(Function.property(MapConfig2.NAME_TEXT_SIZE, new IdentityStops<Float>())),
                PropertyFactory.iconSize(.5f),
                PropertyFactory.textAnchor(Property.TEXT_JUSTIFY_LEFT),
                PropertyFactory.iconOffset(new Float[]{-10.f, 0.f}),
                PropertyFactory.iconImage(Function.property("logo", Stops.<String>identity())),
                PropertyFactory.textPadding(8f)
               ,PropertyFactory.textAllowOverlap(true)

        );

//        areaHighlightTextLayer.setFilter(Filter.has("display"));
        mapboxMap.addLayer(areaHighlightTextLayer);
    }

    public static void addFacilityData(Context context, MapboxMap mMapboxMap, FloorBean mFloorBean){
        initFacilityData(context, mMapboxMap, mFloorBean);
        String geojson = GuoMapUtilsTow.getGeoJson(context, mFloorBean.getFacilityFilename());
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);
        for (Feature feature : featureCollection.getFeatures()) {
            String logo = feature.getStringProperty("logo");
            if (!TextUtils.isEmpty(logo)) {
//                asyncLoadIcon(logo,mMapboxMap,context);
            }
        }
    }

    // 加载图标，网络版, 暂时不添加
//    public static void asyncLoadIcon(final String logoUrl, final MapboxMap mapboxMap,Context context) {
//        RequestBuilder<Bitmap> bitmapRequestBuilder = Glide.with(context).asBitmap().load("http://api.ipalmap.com/logo/64/" + logoUrl);
//        bitmapRequestBuilder.into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                mapboxMap.addImage(logoUrl, resource);
//                if (mapboxMap.getLayer(MapConfig.LAYERID_FACILITY) != null) {
//                    Layer layer = mapboxMap.getLayer(MapConfig.LAYERID_FACILITY);
//                    layer.setProperties(
//                            PropertyFactory.iconImage(Function.property("logo", Stops.<String>identity()))
//                    );
//                }
//            }
//        });
//    }


    public static void initFacilityData(Context context, MapboxMap mMapboxMap, FloorBean mFloorBean) {
        Log.i("initFacilityData", "initFacilityData: ");
        String sourceId = mFloorBean.getFacilityFilename();
        String fileName = mFloorBean.getFacilityFilename();
        addMapFacilitySource(context, mMapboxMap, sourceId, fileName);
        if (mMapboxMap.getLayer(MapConfig2.LAYERID_FACILITY) != null) {
            mMapboxMap.removeLayer(MapConfig2.LAYERID_FACILITY);
        }
        SymbolLayer facilityLayer = new SymbolLayer(MapConfig2.LAYERID_FACILITY, sourceId);
        facilityLayer.setProperties(PropertyFactory.fillExtrusionHeight(1f));
        mMapboxMap.addLayer(facilityLayer);
    }

    public static void addMapSource(Context context, MapboxMap mMapboxMap, String mSouceId, String fileName) {
        String geojson = GuoMapUtilsTow.getGeoJson(context, fileName);
//        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
        // 计算中心点
        if (mSouceId.contains("area")) {
            //刘艺博的方式
            // TODO 此处地图配色
//            StyleManagerHXForH5 mStyleManager = new StyleManagerHXForH5();
//            String mapjson = FileUtils.loadFromAssets(context, fileName);//读取原始的地图文件
//            FeatureCollection featureCollection = BaseFeatureCollection.fromJson(mapjson);//转换成featurecollection
//            mStyleManager.attach(featureCollection);//进行着色处理
//            geojson = featureCollection.toJson();//处理好的featurecollection转换成json
//            FileUtils.writeToFile("F1_area.geojson", geojson);

            //------json——style的方式
//            String styleJson = FileUtils.loadFromAssets(context, "HxH5Style.json");
//            MapStyleManagerHXForH5 styleManager = new MapStyleManagerHXForH5(styleJson);
//            FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);
//            styleManager.attachStyle(featureCollection, mSouceId);
//            geojson = featureCollection.toJson();//处理好的featurecollection转换成json
//            styleManager.attachStyle(featureCollection, mSouceId);
        }
//        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);

        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, geojson);

        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
    }

    public static void addMapFacilitySource(Context context, MapboxMap mMapboxMap, String mSouceId, String fileName) {
        String geojson = GuoMapUtilsTow.getGeoJson(context, fileName);
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);//2
        GeoJsonSource geoJsonSource = new GeoJsonSource(mSouceId, featureCollection);
        if (mMapboxMap.getSource(mSouceId) == null) {
            mMapboxMap.addSource(geoJsonSource);
        }
    }

    // 设置本界面展示或切换楼层camera(现有数据只针对于华西医院)
    public static void setUpCamera(MapboxMap mMapboxMap, String floorParameter, double latitude, double longitude) {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(latitude - 0.0035, longitude + 0.0035))
                .include(new LatLng(latitude + 0.0035, longitude - 0.0035))
                .build();
        // TODO 中心点
        mMapboxMap.setLatLngBoundsForCameraTarget(bounds);

        double zoom = 16.5;
        //当楼层的position是3  和4 的时候叫楼层的缩放小一点，别的楼层进行都放比例增大 F0  F1
        if (floorParameter.equals("F3") || floorParameter.equals("F4")) {
            zoom = 15;
        } else if (floorParameter.equals("B1") || floorParameter.equals("B2") || floorParameter.equals("F15")) {  // B1  B2  F15
            zoom = 16;
        } else if (floorParameter.equals("F10") || floorParameter.equals("F11") || floorParameter.equals("F12") || floorParameter.equals("F13") ||
                floorParameter.equals("F14")) { // F10 到 F14
            zoom = 16;
        } else {
            zoom = 15;
        }

        latitude += 0.002;
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .tilt(0) // 倾斜角度
                .build();
        // 使用一个动画来调整地图
        mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
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
//        Layer layer = mapboxMap.getLayer(MapConfig2.LAYERID_AREA);
//        layer.setProperties(
//                fillExtrusionHeight(Function.property("3DHeight", new IdentityStops<Float>()))
//        );
        // 添加线 areaLine
        Layer lineLayer = mapboxMap.getLayer(MapConfig2.LAYERID_AREA_LINE);
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
//        Layer layer = mapboxMap.getLayer(MapConfig2.LAYERID_AREA);
//        layer.setProperties(
//                fillExtrusionHeight(Function.property("3DHeight", new IdentityStops<Float>()))
//        );
//        // 去掉线条，3D添加线条后太难看了,此处对线条进行了隐藏
//        Layer lineLayer = mapboxMap.getLayer(MapConfig2.LAYERID_AREA_LINE);
//        if (lineLayer != null) {
//            lineLayer.setProperties(PropertyFactory.visibility
//                    (Property.NONE));
//        }
        // 使用一个动画来调整地图
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    // 设置地图边界bounds
    public static void setMapBounds(MapboxMap mapboxMap, double bound){
        LatLng latLng = mapboxMap.getCameraPosition().target;
        double lat = latLng.getLatitude();
        double log = latLng.getLongitude();

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(lat - bound, log + bound))
                .include(new LatLng(lat + bound, log - bound))
                .build();
        // TODO 设置边界
        mapboxMap.setLatLngBoundsForCameraTarget(bounds);
    }
}
