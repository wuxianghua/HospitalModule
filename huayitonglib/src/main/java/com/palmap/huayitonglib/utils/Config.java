package com.palmap.huayitonglib.utils;

import android.graphics.Color;

/**
 * Created by yibo.liu on 2017/10/17 19:40.
 */

public class Config {

    public static final int COLOR_FRAME = Color.parseColor("#D1D1D1");
    public static final int COLOR_AREA = Color.parseColor("#F5F5F5");

    public static final int COLOR_AREA_ORIGINAL = Color.GRAY;//初始颜色
    public static final int COLOR_AREA_CLICK = Color.parseColor("#00a4a4");//选中色
//    public static final int COLOR_AREA_NOCHANGE = Color.parseColor("#ADFFBB");
//    public static final int COLOR_AREA_NAMECHANGE = Color.parseColor("#FFC6BA");
//    public static final int COLOR_AREA_GRAPHCHANGE = Color.parseColor("#8CCEEF");

    public static final int COLOR_AREA_NOCHANGE = Color.parseColor("#91d100");//未变更
    public static final int COLOR_AREA_NAMECHANGE = Color.parseColor("#ff2e12");//名称变更
    public static final int COLOR_AREA_GRAPHCHANGE = Color.parseColor("#1faeff");//图片变更

    public static final String NAME_DISPLAY = "display";
    public static final String LAYERID_BACKGROUND = "backgroundlayer";

    public static final String LAYERID_FRAME = "LAYERID_FRAME";
    public static final String LAYERID_FRAME_LINE = "LAYERID_FRAME_LINE";
    public static final String LAYERID_AREA = "LAYERID_AREA";
    public static final String LAYERID_AREA_LINE = "LAYERID_AREA_LINE";
    public static final String LAYERID_AREA_TEXT = "LAYERID_AREA_TEXT";
    public static final String LAYERID_FACILITY = "LAYERID_FACILITY";

    public static final String SOUCEID_FRAME = "SOUCEID_FRAME";
    public static final String SOUCEID_AREA = "SOUCEID_AREA";
    public static final String SOUCEID_FACILITY = "SOUCEID_FACILITY";

    //华为停车场
//    public static double CENTER_LAT = 22.644;//-上 + 下
//    public static double CENTER_LNG = 114.06;// - 右  +左

    //华为停车场
//    public static final String FILENAME_FRAME = "c_hw_frame.geojson";
//    public static final String FILENAME_AREA = "c_hw_area.geojson";
//    public static final String FILENAME_FACILITY = "c_hw_facility.geojson";

    //华西医院
//    public static double CENTER_LAT = 30.6417033;//-上 + 下
//    public static double CENTER_LNG = 104.0612228;// - 右  +左

    public static double CENTER_LAT = 31.2268634617768;//-上 + 下
    public static double CENTER_LNG = 121.38649602857757;// - 右  +左
    //华西医院F1
    public static final String FILENAME_FRAME = "F1_frame.geojson";
    public static final String FILENAME_AREA = "F1_area.geojson";
    public static final String FILENAME_FACILITY = "F1_facility.geojson";
    public static final String FILENAME_STYLE = "hxyy_style.json";
    //儿童医院的style
//    public static final String MAPSTYLE_PATH = "Ch_style.json";
    public static final String MAPSTYLE_PATH = "Ch_style_3D_2.json";
//
//    //---对应的儿童医院的楼层id TODO
//    public static final int FLOORID_B1_CH= 0;
//    public static final int FLOORID_F1_CH= 1962509;
//    public static final int FLOORID_F2_CH= 1963071;
//    public static final int FLOORID_F3_CH= 1963404;
//    public static final int FLOORID_F4_CH= 3208951;
//    public static final int FLOORID_F5_CH= 2;



        //---对应的儿童医院的楼层id TODO
    public static final int FLOORID_B2_CH= 2696743;
    public static final int FLOORID_B1_CH= 2696763;
    public static final int FLOORID_F0_CH= 2571204;
    public static final int FLOORID_F1_CH= 2452754;
    public static final int FLOORID_F2_CH= 2453325;
    public static final int FLOORID_F3_CH= 2454077;
    public static final int FLOORID_F4_CH= 2454930;
    public static final int FLOORID_F5_CH= 2700757;
    public static final int FLOORID_F6_CH= 2701555;
    public static final int FLOORID_F7_CH= 2702302;
    public static final int FLOORID_F8_CH= 2703007;
    public static final int FLOORID_F9_CH= 2703596;
    public static final int FLOORID_F10_CH= 2704202;
    public static final int FLOORID_F11_CH= 2704624;
    public static final int FLOORID_F12_CH= 2704905;
    public static final int FLOORID_F13_CH= 2705297;
    public static final int FLOORID_F14_CH= 2705689;
    public static final int FLOORID_F15_CH= 2706052;




    //华西医院F14
//    public static final String FILENAME_FRAME = "F14_frame.geojson";
//    public static final String FILENAME_AREA = "F14_area.geojson";
//    public static final String FILENAME_FACILITY = "F14_facility.geojson";
}
