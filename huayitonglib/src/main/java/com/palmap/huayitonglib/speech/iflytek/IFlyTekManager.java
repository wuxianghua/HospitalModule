package com.palmap.huayitonglib.speech.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.palmap.huayitonglib.speech.ISpeechManager;

/**
 * Created by yangjinhuang on 2017/12/21
 */

public class IFlyTekManager implements ISpeechManager {

    public enum Speaker {
        PUTONG_M,   //普通话（男）
        PUTONG_F,   //普通话（女）
        SICHUAN_F   //四川话（女）
        ;

        @Override
        public String toString() {
            switch (this) {
                case PUTONG_F:
                    return "xiaoyan";
                case PUTONG_M:
                    return "xiaoyu";
                case SICHUAN_F:
                    return "xiaorong";
                default:
                    return "xiaoyan";
            }
        }

        public String getName() {
            switch (this) {
                case PUTONG_F:
                    return "普通话（女）";
                case PUTONG_M:
                    return "普通话（男）";
                case SICHUAN_F:
                    return "四川话（女）";
                default:
                    return "普通话（女";
            }
        }
    }

    private static String TAG = IFlyTekManager.class.getSimpleName();

    private SpeechSynthesizer mTts;         //语音合成对象
    private String mEngineType = SpeechConstant.TYPE_CLOUD; //引擎类型
    private String mVoicer = Speaker.PUTONG_F.toString();     //默认发音人
    private int mSpeechSpeed = PARAM.DEFAULT_SPEED;
    private int mSpeechPitch = PARAM.DEFAULT_PITCH;
    private int mSpeechVolume = PARAM.DEFAULT_VOLUME;

    //    private int mPercentForBuffering = 0;   //缓冲进度
    //    private int mPercentForPlaying = 0;     //播放进度
    private SynthesizerListener mTtsListener;   //合成回调监听
    private OnSpeechListener mOnSpeechListener; //播放监听

    public IFlyTekManager(Context context, final OnInitListener onInitListener) {
        mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    onInitListener.onInitFailure("初始化失败,错误码：" + code);
                    Log.e(TAG, "初始化失败,错误码：" + code);
                } else {
                    onInitListener.onInitSuccess();
                }
            }
        });
        initParam();
    }

    private void initParam() {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, String.valueOf(mSpeechSpeed));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, String.valueOf(mSpeechPitch));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, String.valueOf(mSpeechVolume));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    @Override
    public void setSpeechListener(OnSpeechListener listener) {
        mOnSpeechListener = listener;
    }

    @Override
    public void resetParam() {
        if (mTts == null || mTts.isSpeaking()) {
            return;
        }
        initParam();
    }

    @Override
    public void setSpeechEngineType(EngineType type) {
        if (type == EngineType.LOCAL) {
            mEngineType = SpeechConstant.TYPE_LOCAL;
        } else {
            mEngineType = SpeechConstant.TYPE_CLOUD;
        }
    }

    @Override
    public void setSpeechSpeaker(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        mVoicer = name;
        mTts.setParameter(SpeechConstant.VOICE_NAME, mVoicer);
    }

    @Override
    public void setSpeechSpeed(int speed) {
        mSpeechSpeed = speed < 0 || speed > 100 ? PARAM.DEFAULT_SPEED : speed;
        mTts.setParameter(SpeechConstant.SPEED, String.valueOf(mSpeechSpeed));
    }

    @Override
    public void setSpeechPitch(int pitch) {
        mSpeechPitch = pitch < 0 || pitch > 100 ? PARAM.DEFAULT_PITCH : pitch;
        mTts.setParameter(SpeechConstant.PITCH, String.valueOf(mSpeechPitch));
    }

    @Override
    public void setSpeechVolume(int volume) {
        mSpeechVolume = volume < 0 || volume > 100 ? PARAM.DEFAULT_VOLUME : volume;
        mTts.setParameter(SpeechConstant.VOLUME, String.valueOf(mSpeechVolume));
    }

    @Override
    public boolean startSpeaking(String msg) throws IllegalAccessException {
        if (mTts == null) {
            throw new IllegalArgumentException("not init data");
        } else {
            if (TextUtils.isEmpty(msg)) {
                return true;
            }
            if (mTts.isSpeaking()) {
                mTts.stopSpeaking();
            }
            if (mTts.startSpeaking(msg, getTtsListener()) == ErrorCode.SUCCESS) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void pauseSpeaking() {
        if (mTts != null) {
            mTts.pauseSpeaking();
        }
    }

    @Override
    public void resumeSpeaking() {
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }

    @Override
    public void stopSpeaking() {
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }

    @Override
    public boolean isSpeaking() {
        return mTts != null && mTts.isSpeaking();
    }

    @Override
    public void destroy() {
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    private SynthesizerListener getTtsListener() {
        if (mTtsListener == null) {
            mTtsListener = new SynthesizerListener() {
                @Override
                public void onSpeakBegin() {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onSpeakBegin();
                    }
                }

                @Override
                public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onBufferProgress(percent, beginPos, endPos, info);
                    }
                }

                @Override
                public void onSpeakPaused() {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onSpeakPaused();
                    }
                }

                @Override
                public void onSpeakResumed() {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onSpeakResumed();
                    }
                }

                @Override
                public void onSpeakProgress(int percent, int beginPos, int endPos) {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onSpeakProgress(percent, beginPos, endPos);
                    }
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if (mOnSpeechListener != null) {
                        mOnSpeechListener.onCompleted();
                    }
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            };
        }
        return mTtsListener;
    }

}
