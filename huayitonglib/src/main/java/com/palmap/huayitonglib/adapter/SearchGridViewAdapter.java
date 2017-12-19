package com.palmap.huayitonglib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.bean.OfficeItemBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hengwei.guo on 2017/10/13 14:47.
 */

public class SearchGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfficeItemBean> mList = new ArrayList<>();

    public SearchGridViewAdapter(Context context, List<OfficeItemBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void refresh(List<OfficeItemBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    ViewHodler hodler;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.search_item, null);
            hodler = new ViewHodler();
            hodler.officeTv = view.findViewById(R.id.officeTv);
            hodler.officeImg = view.findViewById(R.id.officeImg);
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.officeTv.setText(mList.get(i).getTitle());
        hodler.officeImg.setImageResource(Integer.parseInt(mList.get(i).getImgUrl()));
        return view;
    }

    class ViewHodler{
        TextView officeTv;
        ImageView officeImg;
    }

}
