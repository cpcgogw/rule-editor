package editor.controller;

import editor.model.Edge;
import editor.model.Node;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

import java.awt.event.MouseEvent;

/**
 * Created by vilddjur on 1/25/17.
 */
public class EdgeController {
    private Pane canvas;
    private boolean dragging;
    public EdgeController(Pane canvas){
        this.canvas = canvas;
        dragging = false;
    }

    public Edge addEdge(Node c, Node s) {
        Edge e = new Edge(c, s);

        e.setOnMousePressed(mouseEvent -> dragging = true);
        e.setOnMouseReleased(event -> {
            dragging = false;
        });

        e.setOnMouseDragged(event -> {
                if(dragging){
                    e.setControlX(event.getX());
                    e.setControlY(event.getY());
                }
        });

        return e;
    }

}
