package editor.controller;

import editor.FileHandler;
import editor.model.Edge;
import javafx.application.Platform;
import javafx.fxml.FXML;
import editor.model.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static editor.controller.Controller.tools.*;
import static editor.model.Node.DEFAULT_RADIUS;

import editor.model.Node.NodeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    /**
     * Buttons for handling active tools
     */

    @FXML
    private AnchorPane window;

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
    private Pane rule_canvas;
    @FXML
    private GridPane rule_pane;

    @FXML
    private MenuItem save_button;

    @FXML
    private MenuItem load_button;

    @FXML
    private MenuItem rule_menu_item;

    @FXML
    private MenuItem close_button;

    @FXML
    private MenuItem new_button;

    @FXML
    private MenuItem level_menu_item;

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
    private FileChooser fileChooser = new FileChooser();
    public static tools activeTool;
    public static NodeType activeType;

    public static Pane activeCanvas;

    private NodeController nodeController;
    public void initialize(){
        activeType = NodeType.START;
        activeTool = NODE;
        activeCanvas = canvas;

        edge_button.setOnMouseClicked(mouseEvent -> activeTool = EDGE);
        // init editing buttons
        delete_button.setOnMouseClicked(mouseEvent -> activeTool = DELETE);
        move_button.setOnMouseClicked(mouseEvent -> activeTool = MOVE);
        // init node buttons
        start_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.START));
        end_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.END));
        key_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.KEY));
        lock_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.LOCK));
        room_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.ROOM));
        // init top menu
        save_button.setOnAction(actionEvent -> PrepareSave());
        load_button.setOnAction(actionEvent -> PrepareLoad());
        rule_menu_item.setOnAction(actionEvent -> showRules());
        level_menu_item.setOnAction(actionEvent -> showLevel());
        close_button.setOnAction(actionEvent -> Platform.exit());

        //init level canvas
        canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent, canvas));

        //init rule canvas
        

        nodeController = new NodeController();

        new_button.setOnAction(actionEvent -> {nodeController.clear(); canvas.getChildren().clear();});
    }

    private void showRules() {
        canvas.setVisible(false);
        rule_pane.setVisible(true);
        
    }

    private void showLevel() {
        canvas.setVisible(true);
        rule_pane.setVisible(false);
        activeCanvas = canvas;
    }

    private void activateType(NodeType type) {
        activeType = type;
        activeTool = NODE;
    }

    /**
     * Loads file and appends elements to canvas
     */
    private void PrepareLoad() {
        File file;
        Stage stage;

        fileChooser.setTitle("Explorer");
        stage = (Stage) window.getScene().getWindow();
        fileChooser.setInitialDirectory(new File("saves"));
        file = fileChooser.showOpenDialog(stage);

        //No file selected, don't do anything
        if (file == null) {return;}

        //Clear before loading in elements
        nodeController.clear();
        canvas.getChildren().clear();

        Pair<ArrayList<Node>,ArrayList<Edge>> pair = FileHandler.LoadNodes(file);
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

    /**
     * Saves level state in file
     */
    private void PrepareSave() {
        String path = JOptionPane.showInputDialog("Save","What is the name of the savefile?");
        if (path == "" || path == null) {
            path = "newfile";
        }
        FileHandler.SaveNodes(nodeController.getNodes(),"saves/"+path);
    }

    private void handlePress(MouseEvent event, Pane c) {
        if(activeTool == NODE){
            Node node = nodeController.addNode(event.getX(), event.getY(), DEFAULT_RADIUS, Color.BLUE);

            c.getChildren().add(node);
        }
    }
}
