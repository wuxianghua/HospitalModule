package com.palmap.huayitonglib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.view.PulseMarkerView;


public class PulseMarkerViewAdapter extends MapboxMap.MarkerViewAdapter<PulseMarkerView> {
    private LayoutInflater inflater;

    public PulseMarkerViewAdapter(@NonNull Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public View getView(@NonNull PulseMarkerView marker, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_test_marker, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setImageBitmap(marker.getIcon().getBitmap());
        return convertView;
    }

    private static class ViewHolder {

        public ImageView icon;
    }
}
