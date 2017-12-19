package com.palmap.huayitonglib.navi.astar.navi;

/**
 * @author Vito Zheng
 */
public interface H {
    double H(AStarVertex current, AStarVertex target);
}
