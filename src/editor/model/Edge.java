package editor.model;


import javafx.scene.shape.Line;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Edge extends Line{
    private Node startNode;
    private Node endNode;

    /**
     * Takes a start Node and an end Node, draws a line between the center of the two.
     * @param startNode
     * @param endNode
     * @throws Exception
     */
    public Edge (Node startNode, Node endNode) throws Exception {
        this.setStartNode(startNode);
        if(endNode != null){
            this.setEndNode(endNode);
        }
    }

    /**
     * Sets the startNode to the given Node, throws nullpointer exception when given Node is null
     * @param startNode
     * @throws Exception
     */
    public void setStartNode(Node startNode) throws Exception {
        if(startNode == null){
            throw new NullPointerException("Edge: start Node cannot be null");
        }
        this.setStartX(startNode.getCenterX());
        this.setStartY(startNode.getCenterY());
        this.startNode = startNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the endNode to the given Node, throws nullpointer exception when given Node is null
     * @param endNode
     * @throws Exception
     */
    public void setEndNode(Node endNode) throws Exception {
        if(endNode == null){
            throw new NullPointerException("Edge: end Node cannot be null");
        }
        this.setEndX(endNode.getCenterX());
        this.setEndY(endNode.getCenterY());
        this.endNode = endNode;
    }

    public Node getEndNode() {
        return endNode;
    }
}
