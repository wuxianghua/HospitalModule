package com.palmap.huayitonglib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
//        helper.setText(R.id.search_title, item.getName())
//                .setText(R.id.search_addr, item.getFloorName());
//        String distance = new Random().nextInt(500) + 50 + "m";
//        helper.setText(R.id.search_distance, distance);

//        ImageView routeImg = helper.getView(R.id.routeImg);
//        routeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MapboxActivity.class);
//                intent.putExtra(Constant.KEY_FLOORNAME, item.getFloorName());
//                intent.putExtra(Constant.KEY_NAME, item.getName());
//                intent.putExtra(Constant.KEY_FLOORID, item.getFloorId());
//                intent.putExtra(Constant.KEY_LATITUDE, item.getLatitude());
//                intent.putExtra(Constant.KEY_LONGITUDE, item.getLongitude());
//                intent.putExtra(Constant.KEY_POIID, item.getPoiId());
//                mContext.startActivity(intent);
//            }
//        });
    }
}
