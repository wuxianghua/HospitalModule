package com.palmap.huayitonglib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    //-------------------界面点击事件
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_rr) {
            finish();
        }
    }
}
