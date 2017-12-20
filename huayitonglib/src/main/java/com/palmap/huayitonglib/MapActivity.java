package com.palmap.huayitonglib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.bean.FloorBean;
import com.palmap.huayitonglib.utils.Config;
import com.palmap.huayitonglib.utils.Constant;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.utils.GuoMapUtils;
import com.palmap.huayitonglib.utils.MapInitUtils;
import com.palmap.huayitonglib.utils.MapUtils;
import com.palmap.huayitonglib.utils.MapUtils2;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private MapActivity self;
    private FloorBean mFloorBean;
    //当前FloorId为平面层楼层
    private int mCurrentFloorId = Config.FLOORID_F0_CH;

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

    private void initView() {
        map_scrollview = (ScrollView) findViewById(R.id.map_scrollview);
        changefloor_text = (TextView) findViewById(R.id.changefloor_text);
        xishoujian_image = (ImageView) findViewById(R.id.xishoujian_image);
        yinhang_image = (ImageView) findViewById(R.id.yinhang_image);
        dianti_image = (ImageView) findViewById(R.id.dianti_image);
        futi_image = (ImageView) findViewById(R.id.futi_image);
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
            } else {
                initCommonIcon();
            }
        } else if (i == R.id.yinhang_image) {
            if (yinhang) {
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang_dianji);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                yinhang = false;
                xishoujian = true;
                dianti = true;
                futi = true;
            } else {
                initCommonIcon();
            }

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
            } else {
                initCommonIcon();
            }

        } else if (i == R.id.jia_rr) {
            //地图放大
            MapUtils.setMapExtend(mMapboxMap);
        } else if (i == R.id.jian_rr) {
            //地图缩小
            MapUtils.setMapSmall(mMapboxMap);
        }
    }

    //------------切换楼层点击按钮
    public void onChangeFloorClick(View view) {
        map_scrollview.setVisibility(View.GONE);
        int i = view.getId();
        if (i == R.id.b2) {
            shiftFloors("B2", false);
            changefloor_text.setText("B2");
        } else if (i == R.id.b1) {
            shiftFloors("B1", false);
            changefloor_text.setText("B1");
        } else if (i == R.id.f0) {
            //平面图------------
            shiftFloors("F0", false);
            changefloor_text.setText("平面图");
        } else if (i == R.id.f1) {
            shiftFloors("F1", false);
            changefloor_text.setText("F1");
        } else if (i == R.id.f2) {
            shiftFloors("F2", false);
            changefloor_text.setText("F2");
        } else if (i == R.id.f3) {
            shiftFloors("F3", false);
            changefloor_text.setText("F3");
        } else if (i == R.id.f4) {
            shiftFloors("F4", false);
            changefloor_text.setText("F4");
        } else if (i == R.id.f5) {
            shiftFloors("F5", false);
            changefloor_text.setText("F5");
        } else if (i == R.id.f6) {
            shiftFloors("F6", false);
            changefloor_text.setText("F6");
        } else if (i == R.id.f7) {
            shiftFloors("F7", false);
            changefloor_text.setText("F7");
        } else if (i == R.id.f8) {
            shiftFloors("F8", false);
            changefloor_text.setText("F8");
        } else if (i == R.id.f9) {
            shiftFloors("F9", false);
            changefloor_text.setText("F9");
        } else if (i == R.id.f10) {
            shiftFloors("F10", false);
            changefloor_text.setText("F10");
        } else if (i == R.id.f11) {
            shiftFloors("F11", false);
            changefloor_text.setText("F11");
        } else if (i == R.id.f12) {
            shiftFloors("F12", false);
            changefloor_text.setText("F12");
        } else if (i == R.id.f13) {
            shiftFloors("F13", false);
            changefloor_text.setText("F13");
        } else if (i == R.id.f14) {
            shiftFloors("F14", false);
            changefloor_text.setText("F14");
        } else if (i == R.id.f15) {
            shiftFloors("F15", false);
            changefloor_text.setText("F15");
        }
    }

    public void onNavClick(View view) {
        int i = view.getId();
        if (i == R.id.routeLin){
            //去这里的按钮
        }

    }

    //显示和影藏切换楼层滑动框
    private void changeFloorScroll() {
        //点击切换楼层的按钮
        if (map_scrollview.getVisibility() == View.GONE) {
            map_scrollview.setVisibility(View.VISIBLE);
        } else {
            map_scrollview.setVisibility(View.GONE);
        }
    }

    //第一次进入界面的展示
    private void initUiView() {
        //初始化界面的时候，对切换楼层滑动框进行影藏;
        map_scrollview.setVisibility(View.GONE);
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
        }
    }

    private void loadSelfMap() {
        GuoMapUtils.addBackgroudLayer(getApplicationContext(), mMapboxMap);
        GuoMapUtils.addFrameLayer(getBaseContext(), mMapboxMap, mFloorBean, 1);
        GuoMapUtils.addAreaLayer(getBaseContext(), mMapboxMap, mFloorBean);
//        addFacilityLayer(TYPE_NOICON);
    }


    //楼层的标识符
    private String mAlias = "F1";
    double mLatitude, mLngtitude;//墨卡托坐标

    //切换楼层的方法
    private void shiftFloors(String alias, boolean isJump) {

        if (!TextUtils.isEmpty(alias)) {
            mAlias = alias;
            changefloor_text.setText("F7");
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
    }

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
