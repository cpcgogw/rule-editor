package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    }
}
