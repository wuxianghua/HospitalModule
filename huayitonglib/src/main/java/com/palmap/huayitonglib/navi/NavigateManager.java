package com.palmap.huayitonglib.navi;

import android.util.Log;

import com.palmap.huayitonglib.navi.astar.navi.AStarLanePath;
import com.palmap.huayitonglib.navi.astar.navi.AStarPath;
import com.palmap.huayitonglib.navi.entity.ActionState;
import com.palmap.huayitonglib.navi.entity.NaviInfo;
import com.palmap.huayitonglib.navi.entity.NodeInfo;
import com.palmap.huayitonglib.navi.entity.PartInfo;
import com.palmap.huayitonglib.navi.route.INavigateManager;
import com.vividsolutions.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class NavigateManager {

    private static final String TAG = NavigateManager.class.getSimpleName();

    private List<AStarPath> mAStarPaths = null;
    private List<PartInfo> mPartInfos = new ArrayList<>();
    private NavigateUpdateListener mNavigateUpdateListener = null;

    public NavigateManager() {
    }

    public NavigateManager(List<AStarPath> aStarPaths) {
        setAStarPath(aStarPaths);
    }

    public List<PartInfo> getPartInfos() {
        return mPartInfos;
    }

    public void setAStarPath(List<AStarPath> aStarPaths) {
        mAStarPaths = aStarPaths;
        try {
            handlePartInfo();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 设置导航回调监听
     *
     * @param listener 导航回调监听
     */
    public void setNavigateUpdateListener(NavigateUpdateListener listener) {
        mNavigateUpdateListener = listener;
    }

    /**
     * 更新定位点
     *
     * @param floorId 楼层Id
     * @param x       x坐标
     * @param y       y坐标
     * @param degree  设备方位角
     */
    public void updatePosition(long floorId, double x, double y, double degree) {
        if (floorId <= 0 || x == 0 || y == 0) {
            return;
        }
        if (mPartInfos.isEmpty()) {
            return;
        }
        NaviInfo naviInfo = new NaviInfo();
        NodeInfo startNode = mPartInfos.get(0).getStartNode();
        double minDistance =
                startNode == null ? 0d : calculateLength(x, y, startNode.getX(), startNode.getY());
        int index = 0;
        for (PartInfo partInfo : mPartInfos) {
            if (partInfo == null || floorId != partInfo.getFloorId()) {
                continue;
            }
            double distance = partInfo.getDistanceByPoint(x, y);
            if (distance < minDistance) {
                index = partInfo.getIndex();
                minDistance = distance;
            }
        }
        PartInfo targetPart = mPartInfos.get(index);
        naviInfo.setFloorId(targetPart.getFloorId());
        naviInfo.setOriginalX(x);
        naviInfo.setOriginalY(y);
        naviInfo.setDistance(targetPart.getDistanceByPoint(x, y));
        double[] adsorbPoint = targetPart.getAdsorbPoint(x, y);
        naviInfo.setOnLineX(adsorbPoint[0]);
        naviInfo.setOnLineY(adsorbPoint[1]);
        naviInfo.setRemainLength(targetPart.getDistanceFromEndNode(adsorbPoint[0], adsorbPoint[1]));
        naviInfo.setTotalRemainLength(naviInfo.getRemainLength() + targetPart.getDistance());
        naviInfo.setAdsorbPart(targetPart);
        naviInfo.setNextAction(targetPart.getNextAction());
        StringBuilder tipBuilder = new StringBuilder();
        tipBuilder.append("直行").append((int) naviInfo.getRemainLength()).append("米后").append
                (targetPart.getNextAction().toString());
        naviInfo.setNaviTip(tipBuilder.toString());
        if (mNavigateUpdateListener != null) {
            mNavigateUpdateListener.onNavigateUpdate(naviInfo);
        }
    }

    private void handlePartInfo() throws IllegalAccessException {
        if (mAStarPaths == null) {
            throw new IllegalAccessException("not exist original route data");
        }
        mPartInfos.clear();
        int index = 0;
        for (AStarPath path : mAStarPaths) {
            if (path == null) {
                continue;
            }
            if (path instanceof AStarLanePath) {
                PartInfo partInfo = new PartInfo();
                Point startPoint = (Point) path.getFrom().getVertex().getShape();
                Point endPoint = (Point) path.getTo().getVertex().getShape();
                partInfo.setIndex(index);
                partInfo.setFloorId((((AStarLanePath) path).getPath() == null
                        ? -1L : ((AStarLanePath) path).getPath().getPlanarGraphId()));
//                partInfo.setFloorHeight(getFloorHeight(path.getAltitude()));
                if (startPoint != null && endPoint != null) {
                    partInfo.setStartNode(
                            new NodeInfo(startPoint.getX(), startPoint.getY(), partInfo.getFloorId()));
                    partInfo.setEndNode(
                            new NodeInfo(endPoint.getX(), endPoint.getY(), partInfo.getFloorId()));
                    partInfo.setAngle(calculateAngle(startPoint, endPoint));
                    partInfo.setLength(calculateLength(startPoint, endPoint));
                }
                mPartInfos.add(partInfo);
                index++;
            }
        }
        updatePartInfo();
    }

    private void updatePartInfo() {
        for (int i = mPartInfos.size() - 1; i >= 0; i--) {
            PartInfo partInfo = mPartInfos.get(i);
            if (partInfo == null) {
                continue;
            }
            if (i + 1 >= mPartInfos.size()) {
                partInfo.setDistance(0);
                partInfo.setNextAction(ActionState.ACTION_ARRIVE);
            } else {
                PartInfo nextPart = mPartInfos.get(i + 1);
                partInfo.setDistance(nextPart.getLength() + nextPart.getDistance());
                if (nextPart.getFloorId() == partInfo.getFloorId()) {
                    float dAngle = nextPart.getAngle() - partInfo.getAngle();
                    if (dAngle > 180) {
                        dAngle -= 360;
                    } else if (dAngle < -180) {
                        dAngle += 360;
                    }
                    if (dAngle >= 80 && dAngle <= 100) {
                        partInfo.setNextAction(ActionState.ACTION_TURN_RIGHT);
                    } else if (dAngle > 0 && dAngle < 80) {
                        partInfo.setNextAction(ActionState.ACTION_FRONT_RIGHT);
                    } else if (dAngle > 100 && dAngle <= 180) {
                        partInfo.setNextAction(ActionState.ACTION_BACK_RIGHT);
                    } else if (dAngle >= -100 && dAngle <= -80) {
                        partInfo.setNextAction(ActionState.ACTION_TURN_LEFT);
                    } else if (dAngle >= -180 && dAngle < -100) {
                        partInfo.setNextAction(ActionState.ACTION_BACK_LEFT);
                    } else if (dAngle < 0 && dAngle > -80) {
                        partInfo.setNextAction(ActionState.ACTION_FRONT_LEFT);
                    }
                } else {
                    partInfo.setNextAction(ActionState.CHANGE_FLOOR);
//                    if (nextPart.getFloorHeight() > partInfo.getFloorHeight()) {
//                        partInfo.setNextAction(ActionState.ACTION_UPSTAIRS);
//                    } else {
//                        partInfo.setNextAction(ActionState.ACTION_DOWNSTAIRS);
//                    }
                }
            }
            Log.e("***", partInfo.getIndex() + " , " +partInfo.getNextAction().toString());
        }
    }

    //计算长度
    private double calculateLength(Point startPoint, Point endPoint) {
        double dx = endPoint.getX() - startPoint.getX();
        double dy = endPoint.getY() - startPoint.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //计算长度
    private double calculateLength(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //计算方位角
    private float calculateAngle(Point startPoint, Point endPoint) {
        float angle = 0;
        double dx = endPoint.getX() - startPoint.getX();
        double dy = endPoint.getY() - startPoint.getY();
        angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        if (angle < 0) {
            angle += 360;
        }
        angle = 90 - angle;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    //获取楼层高度
    public int getFloorHeight(double altitude) {
        if (altitude >= 0) {
            return (int) Math.floor(altitude / 5.0) + 1;
        } else {
            return (int) Math.ceil(altitude / 5.0);
        }
    }

}
