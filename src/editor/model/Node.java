package editor.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Node extends Circle {
    private ArrayList<Edge> edges;
    private int id;
    private static int idCounter=0;
    public static final int DEFAULT_RADIUS = 40;


    public enum NodeType{
        START, END, LOCK, KEY, ROOM
    }
    private NodeType type;

    public Node(double x, double y, int radius, Color color, NodeType type){
        super(x, y, radius, color);
        edges = new ArrayList<Edge>();
        id = idCounter++;
        this.type = type;
        setColor();
    }
    public Node(int id, double x, double y, int radius, Color color, NodeType type){
        this(x, y, radius, color, type);
        this.id = id;
        if(id>=idCounter){
            idCounter = id+1;
        }
    }

    private void setColor() {
        switch (type){
            case END:
                this.setFill(Color.FORESTGREEN);
                break;
            case KEY:
                this.setFill(Color.ORANGE);
                break;
            case LOCK:
                this.setFill(Color.RED);
                break;
            case ROOM:
                this.setFill(Color.PINK);
                break;
            case START:
                this.setFill(Color.DEEPSKYBLUE);
                break;
        }
    }

    public void setPos(double x, double y) {
        super.setCenterX(x);
        super.setCenterY(y);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void updateEdges(){
        for (Edge e : edges) {
            e.updateNodes();
        }
    }

    public void addEdge(Edge e){
        edges.add(e);
    }

    public int getID() {
        return id;
    }
    public NodeType getType(){
        return type;
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }
}
