package com.palmap.huayitonglib.activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.palmap.huayitonglib.speech.utils.JsonParser;
import com.palmap.huayitonglib.utils.NetUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by yangjinhuang on 2017/12/21
 */

public abstract class VoiceListenActivity extends AppCompatActivity {

    private static final String TAG = VoiceListenActivity.class.getSimpleName();

    private RecognizerDialog mIatDialog;
    private RecognizerDialogListener mRecognizerDialogListener = null;
    private Handler mMainHandler = null;
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    protected void initVoiceListen() {
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        if (mIatDialog == null) {
            mIatDialog = new RecognizerDialog(this, new InitListener() {
                @Override
                public void onInit(int code) {
                    if (code != ErrorCode.SUCCESS) {
                        Log.e(TAG, "初始化失败，错误码：" + code);
                    } else {
                        resetVoiceParam();
                        mIatDialog.setListener(getDialogListener());
                    }
                }
            });
        }
    }

    protected void showListenDialog() {
        if (!NetUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前没有网络连接哦…", Toast.LENGTH_SHORT).show();
        } else {
            if (mIatDialog == null) {
                initVoiceListen();
            }
            if (mIatDialog != null) {
                mIatDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        try {
                            ViewGroup viewGroup = ((ViewGroup) ((ViewGroup) ((ViewGroup)
                                    ((ViewGroup) ((ViewGroup) (mIatDialog.getWindow().getDecorView())).
                                            getChildAt(0)).getChildAt(0)).getChildAt(0)).getChildAt(0));
                            viewGroup.removeViewAt(4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mIatResults.clear();
                mIatDialog.show();
            }
        }
    }

    protected void resetVoiceParam() {
        // 清空参数
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);
        // 设置语言
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置听写引擎
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    public abstract void handleListenResult(String value);

    public abstract void onListenError();

    private RecognizerDialogListener getDialogListener() {
        if (mRecognizerDialogListener == null) {
            mRecognizerDialogListener = new RecognizerDialogListener() {
                @Override
                public void onResult(RecognizerResult results, boolean b) {
                    String text = JsonParser.parseIatResult(results.getResultString());
                    handleListenResult(text);
                    if (mIatDialog != null) {
                        mIatDialog.dismiss();
                    }
//                    String sn = null;
//                    // 读取json结果中的sn字段
//                    try {
//                        JSONObject resultJson = new JSONObject(results.getResultString());
//                        sn = resultJson.optString("sn");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    mIatResults.put(sn, text);
//                    StringBuilder builder = new StringBuilder();
//                    for (String key : mIatResults.keySet()) {
//                        builder.append(mIatResults.get(key));
//                    }
//                    handleListenResult(builder.toString());
                }

                @Override
                public void onError(SpeechError speechError) {
                    if (mMainHandler != null && mIatDialog != null) {
                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIatDialog.dismiss();
                            }
                        }, 1000);
                    }
                }
            };
        }
        return mRecognizerDialogListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIatDialog != null) {
            mIatDialog.cancel();
            mIatDialog.destroy();
        }
    }

}
