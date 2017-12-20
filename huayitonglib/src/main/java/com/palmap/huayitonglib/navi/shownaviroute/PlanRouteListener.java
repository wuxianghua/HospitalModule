package com.palmap.huayitonglib.navi.shownaviroute;

/**
 * Created by yibo.liu on 2017/12/20 16:28.
 */

public interface PlanRouteListener {

    /**
     * 如果返回true 则显示路线
     *
     * @param bean
     * @return
     */
    boolean onSuccess(RouteBean bean);

    void onError();
}
