package com.palmap.huayitonglib.navi;


import com.palmap.huayitonglib.navi.entity.NodeInfo;
import com.palmap.huayitonglib.navi.entity.PartInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public class NavigateFactory {

    private static final double MIN_DISTANCE = 1;

    public static List<NodeInfo> makeMockPointArray(double distance, List<PartInfo> partInfos) {
        distance = distance <= 0 ? MIN_DISTANCE : distance;
        List<NodeInfo> mockPointArray = new ArrayList<>();
        long floorId = -1;
        for (PartInfo part : partInfos) {
            if (part == null) {
                continue;
            }
            if (part.getStartNode() == null || part.getEndNode() == null) {
                continue;
            }
            int mockPointCount = (int)(part.getLength() / distance);
            if (part.getFloorId() != floorId && part.getIndex() == 0) {
                mockPointArray.add(part.getStartNode());
            }
            if (mockPointCount == 0) {
                mockPointArray.add(part.getEndNode());
            } else {
                if (part.getStartNode().getX() == part.getEndNode().getX()) {
                    for (int i = 0; i < mockPointCount; i++) {
                        double mockY = part.getStartNode().getY() + distance * (i + 1);
                        if (part.getEndNode().getY() - mockY < distance) {
                            mockPointArray.add(part.getEndNode());
                            break;
                        } else {
                            mockPointArray.add(new NodeInfo(
                                    part.getStartNode().getX(), mockY, part.getStartNode().getZ()));
                        }
                    }
                } else if (part.getStartNode().getY() == part.getEndNode().getY()) {
                    for (int i = 0; i < mockPointCount; i++) {
                        double mockX = part.getStartNode().getX() + distance * (i + 1);
                        if (part.getEndNode().getX() - mockX < distance) {
                            mockPointArray.add(part.getEndNode());
                            break;
                        } else {
                            mockPointArray.add(new NodeInfo(
                                    mockX, part.getStartNode().getY(), part.getStartNode().getZ()));
                        }
                    }
                } else {
                    for (int i = 0; i < mockPointCount; i++) {
                        double dDistance = distance * (i + 1);
                        double mockX = part.getStartNode().getX() + dDistance * Math.sin(Math.toRadians(part.getAngle()));
                        double mockY = part.getStartNode().getY() + dDistance * Math.cos(Math.toRadians(part.getAngle()));
                        NodeInfo mockNode = new NodeInfo(mockX, mockY, part.getStartNode().getZ());
                        if (calculateLength(part.getEndNode(), mockNode) < distance) {
                            mockPointArray.add(part.getEndNode());
                            break;
                        } else {
                            mockPointArray.add(mockNode);
                        }
                    }
                }
            }
        }
        return mockPointArray;
    }

    //计算长度
    private static double calculateLength(NodeInfo startNode, NodeInfo endNode) {
        double dx = endNode.getX() - startNode.getX();
        double dy = endNode.getY() - startNode.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

}
