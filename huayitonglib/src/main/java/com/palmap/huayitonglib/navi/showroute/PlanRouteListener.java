package com.palmap.huayitonglib.navi.showroute;

/**
 * Created by yibo.liu on 2017/12/20 16:28.
 */

public interface PlanRouteListener {

    boolean onSuccess(RouteBean bean);

    void onError();
}
