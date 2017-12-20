package com.palmap.huayitonglib.navi.shownaviroute;

import android.content.Context;

/**
 * Created by yibo.liu on 2017/12/19 17:38.
 */

public interface IRoute<T, K> {

    /**
     * @param context
     */
    void init(Context context, T mapEngine, String routeDataPath, String resId);

    /**
     * 路线规划的监听器
     *
     * @param listener
     */
    void setListener(PlanRouteListener listener);

    /**
     * 规划路线
     *
     * @param fromlong
     * @param fromlat
     * @param fromPlanargraph
     * @param tolong
     * @param tolat
     * @param toPlanargraph
     */
    void planRoute(double fromlong, double fromlat, long fromPlanargraph, double tolong, double tolat, long
            toPlanargraph);

    /**
     * 显示路线
     *
     * @param source
     */
    void showNaviRoute(K source);

    /**
     * 显示导航路线
     *
     * @param currentFloorId
     */
    void showNaviRoute(long currentFloorId);

    /**
     * 清除显示路线
     */
    void clearRoute();
}
