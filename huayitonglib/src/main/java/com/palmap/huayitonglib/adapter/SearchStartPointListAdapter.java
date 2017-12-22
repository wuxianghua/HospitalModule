package com.palmap.huayitonglib.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.utils.Constant;

import java.util.List;

/**
 * Created by hengwei.guo on 2017/10/13 15:58.
 */

public class SearchStartPointListAdapter extends BaseQuickAdapter<MapPointInfoBean, BaseViewHolder> {

    private SearchActivity self;

    public SearchStartPointListAdapter(@LayoutRes int layoutResId, @Nullable List<MapPointInfoBean> data, SearchActivity self) {
        super(layoutResId, data);
        this.self = self;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MapPointInfoBean item) {
        helper.setText(R.id.poiNameTv, item.getName())
                .setText(R.id.poiAreaTv, item.getAddress())
                .setText(R.id.floorTv, item.getFloorName());
        TextView setStartPointTv = helper.getView(R.id.setStartPointTv);
        setStartPointTv.setFocusable(true);
        setStartPointTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击设为起点
                Log.i(TAG, "onItemClick: 设为起点");
                Intent intent = new Intent();
                intent.putExtra("MapPointInfoBean", item);
                self.setResult(Constant.START_RESULTCODE, intent);
                self.finish();
            }
        });

    }
}
