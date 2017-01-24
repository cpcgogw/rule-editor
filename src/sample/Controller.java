package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static sample.Controller.tools.DELETE;
import static sample.Controller.tools.EDGE;
import static sample.Controller.tools.NODE;

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
    private Pane canvas;
    /**
     * Enum to keep track of which tool is active
     */
    public enum tools {
        EDGE, NODE, DELETE
    }
    private tools activeTool;
    public void initialize(){
        activeTool = NODE;
        node_button.setOnMouseClicked(mouseEvent -> activeTool = NODE);
        edge_button.setOnMouseClicked(mouseEvent -> activeTool = EDGE);
        delete_button.setOnMouseClicked(mouseEvent -> activeTool = DELETE);

        canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent));
    }

    private void handlePress(MouseEvent event) {
        if(activeTool == NODE){
            Circle c = new Circle(event.getX(), event.getY(), 40, Color.BLUE);
            c.setOnMouseClicked(mouseEvent -> handlePressNode(mouseEvent, c));
            canvas.getChildren().add(c);
        }else if(activeTool == EDGE){
            
        }

    }

    private void handlePressNode(MouseEvent event, Node c) {
        if(activeTool == DELETE){
            canvas.getChildren().remove(c);
        }
    }
}
