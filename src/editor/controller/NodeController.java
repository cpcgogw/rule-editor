package editor.controller;

import editor.model.Edge;

import editor.model.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static editor.controller.Controller.tools.*;

/**
 * Created by vilddjur on 1/24/17.
 */
public class NodeController {

    private Edge currentEdge;
    private EdgeController edgeController;
    private boolean dragging;
    private ArrayList<Node> nodes;

    /**
     *
     */
    public NodeController(){
        currentEdge = null;
        dragging = false;
        this.edgeController = new EdgeController();
        this.nodes = new ArrayList<Node>();
    }

    /**
     * Removes all Nodes and Edges
     */
    public void clear() {
        nodes.clear();
    }

    private void handlePressNode(MouseEvent event, Node c) {
        if(Controller.activeTool == DELETE){
            Controller.getActiveCanvas().getChildren().remove(c);
            Controller.getActiveCanvas().getChildren().removeAll(c.getEdges());
            for (Edge e: c.getEdges()) {
                Controller.getActiveCanvas().getChildren().removeAll(e.getArrow());
            }
        }else if(Controller.activeTool == EDGE){
            if(currentEdge == null){
                currentEdge = edgeController.addEdge(c, null);
            }else{
                Controller.getActiveCanvas().getChildren().add(currentEdge.setEndNode(c));
                Controller.getActiveCanvas().getChildren().add(currentEdge);
                currentEdge = null;
            }
        }else if(Controller.activeTool == MOVE){
            dragging = true;
        }
    }
    public Node addNode(Node c) {
        c.setOnMousePressed(mouseEvent -> handlePressNode(mouseEvent, c));
        c.setOnMouseReleased(event -> {
            dragging = false;
        });
        c.setOnMouseDragged(event -> {
            if(dragging){
                c.setPos(event.getX(),event.getY());
                c.updateEdges();
            }
        });
        nodes.add(c);
        return c;
    }
    public Node addNode(double x, double y, int radius, Color color) {
        Node c = new Node(x,y,radius,color, Controller.activeType);
        c.setOnMousePressed(mouseEvent -> handlePressNode(mouseEvent, c));
        c.setOnMouseReleased(event -> {
            dragging = false;
        });
        c.setOnMouseDragged(event -> {
            if(dragging){
                c.setPos(event.getX(),event.getY());
                c.updateEdges();
            }
        });
        nodes.add(c);
        return c;
    }

    public  ArrayList<Node> getNodes(){
        return nodes;
    }

    public  EdgeController getEdgeController(){
        return edgeController;
    }
}
