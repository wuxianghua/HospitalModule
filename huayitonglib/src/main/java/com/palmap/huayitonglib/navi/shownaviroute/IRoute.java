package com.palmap.huayitonglib.navi.shownaviroute;

import android.content.Context;

/**
 * Created by yibo.liu on 2017/12/19 17:38.
 */

public interface IRoute<T, K> {

    /**
     * 初始化
     *
     * @param context
     * @param mapEngine 地图引擎
     * @param routeDataPath 地图数据路径
     * @param resId 连接点图标资源id
     * @param aboveId 导航线要在那个层之上
     */
    void init(Context context, T mapEngine, String routeDataPath, int resId, String aboveId);

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
     * 清除显示路线,并且将保存的路线信息清除，请谨慎调用
     */
    void clearRoute();
}
