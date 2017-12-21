package com.palmap.huayitonglib;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.palmap.huayitonglib.db.bridge.DbConnectionManager;

/**
 * Created by hengwei.guo on 2017/12/20 11:25.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
        initSpeechSDK();
    }

    /**
     * 初始化数据库
     */
    private void initDataBase() {
        DbConnectionManager.get().init(this);
    }

    /**
     * 初始化语音SDK
     */
    private void initSpeechSDK() {
        // TODO: 2017/12/21 改成接口
        SpeechUtility.createUtility(this, "appid=" + BuildConfig.APP_ID);
    }

}
