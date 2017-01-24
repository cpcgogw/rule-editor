package editor.controller;

import editor.model.Edge;

import editor.model.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static editor.controller.Controller.tools.*;

/**
 * Created by vilddjur on 1/24/17.
 */
public class NodeController {

    private final Pane canvas;
    private Edge currentEdge;
    private Controller controller;
    private boolean dragging;
    /**
     * TODO: fix Node dependency, we dont want controller here, only using it to keep track of active tool.
     * @param controller
     * @param canvas
     */
    public NodeController(Controller controller, Pane canvas){
        currentEdge = null;
        dragging = false;
        this.controller = controller;
        this.canvas = canvas;
    }

    private void handlePressNode(MouseEvent event, Node c) {
        if(controller.getActiveTool() == DELETE){
            canvas.getChildren().remove(c);
        }else if(controller.getActiveTool() == EDGE){
            if(currentEdge == null){
                try {
                    currentEdge = new Edge(c, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    currentEdge.setEndNode(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                canvas.getChildren().add(currentEdge);
                currentEdge = null;
            }
        }else if(controller.getActiveTool() == MOVE){
            dragging = true;
        }
    }

    public Node addNode(double x, double y, int radius, Color color) {
        Node c = new Node(x,y,radius,color);
        c.setOnMousePressed(mouseEvent -> handlePressNode(mouseEvent, c));
        c.setOnMouseReleased(event -> {
            dragging = false;
            // reload all edges
        });
        c.setOnMouseDragged(event -> {
            if(dragging){
                c.setPos(event.getX(),event.getY());
            }
        });
        return c;
    }
}
