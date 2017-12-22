package com.palmap.huayitonglib.navi.showroute;

/**
 * Created by yibo.liu on 2017/12/21 22:11.
 */

public class SimulateNaviStateBean {

    private boolean acrossFloor;

    private long fromFloorId;

    private long toFloorId;

    private long finishFloorId;

    public boolean isAcrossFloor() {
        return acrossFloor;
    }

    public void setAcrossFloor(boolean acrossFloor) {
        this.acrossFloor = acrossFloor;
    }

    public long getFromFloorId() {
        return fromFloorId;
    }

    public void setFromFloorId(long fromFloorId) {
        this.fromFloorId = fromFloorId;
    }

    public long getToFloorId() {
        return toFloorId;
    }

    public void setToFloorId(long toFloorId) {
        this.toFloorId = toFloorId;
    }

    public long getFinishFloorId() {
        return finishFloorId;
    }

    public void setFinishFloorId(long finishFloorId) {
        this.finishFloorId = finishFloorId;
    }
}
