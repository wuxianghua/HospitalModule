package com.palmap.huayitonglib.navi.showroute;

/**
 * 模拟导航的监听器
 * Created by yibo.liu on 2017/12/21 22:01.
 */

public interface SimulateNaviStateListener {

    void onPre(long fromFloorId);

    void onStart(long floorId);

    void onFinish(long finishFloorId,long fromFloorId,long toFloorId);

    void onError();
}
