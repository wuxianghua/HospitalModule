package com.palmap.huayitonglib.navi.showroute;

/**
 * Created by yibo.liu on 2017/12/21 22:01.
 */

public interface SimulateNaviListener {

    /**
     * @param acrossFloor
     */
    void onStart(boolean acrossFloor);

    void onFinish();

    void onError();
}
