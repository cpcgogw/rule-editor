package editor.model;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/28/17.
 */
public class Pattern {
    public ArrayList<Node> nodes;
    public Pattern(){
        nodes = new ArrayList<Node>();
    }

    @Override
    public int hashCode() {
        return super.hashCode()+nodes.hashCode()*3;
    }

    @Override
    public boolean equals(Object o) {
        if(o.hashCode() == this.hashCode()){
            if(o instanceof Pattern){
                Pattern tmp = (Pattern) o;
                return super.equals(tmp) && nodes.equals(tmp.nodes); // maybe sort lists
            }else{
                return false;
            }
        }else {
            return false;
        }    }
}
