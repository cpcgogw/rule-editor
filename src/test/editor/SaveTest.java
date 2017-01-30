package editor;

import editor.model.Edge;
import editor.model.Node;
import editor.model.Pattern;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by vilddjur on 1/30/17.
 */
class SaveTest {
    private static final int NUM_OF_NODES_IN_PATTERN = 7;
    private static final List<Node.NodeType> TYPES =
            Collections.unmodifiableList(Arrays.asList(Node.NodeType.values()));
    private static final int NUM_OF_EDGES_IN_PATTERN = 10;
    private Random random;

    @BeforeAll
    void init(){
        random = new Random();
    }
    @Test
    void saveRule() {

    }
    @Test
    void saveLevel() {
        //create pattern
        Pattern pattern = new Pattern();
        pattern.nodes.addAll(generateNodes());
        //save pattern
        String path = "test/saveLevelTest.xml";
        FileHandler.SaveNodes(pattern.nodes, path);
        //load pattern
        Pattern pattern2 = new Pattern();
        ArrayList<Node> nodes = new ArrayList<Node>();
        Pair<ArrayList<Node>, ArrayList<Edge>> pair = FileHandler.LoadNodes(new File(path));
        nodes = pair.getKey();
        pattern2.nodes.addAll(nodes);
        //check that they are the same
        assertEquals(pattern, pattern2);
    }

    private ArrayList<Node> generateNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0;i<NUM_OF_NODES_IN_PATTERN;i++){
            Node n = new Node(Math.random()*40, Math.random()*40, Node.DEFAULT_RADIUS, Color.AQUA, TYPES.get(random.nextInt(TYPES.size())));
            nodes.add(n);
        }
        for (int i = 0; i < NUM_OF_EDGES_IN_PATTERN; i++) {
            Edge e = new Edge(nodes.get(random.nextInt(nodes.size())), nodes.get(random.nextInt(nodes.size())));
        }
        return nodes;
    }
}