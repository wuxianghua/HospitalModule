package com.palmap.huayitonglib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.adapter.SearchGridViewAdapter;
import com.palmap.huayitonglib.bean.OfficeItemBean;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.utils.DisplayUtil;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.view.ExpandableView;
import com.palmap.huayitonglib.view.ExpandableViewHX;
import com.palmap.huayitonglib.view.NonScrollGridView;
import com.palmap.huayitonglib.view.StreamLableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hengwei.guo on 2017/12/19 11:23.
 */

public class SearchShowFragment extends Fragment {

    private View view;
    private SearchActivity self;
    // F1到F4 出入口
    private ExpandableView mExpandableView_F3,mExpandableView_F2,mExpandableView_F1,mExpandableView_F4,mExpandableView_Exit;
    private NonScrollGridView mGridView;
    private SearchGridViewAdapter mAdapter;
    private List<OfficeItemBean> mList;
    //子控件的的大小
    private ViewGroup.MarginLayoutParams layoutParams;

    private LinearLayout foldAllLin;

    private String[] searchNames = {"急诊","缴费","取药","取报告","挂号","抽血","输液","洗手间"};
    private int[] searchImg = {R.mipmap.ic_search_jizhen,R.mipmap.ic_search_jiaofei,R.mipmap.ic_search_quyao,
            R.mipmap.ic_search_qubaogao,R.mipmap.ic_search_guahao,R.mipmap.ic_search_chouxue,
            R.mipmap.ic_search_shuye,R.mipmap.ic_search_xishoujian};
    private String[] F2_A1_Content = {"耳鼻咽喉","头颈外科"};
    private String[] F2_A2_Content = {"皮肤性病科门诊","皮肤科相关的检查"};
    private String[] F2_A3_Content = {"耳鼻喉科相关检查","财务收费窗口"};
    private String[] F2_B_Content = {"藏区医疗服务部","激光科治疗室","遗传咨询门诊","普外科","财务收费窗口"};
    private String[] F2_E_Content = {"骨科","美容整形-烧伤外科","康复医学科","财务收费窗口"};
    private String[] F2_F_Content = {"综合服务窗口","114预约取号处"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_show,container,false);
        self = (SearchActivity) getActivity();
        initView();
        initData();
        return view;
    }

    /** StreamLableLayout点击事件 */
    class TextOnclickListener implements View.OnClickListener{
        private TextView mTextView;
        private String text;
        List<MapPointInfoBean> list = new ArrayList<>();

        public TextOnclickListener(String secondary, List<MapPointInfoBean> list){
            this.list = list;
            this.text = secondary;
        }

        @Override
        public void onClick(View view) {
            if (list.size()!=0){
                self.search(text,list);
            } else {
                Toast.makeText(getContext(),"该科室暂无数据",Toast.LENGTH_SHORT).show();
            }

        }
    }
    
    // gridview的点击事件
    class mOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String str = "";
            str = mList.get(i).getTitle();
            self.setEditText(str);
        }
    }

    private void initView() {
        layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DisplayUtil.dip2px(10), 0, 0, DisplayUtil.dip2px(12));// 设置边距
        layoutParams.height = DisplayUtil.dip2px((float) 28);

        mGridView = view.findViewById(R.id.gridView);
        mList = new ArrayList<>();
        mAdapter = new SearchGridViewAdapter(getContext(),mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new mOnItemClickListener());

