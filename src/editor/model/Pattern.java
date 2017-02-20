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
        for (Rule r : rules) {
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i; j <= nodes.size(); j++) {
                    Pattern subPattern = new Pattern();
                    subPattern.nodes.addAll(nodes.subList(i,j));
                    System.out.println(nodes.size());
                    System.out.println(nodes.subList(i,j).size());
                    System.out.println("checking subpattern:");
                    for (Node n :
                            subPattern.nodes) {
                        System.out.println(" Type: " + n.getType() + ", id:" + n.getNodeId());
                    }
                    System.out.println("vs: ");
                    for (Node n :
                            r.matchingPattern.nodes) {
                        System.out.println(" Type: " + n.getType() + ", id:" + n.getNodeId());
                    }
                    if(r.matches(subPattern)){
                        Pattern p = r.matchAndReplace(subPattern);
                        ArrayList<Node> tmp = new ArrayList<>();
                        tmp.addAll(nodes.subList(0,i));
                        tmp.addAll(p.nodes);
                        tmp.addAll(nodes.subList(j,nodes.size()));
                        this.nodes = tmp;
                        System.out.println("adding nodes; " + p.nodes.size());
                        i+=p.nodes.size();
                        j+=p.nodes.size();
                        if(i>nodes.size() || j>nodes.size()){
                            break;
                        }
                    }
                }
            }
        }
    }
}
