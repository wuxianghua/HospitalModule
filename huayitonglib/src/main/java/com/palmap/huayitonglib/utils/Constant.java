package com.palmap.huayitonglib.utils;

import android.os.Environment;


public class Constant {

    public static final String APP_KEY = "pk" +
            ".eyJ1IjoiY2FtbWFjZSIsImEiOiJjaW9vbGtydnQwMDAwdmRrcWlpdDVoM3pjIn0.Oy_gHelWnV12kJxHQWV7XQ";
    public final static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MapboxData/";

    public static final Integer[] poiId20 = {2696948, 2698582, 2699405, 2696979, 2696938, 2696938, 2454194, 2454194,
            2454207, 2454122, 2455098, 2455098, 2453350, 2454109, 2454206, 2453456, 2454153, 2454215,
            2452817, 2697091, 2697245, 2697256, 2697168, 2452827, 2697451, 2452907, 2452918, 2697277,
            2697289, 2698503, 2699176, 2697091, 2453409, 2696949, 2699174, 2699417};
    public static final Integer[] poiId17 = {2571212, 2699564, 2700391};

    // 以下是地图界面和搜索界面交互相关参数
    public static final String SEATCHTYPE_KEY = "search_tyle";
    public static final String SEATCH_KEYWORD = "search_keyWord";
    public static final int END_REQUESTCODE = 1000;
    public static final int START_REQUESTCODE = 2000;
    public static final int GOWITHME_RESULTCODE = 3000;
    public static final int LOOKMAP_RESULTCODE = 4000;
    public static final int START_RESULTCODE = 5000;

}
