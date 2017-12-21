package com.palmap.huayitonglib.navi.entity;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class PartInfo {

    //直线方程参数(Ax+By+C=0)
    class EquationParam {
        double A;
        double B;
        double C;
    }

    private int mIndex;                 //段索引
    private long mFloorId;              //楼层Id
    private float mAngle;               //方位角
    private double mLength;             //段长
    private double mDistance;           //段终点距离线路终点距离
    private int mFloorHeight;           //楼层高度（-1，1，2）
    private NodeInfo mStartNode;        //起点
    private NodeInfo mEndNode;          //终点
    private ActionState mNextAction;    //下一步状态
    private EquationParam mEquationParam;    //直线方程参数

    public PartInfo() {
        init();
    }

    private void init() {
        mIndex = 0;
        mStartNode = null;
        mEndNode = null;
        mFloorId = -1L;
        mAngle = 0;
        mLength = 0;
        mDistance = 0;
        mFloorHeight = 1;
        mNextAction = ActionState.UNDEFINED;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public NodeInfo getStartNode() {
        return mStartNode;
    }

    public void setStartNode(NodeInfo startNode) {
        this.mStartNode = startNode;
    }

    public NodeInfo getEndNode() {
        return mEndNode;
    }

    public void setEndNode(NodeInfo endNode) {
        this.mEndNode = endNode;
    }

    public long getFloorId() {
        return mFloorId;
    }

    public void setFloorId(long floorId) {
        this.mFloorId = floorId;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
    }

    public double getLength() {
        return mLength;
    }

    public void setLength(double length) {
        this.mLength = length;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    public int getFloorHeight() {
        return mFloorHeight;
    }

    public void setFloorHeight(int floorHeight) {
        this.mFloorHeight = floorHeight;
    }

    public ActionState getNextAction() {
        return mNextAction;
    }

    public void setNextAction(ActionState nextAction) {
        this.mNextAction = nextAction;
    }

    private EquationParam getEquationParam() {
        if (mEquationParam == null) {
            mEquationParam = new EquationParam();
            if (mStartNode != null && mEndNode != null) {
                mEquationParam.A = mEndNode.getY() - mStartNode.getY();
                mEquationParam.B = mStartNode.getX() - mEndNode.getX();
                mEquationParam.C = mEndNode.getX() * mStartNode.getY() - mStartNode.getX() * mEndNode.getY();
            }
        }
        return mEquationParam;
    }

    /**
     * 获取线上点距离本段终点的距离
     *
     * @param x 点X坐标
     * @param y 点Y坐标
     * @return 距离
     */
    public double getDistanceFromEndNode(double x, double y) {
        if (mEndNode == null) {
            return -1;
        } else {
            double dx = mEndNode.getX() - x;
            double dy = mEndNode.getY() - y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    /**
     * 获取点到该段所在直线的距离
     *
     * @param x 点X坐标
     * @param y 点Y坐标
     * @return 距离
     */
    public double getDistanceByPoint(double x, double y) {
        PartInfo.EquationParam param = getEquationParam();
        if (param.A == 0d && param.B == 0d) {
            return -1;
        } else {
            return Math.abs(param.A * x + param.B * y + param.C) /
                    Math.sqrt(param.A * param.A + param.B * param.B);
        }
    }

    /**
     * 获取线上吸附点
     *
     * @param x 点X坐标
     * @param y 点Y坐标
     * @return 线上点
     */
    public double[] getAdsorbPoint(double x, double y) {
        PartInfo.EquationParam param = getEquationParam();
        if (param.A == 0d && param.B == 0d) {
            return new double[]{0d, 0d};
        } else {
            double newX = (param.B * param.B * x - param.A * param.B * y - param.A * param.C) /
                    (param.A * param.A + param.B * param.B);
            double newY = (param.A * param.A * y - param.A * param.B * x - param.B * param.C) /
                    (param.A * param.A + param.B * param.B);
            return new double[]{newX, newY};
        }
    }

}
