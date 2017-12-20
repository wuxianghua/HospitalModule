package com.palmap.huayitonglib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.db.bridge.MapPointInfoDbManager;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.fragment.SearchListFragment;
import com.palmap.huayitonglib.fragment.SearchShowFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearch_Ed;
    private SearchShowFragment mSearchShowFragment;
    private SearchListFragment mSearchListFragment;
    private List<MapPointInfoBean> mList = new ArrayList<>();
    private static int TYPE_SHOW = 0;
    private static int TYPE_SEARCH = 1;
    private int type = TYPE_SHOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    // 搜索监听
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            search(str);
        }
    };

    private void initView() {
        mSearch_Ed = (EditText) findViewById(R.id.search_Ed);
        mSearch_Ed.addTextChangedListener(mTextWatcher);

        mSearchShowFragment = new SearchShowFragment();
        mSearchListFragment = new SearchListFragment();
        replaceFragment(mSearchShowFragment);
    }

    private void search(String str){
        Log.e("db", "search: " + MapPointInfoDbManager.get().getAll().size());
        if (str.length()>0){
            mList = MapPointInfoDbManager.get().query(str);
            if (mList != null && mList.size() !=0){
                Log.e("db", "search: type " +  type );
                if (type == TYPE_SEARCH) {
                    mSearchListFragment.setData(mList);
                } else {
                    replaceFragment(mSearchListFragment);
                }
//                replaceFragment(mSearchListFragment);
//                mSearchListFragment.setData(mList);
            }
        } else {
            replaceFragment(mSearchShowFragment);
        }
    }

    public void setEditText(String str){
        mSearch_Ed.setText(str);
    }

    private void replaceFragment(Fragment fragment){
        if (fragment == mSearchListFragment){
            type = TYPE_SEARCH;
        } else {
            type = TYPE_SHOW;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        mSearch_Ed.setFocusableInTouchMode(true);
    }

    /**
     * 关闭键盘
     *
     * @param context
     */
    public static void closeSoftKeyBoard(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