//        mExpandableView_F1 = view.findViewById(R.id.expandableView_F1);
//        mExpandableView_F2 = view.findViewById(R.id.expandableView_F2);
//        mExpandableView_F3 = view.findViewById(R.id.expandableView_F3);
//        mExpandableView_F4 = view.findViewById(R.id.expandableView_F4);
//        mExpandableView_Exit = view.findViewById(R.id.expandableView);

        foldAllLin = view.findViewById(R.id.foldAllLin);
    }

    private void initData() {
        for (int i = 0; i < searchNames.length; i++){
            OfficeItemBean itemBean = new OfficeItemBean(searchNames[i],searchImg[i] + "");
            mList.add(itemBean);
        }
        mAdapter.refresh(mList);

        // 解析数据
        String serachPoiData = FileUtils.loadFromAssets(getContext(),"HXSearchShow.json");
        try {
            JSONArray jsonArray = new JSONArray(serachPoiData);
            if (jsonArray!=null){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String elementary = jsonObject.getString("elementary");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                    foldAllLin.addView(creatExpandableView(getContext(),elementary, jsonArray1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ExpandableViewHX creatExpandableView(Context mContext, String title, JSONArray jsonArray){
        final ExpandableViewHX mExpandableView = new ExpandableViewHX(mContext);
        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_lin.setMargins((int)DisplayUtil.dip2px(0),(int) DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(-5));
        mExpandableView.setLayoutParams(params_lin);
        mExpandableView.setmTitlt(title);
        mExpandableView.setmTitleImage(getResources().getDrawable(R.drawable.bg_rectangle_green));
        mExpandableView.setOrientation(LinearLayout.VERTICAL);
        mExpandableView.addView(CreatLinearLayout(getContext(),jsonArray));
        return mExpandableView;
    }

    public LinearLayout CreatLinearLayout(Context mContext, JSONArray jsonArray){
        final LinearLayout mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_lin.setMargins((int)DisplayUtil.dip2px(0),(int) DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(-5));
        mLinearLayout.setLayoutParams(params_lin);
        mLinearLayout.addView(CreatStreamLableLayout(getContext(),jsonArray));
        return mLinearLayout;
    }

    private StreamLableLayout CreatStreamLableLayout(Context mContext, JSONArray jsonArray){
        final StreamLableLayout mStreamLableLayout = new StreamLableLayout(mContext);
        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_lin.setMargins((int)DisplayUtil.dip2px(5),(int)DisplayUtil.dip2px(2),(int)DisplayUtil.dip2px(15),(int)DisplayUtil.dip2px(0));
        mStreamLableLayout.setLayoutParams(params_lin);
        try{
            for (int j=0;j<jsonArray.length();j++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                String secondary = jsonObject1.getString("secondary");
                JSONArray jsonArray2 = jsonObject1.getJSONArray("data");
                List<MapPointInfoBean> list = new ArrayList<>();
                for (int k=0;k<jsonArray2.length();k++){
                    JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                    MapPointInfoBean mapBean = new MapPointInfoBean();
                    mapBean.setPoiId(jsonObject3.getString("poiId"));
                    mapBean.setFloorName(jsonObject3.getString("floorName"));
                    mapBean.setName(jsonObject3.getString("name"));
                    mapBean.setLongitude(jsonObject3.getString("longitude"));
                    mapBean.setLatitude(jsonObject3.getString("latitude"));
                    mapBean.setFloorId(jsonObject3.getString("floorId"));
                    mapBean.setPoint(jsonObject3.getString("point"));
                    mapBean.setAddress(jsonObject3.getString("address"));
                    list.add(mapBean);
                }
                mStreamLableLayout.addView(CreatTextView(secondary, list),layoutParams);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return mStreamLableLayout;
    }

    private TextView CreatTextView(String secondary, List<MapPointInfoBean> list){
        final TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setPadding((int) DisplayUtil.dip2px(8), 0, (int) DisplayUtil.dip2px(8), 0);
        textView.setTextSize(13);
        textView.setBackgroundResource(R.drawable.bg_history_radiu);
        textView.setText(secondary);
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        textView.setOnClickListener(new TextOnclickListener(secondary,list));
        return textView;
    }


//

//
//    private StreamLableLayout CreatStreamLableLayout(Context mContext, String[] array){
//        final StreamLableLayout mStreamLableLayout = new StreamLableLayout(mContext);
//        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params_lin.setMargins((int)DisplayUtil.dip2px(5),(int)DisplayUtil.dip2px(2),(int)DisplayUtil.dip2px(15),(int)DisplayUtil.dip2px(0));
//        mStreamLableLayout.setLayoutParams(params_lin);
//        for (int i=0; i<array.length;i++){
//            mStreamLableLayout.addView(CreatTextView(array[i]),layoutParams);
//        }
//        return mStreamLableLayout;
//    }
//
//    public LinearLayout CreatLinearLayout(Context mContext, String key, List<LocationMapBean> mList){
//        final LinearLayout mLinearLayout = new LinearLayout(mContext);
//        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params_lin.setMargins((int)DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(0),(int)DisplayUtil.dip2px(-5));
//        mLinearLayout.setLayoutParams(params_lin);
//        mLinearLayout.addView(CreatKeyTextView(mContext,key));
//        mLinearLayout.addView(CreatStreamLableLayout(mContext,mList));
//        return mLinearLayout;
//    }
//
//    private StreamLableLayout CreatStreamLableLayout(Context mContext, List<LocationMapBean> mList){
//        final StreamLableLayout mStreamLableLayout = new StreamLableLayout(mContext);
//        LinearLayout.LayoutParams params_lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params_lin.setMargins((int)DisplayUtil.dip2px(5),(int)DisplayUtil.dip2px(2),(int)DisplayUtil.dip2px(15),(int)DisplayUtil.dip2px(0));
//        mStreamLableLayout.setLayoutParams(params_lin);
//        for (int i=0; i<mList.size();i++){
//            mStreamLableLayout.addView(CreatTextView(mList.get(i)),layoutParams);
//        }
//        return mStreamLableLayout;
//    }
//
//    private TextView CreatKeyTextView(Context mContext, String str){
//        final TextView textView = new TextView(mContext);
//        LinearLayout.LayoutParams params_tv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params_tv.setMargins((int)DisplayUtil.dip2px(15),(int)DisplayUtil.dip2px(6),0,0);
//        textView.setLayoutParams(params_tv);
//        textView.setTextSize(14);
//        textView.setText(str);
//        textView.setTextColor(mContext.getResources().getColor(R.color.text_333));
//        return textView;
//    }
//
//    private TextView CreatTextView(LocationMapBean locationMapBean){
//        final TextView textView = new TextView(getContext());
//        textView.setGravity(Gravity.CENTER);
//        textView.setPadding((int) DisplayUtil.dip2px(8), 0, (int) DisplayUtil.dip2px(8), 0);
//        textView.setTextSize(13);
//        textView.setBackgroundResource(R.drawable.bg_history_radiu);
//        textView.setText(locationMapBean.getName());
//        textView.setTextColor(getResources().getColor(R.color.text_gray));
//        textView.setOnClickListener(new TextOnclickListener(locationMapBean));
//        return textView;
//    }
//
//    private TextView CreatTextView(String text){
//        final TextView textView = new TextView(getContext());
//        textView.setGravity(Gravity.CENTER);
//        textView.setPadding((int) DisplayUtil.dip2px(8), 0, (int) DisplayUtil.dip2px(8), 0);
//        textView.setTextSize(13);
//        textView.setBackgroundResource(R.drawable.bg_history_radiu);
//        textView.setText(text);
//        textView.setTextColor(getResources().getColor(R.color.text_gray));
//        textView.setOnClickListener(new TextOnclickListener(textView));
//        return textView;
//    }
}
