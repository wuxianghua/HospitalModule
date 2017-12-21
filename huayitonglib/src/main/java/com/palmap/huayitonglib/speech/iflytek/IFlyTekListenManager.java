package com.palmap.huayitonglib.speech.iflytek;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.palmap.huayitonglib.speech.IListenManger;

/**
 * Created by yangjinhuang on 2017/12/21
 */
// TODO: 2017/12/21 以后再实现
public class IFlyTekListenManager implements IListenManger {

//        public enum Language {
//            CHINESE,   //普通话（男）
//            ENGLISH,   //普通话（女）
//            Cantonese   //粤语（女）
//            ;
//
//            @Override
//            public String toString() {
//                switch (this) {
//                    case PUTONG_F:
//                        return "xiaoyan";
//                    case PUTONG_M:
//                        return "xiaoyu";
//                    case SICHUAN_F:
//                        return "xiaorong";
//                    default:
//                        return "xiaoyan";
//                }
//            }
//
//            public String getName() {
//                switch (this) {
//                    case PUTONG_F:
//                        return "普通话（女）";
//                    case PUTONG_M:
//                        return "普通话（男）";
//                    case SICHUAN_F:
//                        return "四川话（女）";
//                    default:
//                        return "普通话（女";
//                }
//            }
//        }

    private static String TAG = IFlyTekListenManager.class.getSimpleName();

    private SpeechRecognizer mIat;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;     //引擎类型

    public IFlyTekListenManager(Context context, final OnInitListener listener) {
        mIat = SpeechRecognizer.createRecognizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    if (listener != null) {
                        listener.onInitFailure("初始化失败,错误码：" + code);
                        Log.e(TAG, "初始化失败,错误码：" + code);
                    }
                } else {
                    if (listener != null) {
                        listener.onInitSuccess();
                    }
                }
            }
        });
        initParam();
    }

    private void initParam() {
//        mIat.setParameter(SpeechConstant.PARAMS, null);
//        // 设置听写引擎
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//        // 设置返回结果格式
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
//        if (lag.equals("en_us")) {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//            mIat.setParameter(SpeechConstant.ACCENT, null);
//
//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
//            }
//        } else {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
//
//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
//            }
//        }
//
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
//
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
//
//        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
//
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    @Override
    public void resetParam() {
        initParam();
    }

    @Override
    public void destroy() {
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

}
