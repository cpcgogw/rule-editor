package editor.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static editor.controller.Controller.tools.*;

public class Controller {
    /**
     * Buttons for handling active tools
     */
    @FXML
    private Button edge_button;
    @FXML
    private Button node_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button move_button;

    @FXML
    private Pane canvas;

    public tools getActiveTool() {
        return activeTool;
    }

    /**
     * Enum to keep track of which tool is active
     */
    public enum tools {
        EDGE, NODE, DELETE, MOVE
    }
    private tools activeTool;

    private NodeController nodeController;
    public void initialize(){
        activeTool = NODE;
        node_button.setOnMouseClicked(mouseEvent -> activeTool = NODE);
        edge_button.setOnMouseClicked(mouseEvent -> activeTool = EDGE);
        delete_button.setOnMouseClicked(mouseEvent -> activeTool = DELETE);
        move_button.setOnMouseClicked(mouseEvent -> activeTool = MOVE);
        canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent));

        nodeController = new NodeController(this, canvas);
    }

    private void handlePress(MouseEvent event) {
        if(activeTool == NODE){
            Node node = nodeController.addNode(event.getX(), event.getY(), 40, Color.BLUE);

            canvas.getChildren().add(node);
        }
    }
}
