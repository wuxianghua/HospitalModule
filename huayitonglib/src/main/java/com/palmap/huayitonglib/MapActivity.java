package com.palmap.huayitonglib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.bean.FloorBean;
import com.palmap.huayitonglib.utils.Constant;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.utils.MapInitUtils;
import com.palmap.huayitonglib.utils.MapUtils;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private MapActivity self;
    private FloorBean mFloorBean;


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
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                xishoujian = true;
                yinhang = true;
                dianti = true;
                futi = true;
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
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                yinhang = true;
                xishoujian = true;
                dianti = true;
                futi = true;
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
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                dianti = true;
                xishoujian = true;
                yinhang = true;
                futi = true;
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
                futi_image.setBackgroundResource(R.mipmap.ic_map_futi);
                dianti_image.setBackgroundResource(R.mipmap.ic_map_dianti);
                yinhang_image.setBackgroundResource(R.mipmap.ic_map_yinhang);
                xishoujian_image.setBackgroundResource(R.mipmap.ic_map_xishoujian);
                futi = true;
                xishoujian = true;
                yinhang = true;
                dianti = true;
            }

        } else if (i == R.id.jia_rr) {
            //地图放大
        } else if (i == R.id.jian_rr) {
            //地图缩小
        }
    }

    //------------切换楼层点击按钮
    public void onChangeFloorClick(View view) {
        map_scrollview.setVisibility(View.GONE);
        int i = view.getId();
        if (i == R.id.b2) {
            changefloor_text.setText("B2");
        } else if (i == R.id.b1) {
            changefloor_text.setText("B1");
        } else if (i == R.id.f0) {
            //平面图------------
            changefloor_text.setText("平面图");
        } else if (i == R.id.f1) {
            changefloor_text.setText("F1");
        } else if (i == R.id.f2) {
            changefloor_text.setText("F2");
        } else if (i == R.id.f3) {
            changefloor_text.setText("F3");
        } else if (i == R.id.f4) {
            changefloor_text.setText("F4");
        } else if (i == R.id.f5) {
            changefloor_text.setText("F5");
        } else if (i == R.id.f6) {
            changefloor_text.setText("F6");
        } else if (i == R.id.f7) {
            changefloor_text.setText("F7");
        } else if (i == R.id.f8) {
            changefloor_text.setText("F8");
        } else if (i == R.id.f9) {
            changefloor_text.setText("F9");
        } else if (i == R.id.f10) {
            changefloor_text.setText("F10");
        } else if (i == R.id.f11) {
            changefloor_text.setText("F11");
        } else if (i == R.id.f12) {
            changefloor_text.setText("F12");
        } else if (i == R.id.f13) {
            changefloor_text.setText("F13");
        } else if (i == R.id.f14) {
            changefloor_text.setText("F14");
        } else if (i == R.id.f15) {
            changefloor_text.setText("F15");
        }
    }

    public void onNavClick(View view) {
        int i = view.getId();
        if (i == R.id.routeLin) {
            
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

    class onMapReadyCallback implements OnMapReadyCallback{

        @Override
        public void onMapReady(MapboxMap mapboxMap) {
            mMapboxMap = mapboxMap;
            MapInitUtils.initMapSetting(self,mMapboxMap);
            //删除图层后加载自己的地图
            MapUtils.removeAllOriginalLayers(mMapboxMap);
            // 加载地图
            loadSelfMap();
        }
    }

    private void loadSelfMap() {
        MapInitUtils.addBackgroudLayer(getApplicationContext(), mMapboxMap);
        MapInitUtils.addFrameLayer(getBaseContext(), mMapboxMap, mFloorBean, 1);
        MapInitUtils.addAreaLayer(getBaseContext(), mMapboxMap, mFloorBean);
//        addFacilityLayer(TYPE_NOICON);
    }

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
