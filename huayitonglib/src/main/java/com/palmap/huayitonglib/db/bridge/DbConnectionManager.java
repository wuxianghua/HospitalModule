package com.palmap.huayitonglib.db.bridge;

import android.content.Context;

import com.palmap.huayitonglib.db.gen.DaoMaster;
import com.palmap.huayitonglib.db.gen.DaoSession;

/**
 * Created by yibo.liu on 2017/09/13 14:40.
 */

public class DbConnectionManager {
    public static final String TAG = DbConnectionManager.class.getSimpleName();

    private static final String DB_NAME = "SearchPoi_db";

    private static DbConnectionManager sInstance;

    private static boolean mInitialized = false;
    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mDaoMaster;

    private DbConnectionManager() {

    }

    public static DbConnectionManager get() {
        if (sInstance == null) {
            synchronized (DbConnectionManager.class) {
                if (sInstance == null) {
                    sInstance = new DbConnectionManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        if (mInitialized) {
            return;
        }
        mInitialized = !mInitialized;
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void close() {
        mDevOpenHelper.close();
        mDaoSession.clear();
    }
}