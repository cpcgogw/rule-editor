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

    /**
     * TODO: fix Node dependency, we dont want controller here, only using it to keep track of active tool.
     * @param controller
     * @param canvas
     */
    public NodeController(Controller controller, Pane canvas){
        currentEdge = null;
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
        }
    }

    public Node addNode(double x, double y, int radius, Color color) {
        Node c = new Node(x,y,radius,color);
        c.setOnMousePressed(mouseEvent -> handlePressNode(mouseEvent, c));
        return c;
    }
}
