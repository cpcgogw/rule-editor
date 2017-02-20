package editor.model;


import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vilddjur on 1/28/17.
 */
public class Rule {
    public Pattern matchingPattern;
    public ArrayList<Pattern> possibleTranslations;
    private Random rand;
    public Rule(Pattern matchingPattern){
        rand = new Random();
        this.matchingPattern = matchingPattern;
        possibleTranslations = new ArrayList<Pattern>();
    }

    public Rule(Pattern match, ArrayList<Pair<ArrayList<Node>, ArrayList<Edge>>> translations) {
        this.matchingPattern = match;
        possibleTranslations = new ArrayList<Pattern>();
        rand = new Random();
        for (Pair<ArrayList<Node>, ArrayList<Edge>> pair : translations) {
            Pattern p = new Pattern(pair);
            possibleTranslations.add(p);
        }
    }

    public void save(String path){
        try {
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();

            //<Rule></Rule>
            Element elemRule = doc.createElement("Rule");
            doc.appendChild(elemRule);

            //<Rule><MatchingPattern></MatchingPattern></Rule>
            Element elemMatchingPattern = doc.createElement("MatchingPattern");
            elemRule.appendChild(elemMatchingPattern);
            //<Rule><MatchingPattern><Pattern></Pattern></MatchingPattern></Rule>
            Element elemNodes = doc.createElement("Pattern");
            elemMatchingPattern.appendChild(elemNodes);
            //<Rule><MatchingPattern><Pattern>...</Pattern></MatchingPattern></Rule>
            insertNodesInto(matchingPattern.nodes, elemNodes, doc);


            //<Rule><PossibleTranslations></PossibleTranslations></Rule>
            Element elemPosTranslations = doc.createElement("PossibleTranslations");
            elemRule.appendChild(elemPosTranslations);
            //<Rule><PossibleTranslations><Pattern></Pattern>....</PossibleTranslations></Rule>
            for (Pattern p : possibleTranslations) {
                Element elemSinglePattern = doc.createElement("Pattern");
                elemPosTranslations.appendChild(elemSinglePattern);
                //<Rule><PossibleTranslations><Pattern>...</Pattern>[..]</PossibleTranslations></Rule>
                insertNodesInto(p.nodes, elemSinglePattern, doc);
            }

            /* Saves File at specific directory
                */

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            //formatting for human readability.
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            try {
                FileWriter fileWriter = new FileWriter(path);
                StreamResult streamResult = new StreamResult(fileWriter);
                transformer.transform(source, streamResult);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (TransformerException ex) {
            System.out.println("Error outputting document");
        } catch (ParserConfigurationException ex) {
            System.out.println("Error building document");
        } catch (Exception e) {
            System.out.println("Rule::save(String): There was an error saving the Rule: ");
            e.printStackTrace();
        }
    }

    private void insertNodesInto(ArrayList<Node> nodes, Element elemNodes, Document doc) {
        //<Node><ID>id</ID><Tag>tag</Tag><X>x</X><Y>y</Y></Node>
        for (Node node : nodes) {
            Element elemNode = doc.createElement("Node");
            elemNodes.appendChild(elemNode);

            Element elemId = doc.createElement("ID");
            elemId.appendChild(doc.createTextNode(String.valueOf(node.getID())));
            elemNode.appendChild(elemId);

            Element elemTag = doc.createElement("Tag");
            elemTag.appendChild(doc.createTextNode(String.valueOf(node.getType())));
            elemNode.appendChild(elemTag);

            Element elemX = doc.createElement("X");
            elemX.appendChild(doc.createTextNode(String.valueOf(node.getCenterX())));
            elemNode.appendChild(elemX);

            Element elemY = doc.createElement("Y");
            elemY.appendChild(doc.createTextNode(String.valueOf(node.getCenterY())));
            elemNode.appendChild(elemY);

            Element elemEdges = doc.createElement("Edges");
            elemNode.appendChild(elemEdges);
            for (Edge edge : node.getEdges()) {
                Element elemEdge = doc.createElement("Edge");
                elemEdges.appendChild(elemEdge);

                Element elemStart = doc.createElement("StartID");
                elemStart.appendChild(doc.createTextNode(String.valueOf(edge.getStartNode().getID())));
                elemEdge.appendChild(elemStart);

                Element elemEnd = doc.createElement("EndID");
                elemEnd.appendChild(doc.createTextNode(String.valueOf(edge.getEndNode().getID())));
                elemEdge.appendChild(elemEnd);
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if(o.hashCode() == this.hashCode()){
            if(o instanceof Rule){
                Rule tmp = (Rule) o;
                return super.equals(tmp)
                        && tmp.matchingPattern.equals(this.matchingPattern)
                        && tmp.possibleTranslations.equals(this.possibleTranslations); // maybe sort the lists.
            }else{
                return false;
            }

        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += matchingPattern.hashCode()*3;
        code += possibleTranslations.hashCode()*5;
        return super.hashCode() + code;
    }

    public Pattern matchAndReplace(Pattern p) {
        if(p.equals(matchingPattern)){
            p = randomPossiblePattern();
        }
        return p;
    }
    public boolean matches(Pattern p){
        return p.equals(matchingPattern);
    }

    private Pattern randomPossiblePattern() {
        return possibleTranslations.get(rand.nextInt(possibleTranslations.size()));
    }
}
