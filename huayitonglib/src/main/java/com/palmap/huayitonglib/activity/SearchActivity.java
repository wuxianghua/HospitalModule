package com.palmap.huayitonglib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palmap.huayitonglib.R;
import com.palmap.huayitonglib.db.bridge.MapPointInfoDbManager;
import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.fragment.SearchListFragment;
import com.palmap.huayitonglib.fragment.SearchShowFragment;
import com.palmap.huayitonglib.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends VoiceListenActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private EditText mSearch_Ed;
    private SearchShowFragment mSearchShowFragment;
    private SearchListFragment mSearchListFragment;
    private List<MapPointInfoBean> mList = new ArrayList<>();
    private static int TYPE_SHOW = 0;
    private static int TYPE_SEARCH = 1;
    private int type = TYPE_SHOW;
    public static int SEARCH_END = 0;
    public static int SEARCH_START = 1;
    private int searchType = 0;
    private String mDefaultKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        searchType = intent.getIntExtra(Constant.SEATCHTYPE_KEY, 0);
        mDefaultKeyWord = intent.getStringExtra(Constant.SEATCH_KEYWORD);
        initView();
        initVoiceListen();
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
            if (isFormDb) {
                search(str);
            }
        }
    };

    // 点击键盘中的搜索图标或按钮
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.i("ghw", "onEditorAction: 点击搜索按钮");
                search(mSearch_Ed.getText().toString());
                return true;
            }
            return false;
        }
    };

    private void initView() {
        mSearch_Ed = (EditText) findViewById(R.id.search_Ed);
        mSearch_Ed.addTextChangedListener(mTextWatcher);
        mSearch_Ed.setOnEditorActionListener(mOnEditorActionListener);

        mSearchShowFragment = new SearchShowFragment();
        mSearchListFragment = new SearchListFragment(searchType);
        if (TextUtils.isEmpty(mDefaultKeyWord)) {
            replaceFragment(mSearchShowFragment);
        } else {
            setEditText(mDefaultKeyWord);
//            mList.clear();
//            mList = MapPointInfoDbManager.get().query(mDefaultKeyWord);
//            replaceFragment(mSearchListFragment);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mSearchListFragment.setData(mList);
//                }
//            });
        }
    }

    // 点击返回
    public void back(View view) {
//        if (!TextUtils.isEmpty(mSearch_Ed.getText())){
        goBack();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        if (type == TYPE_SEARCH) {
            closeSoftKeyBoard(this);
            replaceFragment(mSearchShowFragment);
            mSearch_Ed.setText("");
        } else {
            finish();
            closeSoftKeyBoard(this);
        }
    }

    @Override
    public void handleListenResult(String value) {
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "没有听清楚哦，请重新搜索", Toast.LENGTH_SHORT).show();
        } else {
            mList.clear();
            mList = MapPointInfoDbManager.get().query(value);
            if (mList.isEmpty()) {
                Toast.makeText(this, String.format("没有找到 %s，请重新搜索", value), Toast.LENGTH_SHORT).show();
            } else {
                setEditText(value);
            }
        }
    }

    @Override
    public void onListenError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 点击语音图标
    public void startVoice(View view) {
        showListenDialog();
    }

    private void search(String str) {
        Log.e("db", "search: " + MapPointInfoDbManager.get().getAll().size());
        if (!TextUtils.isEmpty(str)) {
            mList.clear();
            mList = MapPointInfoDbManager.get().query(str);
            if (mList != null) {
                Log.e("db", "search: type " + type);
                if (type == TYPE_SEARCH) {
                    mSearchListFragment.setData(mList);
                } else {
                    replaceFragment(mSearchListFragment);
                }
            }
        } else {
            replaceFragment(mSearchShowFragment);
        }
    }

    private boolean isFormDb = true;

    public void search(final String str, final List<MapPointInfoBean> mList) {
        if (str.length() > 0) {
            if (mList != null) {
                isFormDb = false;
                Log.e("db", "search: type " + type + "----" + mList.size());
                replaceFragment(mSearchListFragment);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mSearch_Ed.setText(str);
                        mSearch_Ed.setSelection(str.length());
                        isFormDb = true;
                        mSearchListFragment.setData(mList);
                    }
                });
            }
        } else {
            replaceFragment(mSearchShowFragment);
        }
    }

    public List<MapPointInfoBean> getList() {
        return mList;
    }

    public void setEditText(String str) {
        mSearch_Ed.setText(str);
        mSearch_Ed.setSelection(str.length());
        closeSoftKeyBoard(this);
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment == mSearchListFragment) {
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
