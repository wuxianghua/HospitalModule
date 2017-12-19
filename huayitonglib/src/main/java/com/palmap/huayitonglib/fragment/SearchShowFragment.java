package com.palmap.huayitonglib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.adapter.SearchGridViewAdapter;
import com.palmap.huayitonglib.bean.OfficeItemBean;
import com.palmap.huayitonglib.view.NonScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hengwei.guo on 2017/12/19 11:23.
 */

public class SearchShowFragment extends Fragment {

    private View view;
    private SearchActivity self;

    private NonScrollGridView mGridView;
    private SearchGridViewAdapter mAdapter;
    private List<OfficeItemBean> mList;
    private String[] searchNames = {"急诊","缴费","取药","取报告","自助挂号","抽血","输液","洗手间"};
    private int[] searchImg = {R.mipmap.ic_search_jizhen,R.mipmap.ic_search_jiaofei,R.mipmap.ic_search_quyao,
            R.mipmap.ic_search_jizhen,R.mipmap.ic_search_jizhen,R.mipmap.ic_search_jizhen,
            R.mipmap.ic_search_jizhen,R.mipmap.ic_search_xishoujian};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_show,container,false);
        self = (SearchActivity) getActivity();
        initView();
        return view;
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
        mGridView = view.findViewById(R.id.gridView);
        mList = new ArrayList<>();
        for (int i = 0; i < searchNames.length; i++){
            OfficeItemBean itemBean = new OfficeItemBean(searchNames[i],searchImg[i] + "");
            mList.add(itemBean);
        }
        mAdapter = new SearchGridViewAdapter(getContext(),mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new mOnItemClickListener());
    }
}
