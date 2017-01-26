package editor.model;


import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import static editor.model.Node.DEFAULT_RADIUS;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Edge extends QuadCurve{
    private Node startNode;
    private Node endNode;
    private Path arrowHead;
    public static final double STROKE_WIDTH = 3;
    /**
     * Takes a start Node and an end Node, draws a line between the center of the two.
     * @param startNode
     * @param endNode
     */
    public Edge (Node startNode, Node endNode) {
        arrowHead = new Path();
        this.arrowHead.setStrokeWidth(STROKE_WIDTH);
        this.setStartNode(startNode);
        if(endNode != null){
            this.setEndNode(endNode);
        }
        this.setFill(new Color(0,0,0,0));
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(STROKE_WIDTH);

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
    public Shape setEndNode(Node endNode) {
        this.setEndX(endNode.getCenterX());
        this.setEndY(endNode.getCenterY());
        this.endNode = endNode;
        this.endNode.addEdge(this);

        setBend();
        return makeArrow();
    }

    public Shape makeArrow() {
        double deltaY = (getControlY() - endNode.getCenterY());
        double deltaX = (getControlX() - endNode.getCenterX());
        double angle = Math.atan2(deltaY,deltaX);
        double x = endNode.getCenterX() + Math.cos(angle)*DEFAULT_RADIUS;
        double y = endNode.getCenterY() + Math.sin(angle)*DEFAULT_RADIUS;
        arrowHead.getElements().clear();
        arrowHead.getElements().add(new MoveTo(x, y));
        arrowHead.getElements().add(new LineTo(x + Math.cos(angle+Math.toRadians(45))*(DEFAULT_RADIUS/2),y + Math.sin(angle+Math.toRadians(45))*(DEFAULT_RADIUS/2)));
        arrowHead.getElements().add(new LineTo(x + Math.cos(angle+Math.toRadians(-45))*(DEFAULT_RADIUS/2),y + Math.sin(angle+Math.toRadians(-45))*(DEFAULT_RADIUS/2)));
        arrowHead.getElements().add(new LineTo(x,y));
        return arrowHead;
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
        makeArrow();
    }
}
