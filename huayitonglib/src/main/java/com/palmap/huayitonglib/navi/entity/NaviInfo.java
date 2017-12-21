package com.palmap.huayitonglib.navi.entity;

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
    private String mNaviTip;            //导航提示

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
        mNaviTip = "";
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

    public String getNaviTip() {
        return mNaviTip;
    }

    public void setNaviTip(String naviTip) {
        this.mNaviTip = naviTip;
    }
}
