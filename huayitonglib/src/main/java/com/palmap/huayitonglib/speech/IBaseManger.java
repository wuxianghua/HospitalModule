package com.palmap.huayitonglib.speech;

/**
 * Created by yangjinhuang on 2017/12/21
 */

public interface IBaseManger{

    /**
     * 初始化监听
     */
    interface OnInitListener {

        /**
         * 初始化成功
         * 初始化成功，之后可以调用startSpeaking方法
         */
        void onInitSuccess();

        /**
         * 初始化失败
         *
         * @param msg 失败信息
         */
        void onInitFailure(String msg);

    }

    /**
     * 语音引擎类型
     */
    enum EngineType {
        CLOUD,  //云端
        LOCAL   //本地
    }

    /**
     * 恢复默认参数
     */
    void resetParam();

    /**
     * 释放
     */
    void destroy();

}
