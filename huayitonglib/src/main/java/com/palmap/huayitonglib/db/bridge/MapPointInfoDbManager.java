package com.palmap.huayitonglib.db.bridge;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.palmap.huayitonglib.db.entity.MapPointInfoBean;
import com.palmap.huayitonglib.db.gen.MapPointInfoBeanDao;
import com.palmap.huayitonglib.utils.FileUtils;
import com.palmap.huayitonglib.utils.MapPointInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hengwei.guo on 2017/11/06 13:55.
 */

public class MapPointInfoDbManager {

    public static final String TAG = MapPointInfoDbManager.class.getSimpleName();

    private static MapPointInfoDbManager sInstance;
    private MapPointInfoBeanDao mDao;

    private MapPointInfoDbManager() {
        mDao = DbConnectionManager.get().getDaoSession().getMapPointInfoBeanDao();
    }

    public static MapPointInfoDbManager get() {
        if (sInstance == null) {
            synchronized (MapPointInfoDbManager.class) {
                if (sInstance == null) {
                    sInstance = new MapPointInfoDbManager();
                }
            }
        }
        return sInstance;
    }

    public void insert(MapPointInfoBean bean){
        // 根据pointId进行查询，只要pointId一样就属于重复
        MapPointInfoBean mBean = mDao.queryBuilder().where(MapPointInfoBeanDao.Properties.PoiId.eq(bean.getPoiId
                ())).build().unique();
        if (mBean != null) {
            Log.d(TAG, "insert: 已经存在不要重复插入");
            return;
        }
        if (TextUtils.isEmpty(bean.getPoiId())) {
            Log.d(TAG, "insert: 点位id为空添加失败");
            return;
        }
        mDao.insert(bean);
    }

    public void deleteAll(){
        mDao.deleteAll();
    }

    public List<MapPointInfoBean> getAll(){
        List<MapPointInfoBean> mList = mDao.loadAll();
        return mList;
    }

    // 模糊搜索
    public List<MapPointInfoBean> query(String value){
        List<MapPointInfoBean> mList = mDao.queryBuilder().where(MapPointInfoBeanDao.Properties.Name.like("%" + value + "%")).build().list();
        if (mList == null || mList.size() == 0){
            Log.d(TAG, "query: 查询结果为空");
            return new ArrayList<>();
        }
        return mList;
    }

    // 导入全部数据（入库）
    public void insertAllData(Context context){
        double startTime = System.currentTimeMillis();
        String serachPoiData = FileUtils.loadFromAssets(context,"SearchPoiInfo.json");
        List<MapPointInfoBean> mList = MapPointInfoUtils.getSearchPoiData(serachPoiData);
        if (mList.size()!=0){
            for (int i=0;i<mList.size();i++){
                insert(mList.get(i));
            }
        }
        Log.i(TAG, "insertData: 加载数据库数据一共耗时：" + (System.currentTimeMillis() - startTime));
    }
}
