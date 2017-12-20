package com.palmap.huayitonglib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;

/**
 * Created by hengwei.guo on 2017/12/19 11:23.
 */

public class SearchListFragment extends Fragment{

    private View view;
    private SearchActivity self;
    private RecyclerView mRecycleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_list, null);
        return view;
    }


}
