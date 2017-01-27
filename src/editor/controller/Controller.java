package editor.controller;

import editor.FileHandler;
import editor.model.Edge;
import javafx.fxml.FXML;
import editor.model.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static editor.controller.Controller.tools.*;
import static editor.model.Node.DEFAULT_RADIUS;

import editor.model.Node.NodeType;
import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;

public class Controller {
    /**
     * Buttons for handling active tools
     */
    @FXML
    private Button edge_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button move_button;

    @FXML
    private VBox node_types_box;

    @FXML
    private Button start_node_button;

    @FXML
    private Button end_node_button;

    @FXML
    private Button key_node_button;

    @FXML
    private Button lock_node_button;

    @FXML
    private Button room_node_button;

    @FXML
    private Pane canvas;

    @FXML
    private MenuItem save_button;

    @FXML
    private MenuItem load_button;

    public tools getActiveTool() {
        return activeTool;
    }

    public NodeType getActiveType(){
        return activeType;
    }

    /**
     * Enum to keep track of which tool is active
     */
    public enum tools {
        EDGE, NODE, DELETE, MOVE
    }
    private tools activeTool;
    private NodeType activeType;

    private NodeController nodeController;
    public void initialize(){
        activeType = NodeType.START;
        activeTool = NODE;
        edge_button.setOnMouseClicked(mouseEvent -> activeTool = EDGE);
        delete_button.setOnMouseClicked(mouseEvent -> activeTool = DELETE);
        move_button.setOnMouseClicked(mouseEvent -> activeTool = MOVE);

        start_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.START));
        end_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.END));
        key_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.KEY));
        lock_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.LOCK));
        room_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.ROOM));

        save_button.setOnAction(actionEvent -> PrepareSave());
        load_button.setOnAction(actionEvent -> PrepareLoad());

        canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent));

        nodeController = new NodeController(this, canvas);
    }

    private void activateType(NodeType type) {
        activeType = type;
        activeTool = NODE;
    }

    private void PrepareLoad() {
        String path = JOptionPane.showInputDialog("Load","What is the name of the savefile?");
        Pair<ArrayList<Node>,ArrayList<Edge>> pair = FileHandler.LoadNodes("saves/"+path);
        for(Node node : pair.getKey()){
            Node c = nodeController.addNode(node);
            canvas.getChildren().add(c);
        }
        for(Edge e : pair.getValue()){
            Edge c = nodeController.getEdgeController().addEdge(e);
            canvas.getChildren().add(c.getArrow());
            canvas.getChildren().add(c);
        }
    }

    private void PrepareSave() {
        String path = JOptionPane.showInputDialog("Save","What is the name of the savefile?");
        FileHandler.SaveNodes(nodeController.getNodes(),"saves/"+path);
    }

    private void handlePress(MouseEvent event) {
        if(activeTool == NODE){
            Node node = nodeController.addNode(event.getX(), event.getY(), DEFAULT_RADIUS, Color.BLUE);

            canvas.getChildren().add(node);
        }
    }
}
