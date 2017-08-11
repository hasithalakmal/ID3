package com.slit.id3.bean;

import java.util.List;
import java.util.Map;

public class Node {
    private int nodeId;
    private String nodeName;
    private boolean isLeafNode;
    private int upperNodeId;
    private Map<String,Node> lowerNodeMap;
    private boolean isChecked;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public void setLeafNode(boolean leafNode) {
        isLeafNode = leafNode;
    }

    public int getUpperNodeId() {
        return upperNodeId;
    }

    public void setUpperNodeId(int upperNodeId) {
        this.upperNodeId = upperNodeId;
    }

    public Map<String, Node> getLowerNodeMap() {
        return lowerNodeMap;
    }

    public void setLowerNodeMap(Map<String, Node> lowerNodeMap) {
        this.lowerNodeMap = lowerNodeMap;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
