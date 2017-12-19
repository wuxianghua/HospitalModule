package com.palmap.huayitonglib.navi.astar.navi;

/**
 * Created by wyx on 8/31/15.
 */
public abstract class AStarPath {

    protected AStarVertex from;
    protected AStarVertex to;

    public abstract double getWeight();

    public abstract double getAltitude();

    public AStarVertex getFrom() {
        return from;
    }

    public AStarVertex getTo() {
        return to;
    }
}
