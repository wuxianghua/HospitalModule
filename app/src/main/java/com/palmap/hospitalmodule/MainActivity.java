package com.palmap.hospitalmodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.palmap.huayitonglib.activity.MapActivity;
import com.palmap.huayitonglib.activity.SearchActivity;
import com.palmap.huayitonglib.utils.Constant;

import static com.palmap.huayitonglib.activity.SearchActivity.SEARCH_END;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onMainClick(View view){
        startActivity( new Intent(this, MapActivity.class));
    }
}
