package com.palmap.huayitonglib.speech;

/**
 * Created by yangjinhuang on 2017/12/21
 */

public interface ISpeechManager extends IBaseManger {

    /**
     * 语音合成监听
     */
    interface OnSpeechListener {

        /**
         * 开始播放
         */
        void onSpeakBegin();

        /**
         * 暂停播放
         */
        void onSpeakPaused();

        /**
         * 继续播放
         */
        void onSpeakResumed();

        /**
         * 缓存进度
         *
         * @param percent
         * @param beginPos
         * @param endPos
         * @param info
         */
        void onBufferProgress(int percent, int beginPos, int endPos, String info);

        /**
         * 播放进度
         *
         * @param percent
         * @param beginPos
         * @param endPos
         */
        void onSpeakProgress(int percent, int beginPos, int endPos);

        /**
         * 播放完成
         */
        void onCompleted();
    }

    /**
     * 默认参数
     */
    interface PARAM {
        int DEFAULT_SPEED = 50;     //默认语速
        int DEFAULT_PITCH = 50;     //默认语调
        int DEFAULT_VOLUME = 50;    //默认声音
    }

    void setSpeechListener(OnSpeechListener listener);

    /**
     * 设置语音合成引擎类型
     *
     * @param type 引擎类型
     */
    void setSpeechEngineType(EngineType type);

    /**
     * 设置语音合成发音人
     *
     * @param name 发音人名称
     */
    void setSpeechSpeaker(String name);

    /**
     * 设置语速（0-100）
     *
     * @param speed 语速
     */
    void setSpeechSpeed(int speed);

    /**
     * 设置语音语调（0-100）
     *
     * @param pitch 语调
     */
    void setSpeechPitch(int pitch);

    /**
     * 设置语音音量（0-100）
     *
     * @param volume 音量
     */
    void setSpeechVolume(int volume);

    /**
     * 开始播放语音
     *
     * @param msg 语音信息
     * @return 是否合成成功
     */
    boolean startSpeaking(String msg) throws IllegalAccessException;

    /**
     * 暂停播放
     */
    void pauseSpeaking();

    /**
     * 恢复播放
     */
    void resumeSpeaking();

    /**
     * 停止播放
     */
    void stopSpeaking();

    /**
     * 是否正在播放
     *
     * @return 是否在播放
     */
    boolean isSpeaking();

}
