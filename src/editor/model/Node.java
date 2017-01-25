package editor.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Node extends Circle {
    private ArrayList<Edge> edges;

    public Node(double x, double y, int radius, Color color){
        super(x, y, radius, color);
        edges = new ArrayList<Edge>();
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
}
