package com.palmap.huayitonglib;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.bean.FloorBean;
import com.palmap.huayitonglib.db.bridge.MapPointInfoDbManager;
import com.palmap.huayitonglib.navi.shownaviroute.PlanRouteListener;
import com.palmap.huayitonglib.navi.shownaviroute.RouteBean;
import com.palmap.huayitonglib.navi.shownaviroute.RouteManager;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.utils.Config;
import com.palmap.huayitonglib.utils.Constant;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.utils.GuoMapUtils;
import com.palmap.huayitonglib.utils.MapConfig2;
import com.palmap.huayitonglib.utils.MapInitUtils;
import com.palmap.huayitonglib.utils.MapUtils;
import com.palmap.huayitonglib.utils.GuoMapUtilsTow;
import com.palmap.huayitonglib.utils.MarkerUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import static com.palmap.huayitonglib.activity.SearchActivity.SEARCH_END;
import static com.palmap.huayitonglib.activity.SearchActivity.SEARCH_START;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private MapActivity self;
    private FloorBean mFloorBean;
    //当前FloorId为平面层楼层
    private int mCurrentFloorId = Config.FLOORID_F1_CH;


    //设置应用图标：TYPE_RESTROOM---洗手间，TYPE_ESCALATOR-----扶梯，TYPE_ELEVATOR-----电梯，TYPE_ALL--所有图标，TYPE_NOICON----不设置图标

    Handler h = null;
    private RouteManager mRouteManager;

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
    ImageView xishoujian_image, yinhang_image, dianti_image, futi_image, jian_image, jia_image;
    LoopView loopView;
    RelativeLayout loading_rel;

    RelativeLayout title_rr, search_rr, park_zhongdian, luxianguihua_rr, nagv_01_rr, selectstart_rr_01, nagv_top01;
    LinearLayout setstar_bttom_ll01;
    Button stop_nagv_btn;



    ImageView view_2D_3D; // 2D.3D切换按钮
    TextView bilichi_Tv; // 比例尺显示的距离

    private void initView() {
        view_2D_3D = (ImageView) findViewById(R.id.view_2D_3D);
        bilichi_Tv = (TextView) findViewById(R.id.bilichi_Tv);

        jian_image = (ImageView) findViewById(R.id.jian_image);
        jia_image = (ImageView) findViewById(R.id.jia_image);
        jia_image.setImageResource(R.mipmap.ngr_ic_map_zoomin);
        jian_image.setImageResource(R.mipmap.ngr_ic_map_zoomout);
        loading_rel = (RelativeLayout) findViewById(R.id.loading_rel);
        loading_rel.setVisibility(View.VISIBLE);
        map_scrollview = (ScrollView) findViewById(R.id.map_scrollview);
        changefloor_text = (TextView) findViewById(R.id.changefloor_text);
        xishoujian_image = (ImageView) findViewById(R.id.xishoujian_image);
        yinhang_image = (ImageView) findViewById(R.id.yinhang_image);
        dianti_image = (ImageView) findViewById(R.id.dianti_image);
        futi_image = (ImageView) findViewById(R.id.futi_image);
//        loopView = (LoopView) findViewById(R.id.loopView);
        initLoopView();

        //-------------显示逻辑对接view\
        //标题栏
        title_rr = (RelativeLayout) findViewById(R.id.title_rr);
        //搜索框
        search_rr = (RelativeLayout) findViewById(R.id.search_rr);
        //终点选择
        park_zhongdian = (RelativeLayout) findViewById(R.id.park_zhongdian);
        //选择起点2（底部）
        setstar_bttom_ll01 = (LinearLayout) findViewById(R.id.setstar_bttom_ll01);
        //路线规划
        luxianguihua_rr = (RelativeLayout) findViewById(R.id.luxianguihua_rr);
        //导航中
        nagv_01_rr = (RelativeLayout) findViewById(R.id.nagv_01_rr);
        //导航结束
        stop_nagv_btn = (Button) findViewById(R.id.stop_nagv_btn);


        //顶部显示
        //选择起点
        selectstart_rr_01 = (RelativeLayout) findViewById(R.id.selectstart_rr_01);
        //顶部导航提示01
        nagv_top01 = (RelativeLayout) findViewById(R.id.nagv_top01);
//        //----------------
//        changeNavigaView(SHOUYE_SHOW_01);
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
                        final String floor = Config.FLOORLIST[index];
                        Log.i("onItemSelected", "onItemSelected:------------- " + floor);

                        loading_rel.setVisibility(View.VISIBLE);
                        if (floor.equals("平面图")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    shiftFloors("F0", false);
                                }
                            }, 100);

                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    shiftFloors(floor, false);
                                }
                            }, 100);
                        }

                    }
                });
            }
        }, 100);

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
//            startActivity(new Intent(this, SearchActivity.class));
            searchEndPoi();
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
        } else if (i == R.id.share_rr) {
            centerToast("正在建设中，敬请期待");
        } else if (i == R.id.routeLin) {
            //点击去这里
            changeNavigaView(STARTSELEE_UNSHOW_03);
        } else if (i == R.id.cancel) {
            //点击去这里
            changeNavigaView(ENDSELEE_SHOW_02);

        } else if (i == R.id.selectstart_back_01) {
            if (type == 5){
                //点击去这里
                changeNavigaView(STARTSELEE_SHOW_04);


            }else {
                //点击去这里
                changeNavigaView(ENDSELEE_SHOW_02);
            }

        } else if (i == R.id.set_qidian) {
            //设置起点
            //进入路线规划
            changeNavigaView(ROUTE_SHOW_05);
        }else if (i == R.id.moni_naviga_btn){
            changeNavigaView(NAVIGA_SHOW_06);
        }else if (i == R.id.nagv_back_01){
            changeNavigaView(ROUTE_SHOW_05);

        } else if (i == R.id.view_2D_3D){
            // TODO 2D 3D
            String str = (String) view_2D_3D.getContentDescription();
            if (str.equals("2D")){
                GuoMapUtils.setUp3DMap(mMapboxMap);
                view_2D_3D.setImageResource(R.mipmap.ic_map_3d);
                view_2D_3D.setContentDescription("3D");
            } else {
                GuoMapUtils.setUp2DMap(mMapboxMap);
                view_2D_3D.setImageResource(R.mipmap.ic_map_2d);
                view_2D_3D.setContentDescription("2D");
            }

        }
    }

    //------------切换楼层点击按钮
    public void onChangeFloorClick(View view) {

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

    //--------------------地图路线规划的相关参数和逻辑判断变量
    //mapStatus地图状态变量-------默认为可点击（true）
    public static boolean mapStatus = true;
    //设置一个变量选择终点
    private int mStartFloorId = 0;//起点floorid
    private int mEndFloorId = 0;//终点floorid
    private double mStartLongtitude = 0;
    private double mStartLatitude = 0;
    private double mEndLongtitude = 0;
    private double mEndLatitude = 0;
    //是否设置过终点（导航逻辑现有起点在有终点）
    private boolean isHaveSetEnd = false;
    MarkerUtils mMarkerUtils;

    private void initMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new onMapReadyCallback());
    }

    private void initMapData() {
        mFloorBean = FileUtils.getSourceName("F1");
        // 此处只是针对对F1层的着色

    }

    class onMapReadyCallback implements OnMapReadyCallback {

        @Override
        public void onMapReady(MapboxMap mapboxMap) {
            mMapboxMap = mapboxMap;
            GuoMapUtils.initMapSetting(self, mMapboxMap);
            //删除图层后加载自己的地图
            GuoMapUtilsTow.removeAllOriginalLayers(mMapboxMap);

            loading_rel.setVisibility(View.VISIBLE);

            //删除图层后加载自己的地图

            mMarkerUtils = new MarkerUtils(mMapboxMap, getBaseContext(), R.mipmap.ic_map_qidian, R.mipmap.ic_map_zhongdian);
            mMapboxMap.setOnCameraChangeListener(new MapboxMap
                    .OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    if (position.zoom <= 15.0) {
                        //已经到最小了，不能再缩小了，给缩小按钮灰色显示
                        jia_image.setImageResource(R.mipmap.ngr_ic_map_zoomin);
                        jian_image.setImageResource(R.mipmap.ic_map_jian);

                    } else if (position.zoom < 20) {
                        jia_image.setImageResource(R.mipmap.ngr_ic_map_zoomin);
                        jian_image.setImageResource(R.mipmap.ngr_ic_map_zoomout);
                    } else if (position.zoom == 20) {
                        jia_image.setImageResource(R.mipmap.ic_map_jia);
                        jian_image.setImageResource(R.mipmap.ngr_ic_map_zoomout);
                    }

                    // 比例尺显示（根据方法级别设置比例尺显示距离）
                    if (position.zoom >= 15 && position.zoom <= 16) {
                        bilichi_Tv.setText("500 m");
                    } else if (position.zoom >= 16 && position.zoom < 17) {
                        bilichi_Tv.setText("200 m");
                    } else if (position.zoom >= 17 && position.zoom < 18) {
                        bilichi_Tv.setText("100 m");
                    } else if (position.zoom >= 18 && position.zoom < 19) {
                        bilichi_Tv.setText("50 m");
                    } else if (position.zoom >= 19 && position.zoom < 20) {
                        bilichi_Tv.setText("20 m");
                    } else if (position.zoom == 20) {
                        bilichi_Tv.setText("5 m");
                    }
                }
            });
            //设置路线管理器
            setRouteManager();

            mMapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng point) {
                    mRouteManager.planRoute(104.0632504, 30.6420972, 2452754, point.getLongitude(), point.getLatitude(),
                            mCurrentFloorId);
                }
            });

            //地图点击事件
            mMapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng point) {
                    PointF pointF2 = mMapboxMap.getProjection().toScreenLocation(point);
                    List<Feature> features = mMapboxMap.queryRenderedFeatures(pointF2);
                    try {
                        for (Feature feature : features) {
                            if (feature.hasProperty("colorId")) {
                                int colorId = feature.getNumberProperty("colorId").intValue();
                                Log.e("zyy", "onMapClick: colorId " + colorId);
                            }

                            if (feature.hasProperty("category")) {
                                int category = feature.getNumberProperty("category").intValue();
                                Log.e("zyy", "onMapClick: category " + category);
                            }
                        }
                    } catch (Exception e) {

                    }

                    if (mapStatus) {
                        PointF pointF = mMapboxMap.getProjection().toScreenLocation(point);
                        //点击先过滤area层，进行不可点击区域的过滤
                        List<Feature> area_features = mMapboxMap.queryRenderedFeatures(pointF, Config.LAYERID_AREA);

                        //进行终点起点显示逻辑
                        if (isHaveSetEnd) {
                            //选择过终点了，进行起点选择逻辑
                            if (area_features.size() == 0) {
                                changeNavigaView(STARTSELEE_UNSHOW_03);
                            } else {
                                Feature feature = MapUtils.queryMaxFeature(area_features);
                                if (feature != null && feature.hasProperty("category")) {
                                    int category = feature.getNumberProperty("category").intValue();
                                    if (category == 23999000 || category == 23062000 || category == 35002000 || category
                                            == 37000000) {
                                        //用户点击不可点击区域的时候------进行查找的mark的删除
                                        changeNavigaView(STARTSELEE_UNSHOW_03);
                                    } else {
                                        addStartMarker(point);
                                        mStartFloorId = mCurrentFloorId;
                                        mStartLongtitude = point.getLongitude();
                                        mStartLatitude = point.getLatitude();
                                        changeNavigaView(STARTSELEE_SHOW_04);
                                    }
                                }

                            }
                        } else {
                            //还未选择终点，进行终点的选择
                            if (area_features.size() == 0) {
                                changeNavigaView(SHOUYE_SHOW_01);
                            } else {
                                Feature feature = MapUtils.queryMaxFeature(area_features);
                                if (feature != null && feature.hasProperty("category")) {
                                    int category = feature.getNumberProperty("category").intValue();
                                    if (category == 23999000 || category == 23062000 || category == 35002000 || category
                                            == 37000000) {
                                        //用户点击不可点击区域的时候------进行查找的mark的删除
                                        changeNavigaView(SHOUYE_SHOW_01);
                                    } else {
                                        addEndMarker(point);

                                        mEndFloorId = mCurrentFloorId;
                                        mEndLongtitude = point.getLongitude();
                                        mEndLatitude = point.getLatitude();
                                        changeNavigaView(ENDSELEE_SHOW_02);
                                    }
                                }
                            }
                        }


//                        if (area_features.size() == 0) {
//                            //用户点击不是area层进行查找的mark的清除
//                            removeFindMark();
//                            removeFindMarkRecord();
//                            //清空查找的mark的poin值
//                            fing_poin = null;
//                            Log.i(TAG, "onMapClick: -------------------------area_features.size() == 0");
//                        } else {
//                            Feature feature = MapUtils.queryMaxFeature(area_features);
//                            if (feature != null && feature.hasProperty("category")) {
//                                int category = feature.getNumberProperty("category").intValue();
//                                if (category == 23999000 || category == 23062000 || category == 35002000 || category
//                                        == 37000000) {
//                                    //用户点击不可点击区域的时候------进行查找的mark的删除
//                                    removeFindMark();
//                                    removeFindMarkRecord();
//                                    //清空查找的mark的poin值
//                                    fing_poin = null;
//                                } else {
//                                    PointF pointF1 = mMapboxMap.getProjection().toScreenLocation(point);
//                                    Log.i(TAG, "onMapClick: -----------------point:" + point.getLongitude() + "----"
//                                            + point.getLatitude());
//                                    List<Feature> features_01 = null;
//                                    features_01 = mMapboxMap.queryRenderedFeatures(pointF1,
//                                            "find_marker_layerid_huaxi");
//                                    if (features_01 == null || features_01.isEmpty() || features_01.size() == 0) {
//                                        Log.i(TAG, "onMapClick: ----------------------没有选中查找图标，另选了别的区域");
//                                        removeEndMarker();
//                                        removeEndMarkerRecord();
//                                        //增加一个查找图标（point会被记录下来，作为用户点击查找图标的终点值）
//                                        addFindMark(point);
//                                        fing_poin = point;
//                                        mapStatus = true;
//                                        return;
//                                    } else {
//                                        // TODO:点击查找图标进入路线规划的界面，顺序----1、加载界面 2、路线规划成功显示终点和路线 3、显示顶部的对应路线信息界面
//                                        fing_poin = point;
//                                        //TODO 进入路线规划的逻辑
//                                        addEndMarker = true;
//                                        intoRoutePlan(point, true);
//                                    }
//                                }
//                            }
//                        }
                    } else {
                        // TODO:地图不可点击状态
                    }

                }
            });

            // 加载地图
            loadSelfMap();
        }
    }

    /**
     * 设置导航路线管理器
     */
    private void setRouteManager() {
        mRouteManager = RouteManager.get();
        mRouteManager.init(MapActivity.this, mMapboxMap, "roadNet.json", R.mipmap.ic_map_dianti,
                MapConfig2.LAYERID_AREA_TEXT);
        mRouteManager.setPlanRouteListener(new PlanRouteListener() {
            @Override
            public boolean onSuccess(RouteBean bean) {
                Log.d("lybb", "路线规划成功了: ");
                mRouteManager.showNaviRoute(mCurrentFloorId);
                return false;
            }

            @Override
            public void onError() {
                Log.d("lybb", "路线规划失败了: ");
            }
        });
    }

    private void loadSelfMap() {
        GuoMapUtils.addBackgroudLayer(getApplicationContext(), mMapboxMap);
        GuoMapUtils.addFrameLayer(getBaseContext(), mMapboxMap, mFloorBean, 1);
        GuoMapUtils.addAreaLayer(getBaseContext(), mMapboxMap, mFloorBean);
        addFacilityLayer(TYPE_NOICON);
        loading_rel.setVisibility(View.GONE);

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
        String geojson = GuoMapUtilsTow.getGeoJson(getBaseContext(), mFloorBean.getFacilityFilename());
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

    int type = -1;
    //首次展示的时候
    public static final int SHOUYE_SHOW_01 = 1;
    //选择终点
    public static final int ENDSELEE_SHOW_02 = 2;
    //选择起点01(还未有起点，点击去这里进行展示)
    public static final int STARTSELEE_UNSHOW_03 = 3;
    //选择起点02(选择起点后展示的界面，已经有起点)
    public static final int STARTSELEE_SHOW_04 = 4;
    //路线规划(有起点，进行路线规划)
    public static final int ROUTE_SHOW_05 = 5;
    //导航中(有起点，进行路线规划)
    public static final int NAVIGA_SHOW_06 = 6;
    //导航结束
    public static final int STOPNAVIGA_SHOW_07 = 6;

    public void changeNavigaView(int types) {
        if (types == SHOUYE_SHOW_01) {
            //标题栏
            title_rr.setVisibility(View.VISIBLE);
            //搜索框
            search_rr.setVisibility(View.VISIBLE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.GONE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.GONE);
            isHaveSetEnd = false;
            removeEndMarker();
            mEndFloorId = 0;
            mEndLongtitude = 0;
            mEndLatitude = 0;
            type = 1;
            mapStatus = true;
        } else if (types == ENDSELEE_SHOW_02) {
            //标题栏
            title_rr.setVisibility(View.VISIBLE);
            //搜索框
            search_rr.setVisibility(View.VISIBLE);
            //终点选择
            park_zhongdian.setVisibility(View.VISIBLE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.GONE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.GONE);
            isHaveSetEnd = false;
            removeStartMarker();
            mStartFloorId = 0;
            mStartLongtitude = 0;
            mStartLatitude = 0;
            type = 2;
            mapStatus = true;
        } else if (types == STARTSELEE_UNSHOW_03) {
            //标题栏
            title_rr.setVisibility(View.GONE);
            //搜索框
            search_rr.setVisibility(View.GONE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.VISIBLE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.GONE);
            isHaveSetEnd = true;
            removeStartMarker();
            mStartFloorId = 0;
            mStartLongtitude = 0;
            mStartLatitude = 0;
            type = 3;
            mapStatus = true;
        } else if (types == STARTSELEE_SHOW_04) {
            //标题栏
            title_rr.setVisibility(View.GONE);
            //搜索框
            search_rr.setVisibility(View.GONE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.VISIBLE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.VISIBLE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.GONE);
            isHaveSetEnd = true;
            type = 4;
            mapStatus = true;
        } else if (types == ROUTE_SHOW_05) {
            //标题栏
            title_rr.setVisibility(View.GONE);
            //搜索框
            search_rr.setVisibility(View.GONE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.VISIBLE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.VISIBLE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.GONE);
            isHaveSetEnd = true;
            type = 5;
            mapStatus = false;
        } else if (types == NAVIGA_SHOW_06) {
            //标题栏
            title_rr.setVisibility(View.GONE);
            //搜索框
            search_rr.setVisibility(View.GONE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.VISIBLE);
            //导航结束
            stop_nagv_btn.setVisibility(View.GONE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.GONE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.VISIBLE);
            isHaveSetEnd = true;
            type = 6;
            mapStatus = false;
        } else if (types == STOPNAVIGA_SHOW_07) {
            //标题栏
            title_rr.setVisibility(View.GONE);
            //搜索框
            search_rr.setVisibility(View.GONE);
            //终点选择
            park_zhongdian.setVisibility(View.GONE);
            //选择起点2（底部）
            setstar_bttom_ll01.setVisibility(View.GONE);
            //路线规划
            luxianguihua_rr.setVisibility(View.GONE);
            //导航中
            nagv_01_rr.setVisibility(View.GONE);
            //导航结束
            stop_nagv_btn.setVisibility(View.VISIBLE);

            //顶部显示
            //选择起点
            selectstart_rr_01.setVisibility(View.GONE);
            //顶部导航提示01
            nagv_top01.setVisibility(View.VISIBLE);
            isHaveSetEnd = true;
            type = 7;
            mapStatus = false;
        }

    }

    ;

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
        RouteManager.get().showNaviRoute(mCurrentFloorId);
    }

    /**
     * 打终点标记
     *
     * @param latLng
     */
    private void addEndMarker(LatLng latLng) {
        Log.e("zyy", "addEndMarker: ");
        if (mMapboxMap.getLayer(Config.LAYERID_FACILITY) != null) {
            mMarkerUtils.addEndMark(latLng, Config.LAYERID_FACILITY);
        } else {
            mMarkerUtils.addEndMark(latLng, Config.LAYERID_AREA_TEXT);
        }
    }

    /**
     * 打起点标记
     *
     * @param latLng
     */
    private void addStartMarker(LatLng latLng) {
        if (mMapboxMap.getLayer(Config.LAYERID_FACILITY) != null) {
            mMarkerUtils.addStartMark(latLng, Config.LAYERID_FACILITY);
        } else {
            mMarkerUtils.addStartMark(latLng, Config.LAYERID_AREA_LINE);
        }
    }

    /**
     * 去除起点标记,只是界面上的改变
     */
    private void removeStartMarker() {
        Log.e("zyy", "removeStartMarker:");
        mMarkerUtils.removeStartMark();
    }

    /**
     * 去除终点标记,,只是界面上的改变
     */
    private void removeEndMarker() {
        Log.e("zyy", "removeEndMarker: ");
        mMarkerUtils.removeEndMark();
    }


    //居中toast
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

    // 跳转到搜索界面 搜索终点
    private void searchEndPoi(){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constant.SEATCHTYPE_KEY, SEARCH_END); // 选择终点
        startActivityForResult(intent, Constant.END_REQUESTCODE);
    }

    // 搜索起点
    private void searchStartPoi(){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constant.SEATCHTYPE_KEY, SEARCH_START); // 选择起点
        startActivityForResult(intent, Constant.START_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == Constant.END_REQUESTCODE) { // 终点
                if (resultCode == Constant.GOWITHME_RESULTCODE){
                    // 带我去
                    MapPointInfoBean mapPointInfoBean = (MapPointInfoBean) data.getSerializableExtra("MapPointInfoBean");
                    Log.i("map", "onActivityResult: 带我去" + mapPointInfoBean.getName());

                } else if(resultCode == Constant.LOOKMAP_RESULTCODE){
                    // 看地图
                    MapPointInfoBean mapPointInfoBean = (MapPointInfoBean) data.getSerializableExtra("MapPointInfoBean");
                    Log.i("map", "onActivityResult: 看地图" + mapPointInfoBean.getName());
                }
            } else if(requestCode == Constant.START_REQUESTCODE) { // 起点
                if (resultCode == Constant.START_RESULTCODE){
                    // 设为起点
                    MapPointInfoBean mapPointInfoBean = (MapPointInfoBean) data.getSerializableExtra("MapPointInfoBean");
                    Log.i("map", "onActivityResult: 设为起点" + mapPointInfoBean.getName());
                }
            }
        }
    }

}
