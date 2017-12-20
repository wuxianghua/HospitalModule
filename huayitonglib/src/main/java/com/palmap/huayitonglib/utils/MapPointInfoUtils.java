package com.palmap.huayitonglib.utils;

import android.util.Log;

import com.palmap.huayitonglib.db.entity.MapPointInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hengwei.guo on 2017/12/20 11:11.
 */

public class MapPointInfoUtils {

    // 从json文件中解析成数据库需要的字段
    public static List<MapPointInfoBean> getSearchPoiData(String jsonData){
        List<MapPointInfoBean> list = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(jsonData);
            if (array != null && array.length() != 0) {
                Log.i("dList", "onCreate:-------- array.size()---------" + array.length());
                for (int i = 0; i < array.length(); i++) {
                    Log.i("jsonElements", "onCreate:-------- " + array.get(0).toString());
                    JSONObject jsonObject = array.getJSONObject(i);
                    MapPointInfoBean mapBean = new MapPointInfoBean();
                    mapBean.setPoiId(jsonObject.getString("poiId"));
                    mapBean.setFloorName(jsonObject.getString("floorName"));
                    mapBean.setName(jsonObject.getString("name"));
                    mapBean.setLongitude(jsonObject.getString("longitude"));
                    mapBean.setLatitude(jsonObject.getString("latitude"));
                    mapBean.setFloorId(jsonObject.getString("floorId"));
                    mapBean.setPoint(jsonObject.getString("point"));
                    mapBean.setAddress(jsonObject.getString("address"));
                    list.add(mapBean);
                }
                return list;
            } else {
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }

    }
}
