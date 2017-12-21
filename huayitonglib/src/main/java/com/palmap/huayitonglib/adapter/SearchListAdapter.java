package com.palmap.huayitonglib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;

import java.util.List;

/**
 * Created by hengwei.guo on 2017/10/13 15:58.
 */

public class SearchListAdapter extends BaseQuickAdapter<MapPointInfoBean, BaseViewHolder> {

    private Context mContext;

    public SearchListAdapter(@LayoutRes int layoutResId, @Nullable List<MapPointInfoBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MapPointInfoBean item) {
        helper.setText(R.id.poiNameTv, item.getName())
                .setText(R.id.poiAreaTv, item.getAddress())
                .setText(R.id.floorTv, item.getFloorName());
    }
}
