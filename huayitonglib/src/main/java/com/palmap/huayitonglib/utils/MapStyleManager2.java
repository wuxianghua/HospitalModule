package com.palmap.huayitonglib.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonPrimitive;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by wtm on 2017/8/21.
 * <p>
 * 尝试用分别加载 2Dheight 和 3dHeight 的方式来加载2D和3D地图
 */
public class MapStyleManager2 {

    private String styleJson = null;

    private JSONObject styleJsonObj = null;

    public MapStyleManager2(String styleJson) {
        this.styleJson = styleJson;
        try {
            this.styleJsonObj = new JSONObject(this.styleJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void attachStyle(FeatureCollection featureCollection, String layerName) {
        if (featureCollection == null || this.styleJsonObj == null) {
            return;
        }
        try {
            JSONObject layerStyle = styleJsonObj.getJSONObject("default")
                    .getJSONObject("Area");

            Log.d("mapbox", "layerStyle: " + layerStyle.toString());
            JSONObject defaultFace = layerStyle.getJSONObject("renderer")
                    .getJSONObject("default").getJSONObject("face");

            JSONObject defaultOutline = layerStyle.getJSONObject("renderer")
                    .getJSONObject("default").getJSONObject("outline");
            final double defaultHeight = defaultFace.getDouble("height");
            // TODO 2D 和 3D 高度
            final double default2DHeight = defaultFace.getDouble("2DHeight");
            final double default3DHeight = defaultFace.getDouble("3DHeight");
            // 线的高度
            final double defaultLineHeight = defaultFace.getDouble("lineHeight");

            final String defaultColor = defaultFace.getString("color").replace("0x", "#");
            final String defaultOutlineColor = defaultOutline.getString("color");
            final double defaultOpacity = defaultFace.getDouble("opacity");
            JSONObject configStyle = layerStyle.getJSONObject("renderer")
                    .getJSONObject("styles");
            // 匹配样式的匹配字段（优先级）
            JSONArray matchKeys = layerStyle.getJSONObject("renderer").getJSONArray("keys");
            for (Feature feature : featureCollection.getFeatures()) {
                feature.addProperty("height", new JsonPrimitive(defaultHeight));
                feature.addProperty("2DHeight", new JsonPrimitive(default2DHeight));
                feature.addProperty("3DHeight", new JsonPrimitive(default3DHeight));
                feature.addProperty("lineHeight", new JsonPrimitive(defaultLineHeight));
                feature.addProperty("color", new JsonPrimitive(defaultColor));
                feature.addProperty("opacity", new JsonPrimitive(defaultOpacity));
                feature.addProperty("outLineColor", new JsonPrimitive(defaultOutlineColor));
                String category = feature.getStringProperty("category");

                Iterator<String> keys = configStyle.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    // 遍历匹配
                    if (matchKeys != null && matchKeys.length() > 0) {
                        for (int k = 0; k < matchKeys.length(); k++) {
                            String feaKey = (String) matchKeys.get(k);
                            if (feature.hasProperty(feaKey)) {
                                String value = feature.getStringProperty(feaKey);
                                if (Pattern.matches(key, value)) {
                                    matchFratureProperty(key, configStyle, feature);
                                }
                            }
                        }
                    }


//                    if (Pattern.matches(key, category)) {
//                        matchFratureProperty(key,configStyle,feature);
//                    }
//                    if (feature.hasProperty("address")){
//                        String address = feature.getStringProperty("address");
//                        if (Pattern.matches(key, address)){
//                            matchFratureProperty(key,configStyle,feature);
//                        }
//                    }
                }

                attachStyleByColorId(feature);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attachStyleByColorId(Feature feature) {
        if (feature.hasProperty("colorId")) {
            int colorId = feature.getNumberProperty("colorId").intValue();
            Log.d(TAG, "attach:  colorId " + colorId);
            switch (colorId) {
                case CANTING:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_CANTING));
                    break;
                case LOUDONG:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_LOUDONG));
                    break;
                case LUOJIFENQU:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_LUOJIFENQU));
                    break;
                case ZHENSHIJIYILIAOXIANGUAN:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle
                            .COLOR_ZHENSHIJIYILIAOXIANGUAN));
                    feature.addProperty(NAME_CLICKABLE, new JsonPrimitive(true));
                    break;
                case YISHENGBANGONGSHIJIBANGONGRENYUANXIANGGUAN:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle
                            .COLOR_YISHENGBANGONGSHIJIBANGONGRENYUANXIANGGUAN));
                    break;
                case GUAHAOSHOUFEI:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_GUAHAOSHOUFEI));
                    feature.addProperty(NAME_CLICKABLE, new JsonPrimitive(true));
                    break;
                case LUMINGPOI:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_LUMINGPOI));
                    feature.addProperty(NAME_BORDER_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_LUMINGPOI));
                    break;
                case TINGCHEWEI:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_TINGCHEWEI));
                    break;
                case SHINEISHESHI:
                    feature.addProperty(NAME_COLOR, new JsonPrimitive(HuaxiMapStyle.COLOR_SHINEISHESHI));
                    break;
                default:
                    break;
            }
        }
    }

    // 填充属性
    public void matchFratureProperty(String key, JSONObject configStyle, Feature feature) {
        try {
            JSONObject temp = configStyle.getJSONObject(key);
            JSONObject tempFace = temp.optJSONObject("face");
            JSONObject tempOutline = temp.optJSONObject("outline");

            if (tempFace != null) {
                // 设置高度
                double tempHeight = tempFace.optDouble("height", 0);
                if (tempHeight != 0) {
                    feature.addProperty("height", new JsonPrimitive(tempHeight));
                }
                // 设置2D和3D高度
                double temp2DHeight = tempFace.optDouble("2DHeight", 0);
                double temp3DHeight = tempFace.optDouble("3DHeight", 0);
                if (temp2DHeight != 0) {
                    feature.addProperty("2DHeight", new JsonPrimitive(temp2DHeight));
                }
                if (temp3DHeight != 0) {
                    feature.addProperty("3DHeight", new JsonPrimitive(temp3DHeight));
                }
                // 设置线的高度
                double tempLineHeight = tempFace.optDouble("lineHeight", 0);
                if (tempLineHeight != 0) {
                    feature.addProperty("lineHeight", new JsonPrimitive(tempLineHeight));
                }

                String tempFaceColor = tempFace.optString("color");
                if (!TextUtils.isEmpty(tempFaceColor)) {
                    feature.addProperty("color", new JsonPrimitive(
                            tempFaceColor.replace("0x", "#")
                    ));
                }
                String tempOpacity = tempFace.optString("opacity");
                if (!TextUtils.isEmpty(tempOpacity)) {
                    feature.addProperty("opacity", new JsonPrimitive(tempOpacity));
                }
            }
            if (tempOutline != null) {
                String tempOutLineColor = tempOutline.optString("color");
                if (!TextUtils.isEmpty(tempOutLineColor)) {
                    feature.addProperty("outLineColor", new JsonPrimitive(tempOutLineColor.replace("0x", "#")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String TAG = MapStyleManager2.class.getSimpleName();

    public static final String NAME_COLOR = "color";
    public static final String NAME_BORDER_COLOR = "huaxi_border_color";
    public static final String NAME_CLICKABLE = "clickable";

    public static final int CANTING = 1;//餐厅
    public static final int LOUDONG = 2;//楼栋
    public static final int LUOJIFENQU = 3;//逻辑分区
    public static final int ZHENSHIJIYILIAOXIANGUAN = 4;//诊室及医疗相关
    public static final int YISHENGBANGONGSHIJIBANGONGRENYUANXIANGGUAN = 5;//医生办公室及办公人员相关
    public static final int GUAHAOSHOUFEI = 6;//挂号收费
    public static final int LUMINGPOI = 7;//路名poi
    public static final int TINGCHEWEI = 9;//停车位
    public static final int SHINEISHESHI = 10;//室内设施

}