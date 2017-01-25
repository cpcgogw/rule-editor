package editor.model;


import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.QuadCurve;

import java.awt.*;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Edge extends QuadCurve{
    private Node startNode;
    private Node endNode;

    /**
     * Takes a start Node and an end Node, draws a line between the center of the two.
     * @param startNode
     * @param endNode
     */
    public Edge (Node startNode, Node endNode) {
        this.setStartNode(startNode);
        if(endNode != null){
            this.setEndNode(endNode);
        }
        this.setFill(new Color(0,0,0,0));
        this.setStroke(Color.BLACK);
    }

    /**
     * Sets the startNode to the given Node
     * @param startNode
     */
    public void setStartNode(Node startNode) {
        this.setStartX(startNode.getCenterX());
        this.setStartY(startNode.getCenterY());
        this.startNode = startNode;
        startNode.addEdge(this);
    }

    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the endNode to the given Node
     * @param endNode
     */
    public void setEndNode(Node endNode) {
        this.setEndX(endNode.getCenterX());
        this.setEndY(endNode.getCenterY());
        this.endNode = endNode;
        this.endNode.addEdge(this);
        setBend();
    }

    private void setBend() {
        this.setControlX((Math.abs(getEndX()-getStartX())));
        this.setControlY((Math.abs(getEndY()-getStartY())));
    }

    public Node getEndNode() {
        return endNode;
    }

    public void updateNodes() {
        if(startNode != null){
            this.setStartX(startNode.getCenterX());
            this.setStartY(startNode.getCenterY());
        }
        if(endNode != null) {
            this.setEndX(endNode.getCenterX());
            this.setEndY(endNode.getCenterY());
        }
        setBend();
    }
}
