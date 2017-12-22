package com.palmap.huayitonglib.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

public class SearchListAdapter extends BaseQuickAdapter<MapPointInfoBean, BaseViewHolder> {

    private Context mContext;
    private SearchActivity self;

    public SearchListAdapter(@LayoutRes int layoutResId, @Nullable List<MapPointInfoBean> data, SearchActivity self) {
        super(layoutResId, data);
        this.self = self;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MapPointInfoBean item) {
        helper.setText(R.id.poiNameTv, item.getName())
                .setText(R.id.poiAreaTv, item.getAddress())
                .setText(R.id.floorTv, item.getFloorName());
        LinearLayout goWithMeView = helper.getView(R.id.goWithMe);
        Log.i(TAG, "onItemClick: 带我去---设置监听");
        goWithMeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onItemClick: 带我去");
                Intent intent = new Intent();
                intent.putExtra("MapPointInfoBean", item);
                self.setResult(Constant.GOWITHME_RESULTCODE, intent);
                self.finish();
            }
        });

        final LinearLayout lookMapView = helper.getView(R.id.lookMap);
        lookMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击看地图
                Log.i(TAG, "onItemClick: 看地图");
                Intent intent = new Intent();
                intent.putExtra("MapPointInfoBean", item);
                self.setResult(Constant.LOOKMAP_RESULTCODE, intent);
                self.finish();
            }
        });
    }
}
