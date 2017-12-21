package com.palmap.huayitonglib.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.adapter.SearchListAdapter;
import com.palmap.huayitonglib.adapter.SearchStartPointListAdapter;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.palmap.huayitonglib.activity.SearchActivity.SEARCH_END;

/**
 * Created by hengwei.guo on 2017/12/19 11:23.
 */

public class SearchListFragment extends Fragment{

    private View view;
    private SearchActivity self;
    private RecyclerView mRecycleView;
    private List<MapPointInfoBean> mList = new ArrayList<>();
    private int searchType = 0;
    private LinearLayout emptyView;

    public SearchListFragment() {
    }

    public SearchListFragment(int searchType) {
        this.searchType = searchType;
    }

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
            final MapPointInfoBean mapPointInfoBean = mList.get(position);
            if (searchType == SEARCH_END){
                RelativeLayout goWithMeView = view.findViewById(R.id.goWithMe);
                goWithMeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 点击带我去
                        Log.i(TAG, "onItemClick: 带我去");
                        Intent intent = new Intent();
                        intent.putExtra("MapPointInfoBean", mapPointInfoBean);
                        self.setResult(Constant.GOWITHME_RESULTCODE, intent);
                        self.finish();
                    }
                });

                RelativeLayout lookMapView = view.findViewById(R.id.lookMap);
                lookMapView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 点击看地图
                        Log.i(TAG, "onItemClick: 看地图");
                        Intent intent = new Intent();
                        intent.putExtra("MapPointInfoBean", mapPointInfoBean);
                        self.setResult(Constant.LOOKMAP_RESULTCODE, intent);
                        self.finish();
                    }
                });
            } else {
                TextView setStartPointTv = view.findViewById(R.id.setStartPointTv);
                setStartPointTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 点击设为起点
                        Log.i(TAG, "onItemClick: 设为起点");
                        Intent intent = new Intent();
                        intent.putExtra("MapPointInfoBean", mapPointInfoBean);
                        self.setResult(Constant.START_RESULTCODE, intent);
                        self.finish();
                    }
                });
            }
        }
    }

    public void setData(List<MapPointInfoBean> list){
        mList = list;
        Log.e(TAG, "setData: mlist" + mList.size());
//        mAdapter.setNewData(mList);
//        mAdapter.notifyDataSetChanged();
        if (searchType == SEARCH_END){
            SearchListAdapter mAdapter = new SearchListAdapter(R.layout.fragment_search_list_item, mList,getContext());
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ListOnItemClickListener());
        } else {
            SearchStartPointListAdapter mAdapter = new SearchStartPointListAdapter(R.layout.fragment_search_startlist_item, mList,getContext());
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ListOnItemClickListener());
        }
        mRecycleView.scrollToPosition(0);
        if (list.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        emptyView = view.findViewById(R.id.emptyView);
        mRecycleView = view.findViewById(R.id.search_recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = self.getList();
        Log.e(TAG, "setData: mlist" + mList.size());
        if (searchType == SEARCH_END){
            SearchListAdapter mAdapter = new SearchListAdapter(R.layout.fragment_search_list_item, mList,getContext());
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ListOnItemClickListener());
        } else {
            SearchStartPointListAdapter mAdapter = new SearchStartPointListAdapter(R.layout.fragment_search_startlist_item, mList,getContext());
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ListOnItemClickListener());
        }
        if (mList.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }

    }

}
