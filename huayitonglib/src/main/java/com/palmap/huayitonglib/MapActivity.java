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
    //------------切换楼层点击按钮
    public void onChangeFloorClick(View view) {
        int i = view.getId();
        if (i== R.id.b2){

        }else  if (i== R.id.b1){

        }else if (i== R.id.f0){
            //平面图------------
            
        }else if (i== R.id.f1){

        }else if (i== R.id.f2){

        }else if (i== R.id.f3){

        }else if (i== R.id.f4){

        }else if (i== R.id.f5){

        }else if (i== R.id.f6){

        }else if (i== R.id.f7){

        }else if (i== R.id.f8){

        }else if (i== R.id.f9){

        }else if (i== R.id.f10){

        }else if (i== R.id.f11){

        }else if (i== R.id.f12){

        }else if (i== R.id.f13){

        }else if (i== R.id.f14){

        }else if (i== R.id.f15){

        }
    }


}
