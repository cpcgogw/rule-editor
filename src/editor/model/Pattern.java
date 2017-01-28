package editor.model;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/28/17.
 */
public class Pattern {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;
    public Pattern(){
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }
}
