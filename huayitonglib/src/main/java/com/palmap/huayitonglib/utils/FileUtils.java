package com.palmap.huayitonglib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.bean.FloorBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by yibo.liu on 2017/10/16 20:08.
 */
public class FileUtils {
    public static final String TAG = FileUtils.class.getSimpleName();

    public static HashMap<Integer, String> mIdAHashMap = new HashMap<>();
    public static HashMap<String, Integer> mAliasHashMap = new HashMap<>();
    public static HashMap<String, String> mFloorNameHashMap = new HashMap<>();

    /**
     * loads in GeoJSON files from the assets folder.
     * Either use the method provided below or your preferred way of loading in a JSON file.
     *
     * @param context
     * @param filename
     * @return
     */
    public static String loadFromAssets(Context context, String filename) {
        InputStream is = null;
        try {
            is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //读取SD卡中文件的方法
    public static String loadFromSD(Context context, String filename) {
        try {
            StringBuilder sb = new StringBuilder("");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //打开文件输入流
                File newfile = new File(Constant.PATH + filename);
                Uri contentUri = null;
                if (Build.VERSION.SDK_INT > 23) {
                    /**Android 7.0以上的方式**/
                    contentUri = getUriForFile(context, context.getString(R.string.package_name), newfile);
                    context.grantUriPermission(context.getPackageName(), contentUri, Intent
                            .FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                FileInputStream input = new FileInputStream(newfile);
                byte[] temp = new byte[1024];

                int len = 0;
                //读取文件内容:
                while ((len = input.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                input.close();
            }
            return sb.toString();


        } catch (Exception e) {
            Log.e(TAG, "loadFromSD: ", e);
            return null;
        }
    }

    public static int getFloorId(Context context, String alias) {

        try {
            String floorsinfo = FileUtils.loadFromAssets(context, "floors.json");
            JSONObject jsonObject = new JSONObject(floorsinfo);
            JSONArray floors = jsonObject.getJSONArray("floors");
            for (int i = 0; i < floors.length(); i++) {
                JSONObject jsonObject1 = floors.getJSONObject(i);
                String alias1 = jsonObject1.getString("alias");
                int floorId1 = jsonObject1.getInt("floorId");
                if (TextUtils.equals(alias, alias1)) {
                    return floorId1;
                }
            }
        } catch (Exception e) {

        }
        return 2452754;
    }

    public static String getFloorName(Context context, String alias) {

        try {
            String floorsinfo = FileUtils.loadFromAssets(context, "floors.json");
            JSONObject jsonObject = new JSONObject(floorsinfo);
            JSONArray floors = jsonObject.getJSONArray("floors");
            for (int i = 0; i < floors.length(); i++) {
                JSONObject jsonObject1 = floors.getJSONObject(i);
                String alias1 = jsonObject1.getString("alias");
                int floorId1 = jsonObject1.getInt("floorId");
                String floorName = jsonObject1.getString("floorName");
                if (TextUtils.equals(alias, alias1)) {
                    return floorName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";


//        if (!mIdAHashMap.containsValue(alias)) {
//            getFloorAlias(context, -222);
//        }
//
//        String floorName = mFloorNameHashMap.get(alias);
//
//        return floorName;
    }

    public static FloorBean getSourceName(String alias) {

        FloorBean floorBean = new FloorBean();
        floorBean.setAreaFilename(alias + "_area.geojson");
        floorBean.setFrameFilename(alias + "_frame.geojson");
        floorBean.setFacilityFilename(alias + "_facility.geojson");

        return floorBean;
    }

    public static FloorBean getSourceName(Context context, int floorId) {
        String floorAlias = getFloorAlias(context, floorId);
        FloorBean floorBean = getSourceName(floorAlias);
        return floorBean;
    }

    public static String getFloorAlias(Context context, int floorId) {
        try {

            if (mIdAHashMap.containsKey(floorId)) {
                return mIdAHashMap.get(floorId);
            }

            String floorsinfo = FileUtils.loadFromAssets(context, "floors.json");
            JSONObject jsonObject = new JSONObject(floorsinfo);
            JSONArray floors = jsonObject.getJSONArray("floors");
            for (int i = 0; i < floors.length(); i++) {
                JSONObject jsonObject1 = floors.getJSONObject(i);
                String alias = jsonObject1.getString("alias");
                int floorId1 = jsonObject1.getInt("floorId");
                String floorName = jsonObject1.getString("floorName");
                mIdAHashMap.put(floorId1, alias);
                mAliasHashMap.put(alias, floorId1);
                mFloorNameHashMap.put(alias, floorName);
            }

            if (mIdAHashMap.containsKey(floorId)) {
                return mIdAHashMap.get(floorId);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param fileName
     * @param json
     */
    public static void writeToFile(String fileName, String json) {
        try {
            Log.d(TAG, "writeToFile: " + fileName);
            File file = new File(Constant.PATH);
            if (!file.exists()) {
                file.mkdirs();
            }

            File newfile = new File(Constant.PATH + fileName);
            if (!newfile.exists()) {
                file.mkdir();
            }
            FileWriter fileWriter = new FileWriter(newfile);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: 出错了!!!" + e.toString());
        }
    }
}
