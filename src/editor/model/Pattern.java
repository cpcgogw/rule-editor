package editor.model;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/28/17.
 */
public class Pattern {
    public ArrayList<Node> nodes;
    public Pattern(){
        nodes = new ArrayList<Node>();
    }
    public Pattern(Pair<ArrayList<Node>,ArrayList<Edge>> pair){
        nodes = new ArrayList<>();
        nodes.addAll(pair.getKey());
    }
    @Override
    public int hashCode() {
        return super.hashCode()+nodes.hashCode()*3;
    }

    @Override
    public boolean equals(Object o) {
            if(o instanceof Pattern){
                Pattern tmp = (Pattern) o;
                return nodes.equals(tmp.nodes); // maybe sort lists
            }else{
                return false;
            }
    }

    public void findAndReplace(ArrayList<Rule> rules) {
        this.resetIds();
        for (Rule r : rules) {
            for (int i = 0; i < nodes.size(); i++) {
                if(Rule.DEBUG_MODE) {
                    System.out.println("checking subpattern:");
                    System.out.println(" Type: " + nodes.get(i).getType() + ", id:" + nodes.get(i).getNodeId() + ", #edges: " + nodes.get(i).getEdges().size());
                    System.out.println("vs");
                    for (Node n :
                            r.matchingPattern.nodes) {
                        System.out.println(" Type: " + n.getType() + ", id:" + n.getNodeId() + ", #edges: " + n.getEdges().size());
                    }
                }
                Pattern p = new Pattern();
                boolean result = r.nodeContainsSubPattern(nodes.get(i), new ArrayList<Node>(), p);
                if(result){
                    r.replace(p);
                    r.addAllNotIn(this, p);
                }
                if(Rule.DEBUG_MODE) {
                    System.out.println(result);
                    System.out.println("found: ");
                    for (Node n :
                            p.nodes) {
                        System.out.println(" Type: " + n.getType() + ", id:" + n.getNodeId() + ", #edges: " + n.getEdges().size());
                    }
                }
                 if(result)
                     return;
            }
        }
    }

    public void resetIds() {
        for (Node node :
                nodes) {
            node.setNodeId((node.getNodeId()+3)* 2);
        }
    }
}
