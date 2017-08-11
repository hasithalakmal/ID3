package com.slit.id3.bean;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<Node> listOfNodes = new ArrayList<Node>();

    public List<Node> getListOfNodes() {
        return listOfNodes;
    }

    public void setListOfNodes(List<Node> listOfNodes) {
        this.listOfNodes = listOfNodes;
    }

    public void addNode(Node node){
        this.listOfNodes.add(node);
    }

    public Node getNodeByID(int nodeId){
        Node resultNode = null;
        if(this.listOfNodes != null) {
            for (Node node : this.listOfNodes) {
                if (node.getNodeId() == nodeId) {
                    resultNode = node;
                    break;
                }
            }
        }
        return resultNode;
    }
}
