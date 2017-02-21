package editor.controller;

import editor.FileHandler;
import editor.model.Edge;
import editor.model.Pattern;
import editor.model.Rule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import editor.model.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
import java.util.HashMap;
import java.util.List;

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
    private Button select_node_button;
    @FXML
    private Button gen_button;

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
    private TabPane rule_tab_pane;
    @FXML
    private Button new_scenario_button;
    private Rule activeRule;


    @FXML
    private MenuItem save_button;

    @FXML
    private MenuItem load_level_button;
    @FXML
    private MenuItem load_rule_button;

    @FXML
    private MenuItem rule_menu_item;

    @FXML
    private MenuItem close_button;

    @FXML
    private MenuItem new_button;

    @FXML
    private MenuItem level_menu_item;

    /**
     * inspector pane
     */
    @FXML
    public static Pane node_inspector_pane;
    @FXML
    public TextField active_node_id_field;
    @FXML
    private Button active_node_save_button;
    public  Node activeNode;

    private HashMap<Pane, Pattern> scenarios;
    private Pattern matchingPattern;
    private Pattern currentLevel;

    public void setActiveNode(Node activeNode) {
        this.activeNode = activeNode;
        active_node_id_field.setText(String.valueOf(activeNode.getNodeId()));
    }


    public tools getActiveTool() {
        return activeTool;
    }

    public NodeType getActiveType(){
        return activeType;
    }

    /**
     * Returns the activeCanvas which is set when the mouse enters a pane we want to draw our graphs in.
     * @return The latest activated canvas.
     */
    public static Pane getActiveCanvas() {
        return activeCanvas;
    }

    /**
     * Enum to keep track of which tool is active
     */
    public enum tools {
        EDGE, NODE, DELETE, MOVE, SELECT
    }
    private FileChooser fileChooser = new FileChooser();
    public static tools activeTool;
    public static NodeType activeType;

    public static Pane activeCanvas;

    private NodeController nodeController;
    public void initialize(){
        scenarios = new HashMap<Pane, Pattern>();
        activeType = NodeType.START;
        activeTool = NODE;
        activeCanvas = rule_canvas;
        currentLevel = new Pattern();

        edge_button.setOnMouseClicked(mouseEvent -> activeTool = EDGE);
        // init editing buttons
        delete_button.setOnMouseClicked(mouseEvent -> activeTool = DELETE);
        move_button.setOnMouseClicked(mouseEvent -> activeTool = MOVE);
        select_node_button.setOnMouseClicked(mouseEvent -> activeTool = SELECT);
        // init node buttons
        start_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.START));
        end_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.END));
        key_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.KEY));
        lock_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.LOCK));
        room_node_button.setOnMouseClicked(mouseEvent -> activateType(NodeType.ROOM));
        // init top menu
        save_button.setOnAction(actionEvent -> PrepareSave());
        load_level_button.setOnAction(actionEvent -> PrepareLoadLevel());
        load_rule_button.setOnAction(actionEvent -> PrepareLoadRule());
        rule_menu_item.setOnAction(actionEvent -> showRules());
        level_menu_item.setOnAction(actionEvent -> showLevel());
        close_button.setOnAction(actionEvent -> Platform.exit());

        //init level canvas
        canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent, canvas));
        canvas.setOnMouseEntered(event -> requestFocus(canvas));

        gen_button.setOnMouseClicked(event -> generateLevel());

        //init rule canvas
        rule_canvas.setOnMouseClicked(mouseEvent -> handlePress(mouseEvent, rule_canvas));
        rule_canvas.setOnMouseEntered(mouseEvent -> requestFocus(rule_canvas));
        new_scenario_button.setOnMouseClicked(mouseEvent -> addTab("new tab"));
        nodeController = new NodeController(this);

        // init inspector pane
        active_node_save_button.setOnMouseClicked(mouseEvent -> saveActiveNode());

        new_button.setOnAction(actionEvent -> {nodeController.clear(); canvas.getChildren().clear(); currentLevel = new Pattern();});

        // initialize currentRule;
        matchingPattern = new Pattern();
        activeRule = new Rule(matchingPattern);
    }

    private void saveActiveNode() {
        activeNode.setNodeId(Integer.parseInt(active_node_id_field.getText()));
    }


    /**
     * Generates a level based of current level canvas content and displays it in the level canvas
     * looks for scenarios/rules in ./saves/rules
     */
    private void generateLevel() {
        File folder = new File("saves/rules");
        ArrayList<Rule> rules = new ArrayList<>();
        //load list of rules
        for (File f : folder.listFiles()) {
            if(!f.isDirectory()){
                Pattern match = new Pattern(FileHandler.LoadMatchingPattern(f));
                rules.add(new Rule(match, FileHandler.LoadTranslations(f)));
            }
        }
        canvas.getChildren().clear();
        //match to current level
        Pattern newLevel = translateLevel(currentLevel, rules);
        if(Rule.DEBUG_MODE){
            System.out.println("Dumping generated level: \n Level: ");
            for (Node n : newLevel.nodes) {
                System.out.println("  node: Type: " + n.getType() + ", id:" + n.getNodeId() + ", #edges: " + n.getEdges().size());
            }
        }
        //display
        currentLevel = newLevel;
        newLevel.resetIds();
        for(Node n : newLevel.nodes){
            //Node node = n.clone();
            nodeController.addNode(n);
            canvas.getChildren().add(n);
            for(Edge e : n.getEdges()){
                Edge c = nodeController.getEdgeController().addEdge(e);
                if(!canvas.getChildren().contains(c.getArrow()))
                    canvas.getChildren().add(c.getArrow());
                if(!canvas.getChildren().contains(c))
                    canvas.getChildren().add(c);
                e.updateNodes();
            }
        }
    }

    private Pattern translateLevel(Pattern pattern, ArrayList<Rule> rules) {
        pattern.findAndReplace(rules);
        return pattern;
    }

    private void showRules() {
        canvas.setVisible(false);
        rule_pane.setVisible(true);

        activeCanvas = rule_canvas;

        System.out.println("Dumping current rule: \n MatchingPattern: ");
        for (Node n : activeRule.matchingPattern.nodes) {
            System.out.println("  node: " + n.getType().toString());
        }
        System.out.println(" possibleOutcomes: ");
        for (Pattern p : activeRule.possibleTranslations){
            System.out.println("  outcome: ");
            for (Node n : p.nodes) {
                System.out.println("   node: " + n.getType().toString());
            }
        }
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
    private void requestFocus(Pane p){
        activeCanvas = p;
    }

    private void PrepareLoadRule() {
        File file;
        Stage stage;

        fileChooser.setTitle("Explorer");
        stage = (Stage) window.getScene().getWindow();
        fileChooser.setInitialDirectory(new File("."));
        file = fileChooser.showOpenDialog(stage);

        //No file selected, don't do anything
        if (file == null) {return;}

        //Clear canavases before loading in elements
        rule_canvas.getChildren().clear();
        rule_tab_pane.getTabs().clear();
        scenarios.clear();

        //new rule
        matchingPattern = new Pattern();
        activeRule = new Rule(matchingPattern);


        Pair<ArrayList<Node>,ArrayList<Edge>> pair = FileHandler.LoadMatchingPattern(file);
        ArrayList<Pair<ArrayList<Node>,ArrayList<Edge>>> translations = FileHandler.LoadTranslations(file);
        activeCanvas = rule_canvas;
        insertIntoCanvasAndList(rule_canvas, matchingPattern.nodes, pair);

        for (Pair<ArrayList<Node>, ArrayList<Edge>> p : translations){

            Pair<Pane, Pattern> panePatternPair = addTab("saved tab");
            insertIntoCanvasAndList(panePatternPair.getKey(), panePatternPair.getValue().nodes, p);
        }
    }

    private void insertIntoCanvasAndList(Pane canvas, ArrayList<Node> nodes, Pair<ArrayList<Node>, ArrayList<Edge>> pair) {
        for(Node node : pair.getKey()){
            nodes.add(node);
            canvas.getChildren().add(node);
        }
        for(Edge e : pair.getValue()){
            Edge c = nodeController.getEdgeController().addEdge(e);
            canvas.getChildren().add(c.getArrow());
            canvas.getChildren().add(c);
        }
    }

    /**
     * Loads file and appends elements to canvas
     */
    private void PrepareLoadLevel() {
        File file;
        Stage stage;

        fileChooser.setTitle("Explorer");
        stage = (Stage) window.getScene().getWindow();
        fileChooser.setInitialDirectory(new File("."));
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
        if(rule_pane.isVisible()){
            activeRule.save(path);
        }else {
            FileHandler.SaveNodes(nodeController.getNodes(), "saves/" + path);
        }
    }

    private void handlePress(MouseEvent event, Pane c) {
        if(activeTool == NODE){
            Node node = nodeController.addNode(event.getX(), event.getY(), DEFAULT_RADIUS, Color.BLUE);

            c.getChildren().add(node);
            if(c == rule_canvas){
                matchingPattern.nodes.add(node);
            }else if(c == canvas){
                currentLevel.nodes.add(node);
            } else {
                scenarios.get(c).nodes.add(node);
            }
        }
    }
    private Pair<Pane, Pattern> addTab(String s){
        Tab tab = new Tab(s);
        Pane c = new Pane();
        c.setOnMouseClicked(mouseEvent -> {
            activeCanvas = c;
            handlePress(mouseEvent, c);
        });
        c.setOnMouseEntered(event -> requestFocus(c));
        tab.setContent(c);
        rule_tab_pane.getTabs().add(tab);
        Pattern p = new Pattern();
        activeRule.possibleTranslations.add(p);
        scenarios.put(c, p);
        return new Pair<>(c, p);
    }
}
