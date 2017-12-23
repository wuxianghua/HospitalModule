package com.palmap.huayitonglib.navi.showroute;

import com.palmap.huayitonglib.navi.entity.ActionState;

/**
 * 模拟导航的监听器
 * Created by yibo.liu on 2017/12/21 22:01.
 */

public interface SimulateNaviStateListener {

    void onPre(long fromFloorId);

    void onStart(long floorId);

    boolean onSwitchFloor(long floorId);

    void onFinish();

    void onInterrupted();

    void onError();

    void onTopInfo(String topInfo);

    void onBottomInfo(String bottomInfo);

    void onActionState(ActionState state);

    void onSpeak(String msg);
}
