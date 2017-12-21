package com.palmap.huayitonglib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.adapter.SearchListAdapter;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by hengwei.guo on 2017/12/19 11:23.
 */

public class SearchListFragment extends Fragment{

    private View view;
    private SearchActivity self;
    private RecyclerView mRecycleView;
    private SearchListAdapter mAdapter;
    private List<MapPointInfoBean> mList = new ArrayList<>();;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_list, null);
        self = (SearchActivity) getActivity();
        initView();
        return view;
    }

    // 列表的点击事件
    class ListOnItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Log.i(TAG, "onItemClick: 跳转到地图");
            int id = view.getId();
            if (id == R.id.goWithMe){ // 点击带我去
                Log.i(TAG, "onItemClick: 带我去");
            } else if (id == R.id.lookMap){ // 点击看地图
                Log.i(TAG, "onItemClick: 看地图");
            }
        }
    }

    public void setData(List<MapPointInfoBean> list){
        mList = list;
        Log.e(TAG, "setData: mlist" + mList.size());
//        mAdapter.setNewData(mList);
//        mAdapter.notifyDataSetChanged();
        mAdapter = new SearchListAdapter(R.layout.fragment_search_list_item, mList,getContext());
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ListOnItemClickListener());
    }

    private void initView() {
        mRecycleView = view.findViewById(R.id.search_recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = self.getList();
        mAdapter = new SearchListAdapter(R.layout.fragment_search_list_item, mList,getContext());
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ListOnItemClickListener());

    }

}
