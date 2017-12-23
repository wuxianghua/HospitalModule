package com.palmap.huayitonglib.navi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class NaviInfo {

    private long mFloorId;              //楼层Id
    private double mOriginalX;          //原始X坐标
    private double mOriginalY;          //原始Y坐标
    private double mOnLineX;            //线上点X坐标
    private double mOnLineY;            //线上点Y坐标
    private double mDistance;           //距离线距离
    private double mRemainLength;       //所属段剩余距离
    private double mTotalRemainLength;  //距离导航终点距离
    private PartInfo mAdsorbPart;       //所属段
    //    private float mAngle;               //下一段方位角
    private ActionState mNextAction;    //下一步操作状态
    private String mNaviTip;            //导航提示
    private List<NodeInfo> mPassedNodeArray;    //已走过的点集合
    private List<NodeInfo> mUnPassedNodeArray;  //未走过的点集合

    public NaviInfo() {
        init();
    }

    private void init() {
        mFloorId = -1;
        mOriginalX = 0d;
        mOriginalY = 0d;
        mOnLineX = 0d;
        mOnLineY = 0d;
        mDistance = 0d;
        mAdsorbPart = null;
        mNextAction = ActionState.UNDEFINED;
        mNaviTip = "";
        mPassedNodeArray = new ArrayList<>();
        mUnPassedNodeArray = new ArrayList<>();
    }

    public long getFloorId() {
        return mFloorId;
    }

    public void setFloorId(long floorId) {
        this.mFloorId = floorId;
    }

    public double getOriginalX() {
        return mOriginalX;
    }

    public void setOriginalX(double originalX) {
        this.mOriginalX = originalX;
    }

    public double getOriginalY() {
        return mOriginalY;
    }

    public void setOriginalY(double originalY) {
        this.mOriginalY = originalY;
    }

    public double getOnLineX() {
        return mOnLineX;
    }

    public void setOnLineX(double onLineX) {
        this.mOnLineX = onLineX;
    }

    public double getOnLineY() {
        return mOnLineY;
    }

    public void setOnLineY(double onLineY) {
        this.mOnLineY = onLineY;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    public double getRemainLength() {
        return mRemainLength;
    }

    public void setRemainLength(double remainLength) {
        this.mRemainLength = remainLength;
    }

    public double getTotalRemainLength() {
        return mTotalRemainLength;
    }

    public void setTotalRemainLength(double totalRemainLength) {
        this.mTotalRemainLength = totalRemainLength;
    }

    public PartInfo getAdsorbPart() {
        return mAdsorbPart;
    }

    public void setAdsorbPart(PartInfo adsorbPart) {
        this.mAdsorbPart = adsorbPart;
    }

    public ActionState getNextAction() {
        return mNextAction;
    }

    public void setNextAction(ActionState nextAction) {
        this.mNextAction = nextAction;
    }

    public String getNaviTip() {
        return mNaviTip;
    }

    public void setNaviTip(String naviTip) {
        this.mNaviTip = naviTip;
    }

    public List<NodeInfo> getPassedNodeArray() {
        return mPassedNodeArray;
    }

    public void setPassedNodeArray(List<NodeInfo> passedNodeArray) {
        this.mPassedNodeArray = passedNodeArray;
    }

    public void addPassedNode(NodeInfo nodeInfo) {
        if (mPassedNodeArray == null) {
            mPassedNodeArray = new ArrayList<>();
        }
        mPassedNodeArray.add(nodeInfo);
    }

    public List<NodeInfo> getUnPassedNodeArray() {
        return mUnPassedNodeArray;
    }

    public void setUnPassedNodeArray(List<NodeInfo> unPassedNodeArray) {
        this.mUnPassedNodeArray = unPassedNodeArray;
    }

    public void addUnPassedNode(NodeInfo nodeInfo) {
        if (mUnPassedNodeArray == null) {
            mUnPassedNodeArray = new ArrayList<>();
        }
        mUnPassedNodeArray.add(nodeInfo);
    }

}
