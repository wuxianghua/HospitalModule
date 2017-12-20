package com.palmap.huayitonglib;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.bean.FloorBean;
import com.palmap.huayitonglib.db.bridge.MapPointInfoDbManager;
import com.palmap.huayitonglib.utils.Config;
import com.palmap.huayitonglib.utils.Constant;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.utils.GuoMapUtils;
import com.palmap.huayitonglib.utils.MapInitUtils;
import com.palmap.huayitonglib.utils.MapUtils;
import com.palmap.huayitonglib.utils.MapUtils2;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;

import static com.palmap.huayitonglib.utils.Config.FLOORLIST;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private MapActivity self;
    private FloorBean mFloorBean;
    //当前FloorId为平面层楼层
    private int mCurrentFloorId = Config.FLOORID_F0_CH;


    //设置应用图标：TYPE_RESTROOM---洗手间，TYPE_ESCALATOR-----扶梯，TYPE_ELEVATOR-----电梯，TYPE_ALL--所有图标，TYPE_NOICON----不设置图标

    Handler h = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), Constant.APP_KEY);
        setContentView(R.layout.activity_map);
        initView();
        self = this;
        initMapData();
        initMapView(savedInstanceState);

    }

    ScrollView map_scrollview;
    TextView changefloor_text;
    ImageView xishoujian_image, yinhang_image, dianti_image, futi_image;
    LoopView loopView;

    private void initView() {
        map_scrollview = (ScrollView) findViewById(R.id.map_scrollview);
        changefloor_text = (TextView) findViewById(R.id.changefloor_text);
        xishoujian_image = (ImageView) findViewById(R.id.xishoujian_image);
        yinhang_image = (ImageView) findViewById(R.id.yinhang_image);
        dianti_image = (ImageView) findViewById(R.id.dianti_image);
        futi_image = (ImageView) findViewById(R.id.futi_image);
//        loopView = (LoopView) findViewById(R.id.loopView);
        initLoopView();

    }

    private void initLoopView() {

        //        // 1、直接new 一个线程类，传入参数实现Runnable接口的对象（new Runnable），相当于方法二
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //------------------处理切换楼层3d滚轮
                loopView = (LoopView) findViewById(R.id.loopView);
                ArrayList<String> list = new ArrayList();
                for (int i = 0; i < Config.FLOORLIST.length; i++) {
                    list.add(Config.FLOORLIST[i]);
                }
                loopView.setItems(list);
//                loopView.setBackgroundColor(Color.parseColor("#ffffff"));
                //设置是否循环播放
                loopView.setNotLoop();
                //设置初始位置
                loopView.setInitPosition(14);
                loopView.setTextSize(12);//设置字体大小
                loopView.setCenterTextColor(Color.parseColor("#32B9AA"));//设置字体大小

                loopView.setDividerColor(Color.parseColor("#32B9AA"));
                loopView.setDrawingCacheBackgroundColor(Color.parseColor("#32B9AA"));
                //滚动监听
                loopView.setListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        String floor = Config.FLOORLIST[index];
                        Log.i("onItemSelected", "onItemSelected:------------- " + floor);
                        if (floor.equals("平面图")) {
                            shiftFloors("F0", false);
                        } else {
                            shiftFloors(floor, false);
                        }

                    }
                });
            }
        }, 500);

    }

    //公共图标设置按钮，变量
    boolean xishoujian = true;
    boolean yinhang = true;
    boolean dianti = true;
    boolean futi = true;

    //-------------------界面点击事件
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_rr) {
            finish();
        } else if (i == R.id.changefloor_text) {
            changeFloorScroll();
        } else if (i == R.id.search_rr) {
            startActivity(new Intent(this, SearchActivity.class));
            initUiView();
        } else if (i == R.id.xishoujian_image) {
            if (xishoujian) {
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian_dianji);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                xishoujian = false;
                yinhang = true;
                dianti = true;
                futi = true;
                addFacilityLayer(TYPE_RESTROOM);
            } else {
                initCommonIcon();
            }
        } else if (i == R.id.yinhang_image) {
//            if (yinhang) {
//                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang_dianji);
//                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
//                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
//                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
//                yinhang = false;
//                xishoujian = true;
//                dianti = true;
//                futi = true;
//                addFacilityLayer(TYPE_BRAND);
//            } else {
//                initCommonIcon();
//            }
            centerToast("地图数据完善中，暂无银行数据");
        } else if (i == R.id.dianti_image) {
            if (dianti) {
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti_dianji);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                dianti = false;
                xishoujian = true;
                yinhang = true;
                futi = true;
                addFacilityLayer(TYPE_ELEVATOR);
            } else {
                initCommonIcon();
            }
        } else if (i == R.id.futi_image) {
            if (futi) {
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi_dianji);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                futi = false;
                xishoujian = true;
                yinhang = true;
                dianti = true;
                addFacilityLayer(TYPE_ESCALATOR);
            } else {
                initCommonIcon();
            }
        } else if (i == R.id.jia_rr) {
            //地图放大
            MapUtils.setMapExtend(mMapboxMap);
        } else if (i == R.id.jian_rr) {
            //地图缩小
            MapUtils.setMapSmall(mMapboxMap);
        } else if (i == R.id.yuyin_rr) {
            centerToast("正在建设中，敬请期待");
        }
    }

    //------------切换楼层点击按钮
    public void onChangeFloorClick(View view) {
//        map_scrollview.setVisibility(View.GONE);
//        int i = view.getId();
//        if (i == R.id.b2) {
//            shiftFloors("B2", false);
//            changefloor_text.setText("B2");
//        } else if (i == R.id.b1) {
//            shiftFloors("B1", false);
//            changefloor_text.setText("B1");
//        } else if (i == R.id.f0) {
//            //平面图------------
//            shiftFloors("F0", false);
//            changefloor_text.setText("平面图");
//        } else if (i == R.id.f1) {
//            shiftFloors("F1", false);
//            changefloor_text.setText("F1");
//        } else if (i == R.id.f2) {
//            shiftFloors("F2", false);
//            changefloor_text.setText("F2");
//        } else if (i == R.id.f3) {
//            shiftFloors("F3", false);
//            changefloor_text.setText("F3");
//        } else if (i == R.id.f4) {
//            shiftFloors("F4", false);
//            changefloor_text.setText("F4");
//        } else if (i == R.id.f5) {
//            shiftFloors("F5", false);
//            changefloor_text.setText("F5");
//        } else if (i == R.id.f6) {
//            shiftFloors("F6", false);
//            changefloor_text.setText("F6");
//        } else if (i == R.id.f7) {
//            shiftFloors("F7", false);
//            changefloor_text.setText("F7");
//        } else if (i == R.id.f8) {
//            shiftFloors("F8", false);
//            changefloor_text.setText("F8");
//        } else if (i == R.id.f9) {
//            shiftFloors("F9", false);
//            changefloor_text.setText("F9");
//        } else if (i == R.id.f10) {
//            shiftFloors("F10", false);
//            changefloor_text.setText("F10");
//        } else if (i == R.id.f11) {
//            shiftFloors("F11", false);
//            changefloor_text.setText("F11");
//        } else if (i == R.id.f12) {
//            shiftFloors("F12", false);
//            changefloor_text.setText("F12");
//        } else if (i == R.id.f13) {
//            shiftFloors("F13", false);
//            changefloor_text.setText("F13");
//        } else if (i == R.id.f14) {
//            shiftFloors("F14", false);
//            changefloor_text.setText("F14");
//        } else if (i == R.id.f15) {
//            shiftFloors("F15", false);
//            changefloor_text.setText("F15");
//        }
    }

    public void onNavClick(View view) {
        int i = view.getId();
        if (i == R.id.routeLin) {
            //去这里的按钮
        }

    }

    //显示和影藏切换楼层滑动框
    private void changeFloorScroll() {
        //点击切换楼层的按钮
        if (loopView.getVisibility() == View.GONE) {
            loopView.setVisibility(View.VISIBLE);
        } else {
            loopView.setVisibility(View.GONE);
        }
    }

    //第一次进入界面的展示
    private void initUiView() {
        //初始化界面的时候，对切换楼层滑动框进行影藏;
        loopView.setVisibility(View.GONE);
        initCommonIcon();
    }

    private void initCommonIcon() {
        //过滤图标恢复到第一次进入界面的展示
        futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
        dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
        yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
        xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
        futi = true;
        xishoujian = true;
        yinhang = true;
        dianti = true;
        addFacilityLayer(TYPE_NOICON);
    }

    //--------------------------以下是地图相关内容--------------------------
    private void initMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new onMapReadyCallback());
    }

    private void initMapData() {
        mFloorBean = FileUtils.getSourceName("F1");
    }

    class onMapReadyCallback implements OnMapReadyCallback {

        @Override
        public void onMapReady(MapboxMap mapboxMap) {
            mMapboxMap = mapboxMap;
            GuoMapUtils.initMapSetting(self, mMapboxMap);
            //删除图层后加载自己的地图
            MapUtils2.removeAllOriginalLayers(mMapboxMap);
            // 加载地图
            loadSelfMap();
//            new Thread(new Runnable() {
//                @RequiresApi(api = Build.VERSION_CODES.M)
//                @Override
//                public void run() {
//                    // 写子线程中的操作
//
//
//                }
//            }).start();
        }
    }

    private void loadSelfMap() {

        GuoMapUtils.addBackgroudLayer(getApplicationContext(), mMapboxMap);
        GuoMapUtils.addFrameLayer(getBaseContext(), mMapboxMap, mFloorBean, 1);
        GuoMapUtils.addAreaLayer(getBaseContext(), mMapboxMap, mFloorBean);
//        addFacilityLayer(TYPE_NOICON);
        addFacilityLayer(TYPE_NOICON);
    }

    //设置应用图标：TYPE_RESTROOM---洗手间，TYPE_ESCALATOR-----扶梯，TYPE_ELEVATOR-----电梯，TYPE_ALL--所有图标，TYPE_NOICON----不设置图标
    private static final int TYPE_RESTROOM = 0;
    private static final int TYPE_ESCALATOR = 1;
    private static final int TYPE_ELEVATOR = 2;
    private static final int TYPE_BRAND = 3;
    private static final int TYPE_ALL = 4;
    private static final int TYPE_NOICON = 5;

    private void addFacilityLayer(int type) {
        GuoMapUtils.initFacilityData(getBaseContext(), mMapboxMap, mFloorBean);
        String geojson = MapUtils2.getGeoJson(getBaseContext(), mFloorBean.getFacilityFilename());
        FeatureCollection featureCollection = BaseFeatureCollection.fromJson(geojson);
        for (Feature feature : featureCollection.getFeatures()) {
            //设备图标过滤
            if (!feature.hasProperty("category")) {
                continue;
            }
            mMapboxMap.removeImage(feature.getStringProperty("logo"));
            switch (type) {
                case TYPE_ELEVATOR:
                    //设置的所有电梯图标
                    MapUtils.setElevatorIcon(this, mMapboxMap, feature);
                    break;
                case TYPE_ESCALATOR:
                    //设置的楼梯设备图标
                    MapUtils.setEscalatorIcon(this, mMapboxMap, feature);
                    break;
                case TYPE_RESTROOM:
                    //设置的洗手间图标
                    MapUtils.setRestRoomIcon(this, mMapboxMap, feature);
                    break;
                case TYPE_BRAND:
                    //设置的洗手间图标
                    MapUtils.setBankIcon(this, mMapboxMap, feature);
                    break;
                case TYPE_ALL:
                    //设置的所有设备图标
                    MapUtils.setAllMapIcon(this, mMapboxMap, feature);
                    break;
                case TYPE_NOICON:
                    //设置没有应用图标
                    mMapboxMap.removeImage(feature.getStringProperty("logo"));
                    break;
            }
        }

    }


    //楼层的标识符
    private String mAlias = "F1";
    double mLatitude, mLngtitude;//墨卡托坐标

    //切换楼层的方法
    private void shiftFloors(String alias, boolean isJump) {

        if (!TextUtils.isEmpty(alias)) {
            mAlias = alias;
            if (mAlias.equals("F0")) {
                changefloor_text.setText("平面图");
            } else {
                changefloor_text.setText(mAlias);
            }

        }
        mFloorBean = FileUtils.getSourceName(mAlias);
//        MapInitUtils.setOtherUpCamera(mMapboxMap, mLatitude, mLngtitude, poid);
        MapInitUtils.setUpCamera(mMapboxMap, mAlias, mLatitude, mLngtitude);
        Log.i("zyy", "shiftFloors: 同一楼层，不再切换");
        mCurrentFloorId = FileUtils.getFloorId(getBaseContext(), alias);
        Log.i("lyb ", "shiftFloors: mCurrentFloorId " + mCurrentFloorId);
        if (mMapboxMap.getLayer(Config.LAYERID_FACILITY) != null) {
            mMapboxMap.getLayer(Config.LAYERID_FACILITY).setProperties(PropertyFactory.visibility
                    (Property.NONE));
        }
//        切换楼层 把除backgroundlayer之外的layer全部清除
        MapUtils.removeAllWithOutBackgroundlayer(mMapboxMap);
        //-----起点终点在切换容易被覆盖--------要先清除---

//        removeEndMarker();
//        removeStartMarker();
//        removeFindMark();
//        removeAllFlowMarkRecord();
//        removeDianTiMarker();
        //加载地图
        loadSelfMap();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loading_rel.setVisibility(View.GONE);
//            }
//        }, 500);
//        if (canDisplayEndInfo()) {
//            Log.i("canDisplayStartInfo", "shiftFloors:-----------canDisplayEndInfo ");
//            addEndMarker(new LatLng(mPlanRouteBean.getEndLatitude(), mPlanRouteBean.getEndLongtitude()));
//        }
//
//        if (canDisplayStartInfo()) {
//            Log.i("canDisplayStartInfo", "shiftFloors:-----------canDisplayStartInfo ");
//            addStartMarker(new LatLng(mPlanRouteBean.getStartLatitude(), mPlanRouteBean.getStartLongtitude()));
//        }
//
//        if (canDisplayFindInfo()) {
//            Log.i(TAG, "canDisplayFindInfo true");
//            addFindMark(new LatLng(mPlanRouteBean.getFindLatitude(), mPlanRouteBean.getFindLongtitude()));
//        }

//        switch (canDisplayRouteInfo()) {
//            case 0:
//                break;
//            case 1:
//                if (mPlanRouteBean.getFromRoute() != null) {
//                    //如果有起点楼层的导航路线，就显示起点楼层的导航路线
//                    mNavimaager.showRoute(mPlanRouteBean.getFromRoute());
//                    if (routeFloorStart != null) {
//                        //起点楼层的导航路线对应的是，起点楼层的连通设施经纬度。如果连通设施的经纬度存在，那就显示出来
//                        addDianTiMark(routeFloorStart);
//                    }
//                }
//                break;
//            case 2:
//                if (mPlanRouteBean.getOtherRoute() != null) {
//                    //如果有终点楼层的导航路线，就显示终点楼层的导航路线
//                    mNavimaager.showRoute(mPlanRouteBean.getOtherRoute());
//                    if (routeFloorEnd != null) {
//                        //终点楼层的导航路线对应的是，终点楼层的连通设施的经纬度。如果连通设施的经纬度存在，那就显示出来
//                        addDianTiMark(routeFloorEnd);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
    }

    public void centerToast(String str) {
        Toast toast = Toast.makeText(getBaseContext(),
                str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
//        // 此处检测是否已导入搜索对应数据，如未导入，则在此开启子线程进行数据导入
//        if (MapPointInfoDbManager.get().getAll() == null || MapPointInfoDbManager.get().getAll().size() == 0) {
//            handler.sendEmptyMessageDelayed(1, 50);
//        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                Toast.makeText(self, "首次安装，正在准备数据，请耐心等待", Toast.LENGTH_SHORT).show();
                MapPointInfoDbManager.get().insertAllData(self);
            }
            super.handleMessage(msg);
        }

    };


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
