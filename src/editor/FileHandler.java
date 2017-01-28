package editor;

import editor.model.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

/**
 * This is to save and/or store nodes & edges
 *
 *
 */

public class FileHandler {

    public static void SaveNodes(ArrayList<editor.model.Node> nodes, String path) {

        try {

            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();

            Element elemNodes = doc.createElement("Nodes");




            doc.appendChild(elemNodes);




            for (editor.model.Node node : nodes) {
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

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (TransformerException ex) {
            System.out.println("Error outputting document");

        } catch (ParserConfigurationException ex) {
            System.out.println("Error building document");
        }
    }
    public static Pair<ArrayList<editor.model.Node>,ArrayList<Edge>> LoadNodes(File file){
        HashMap<Integer,editor.model.Node> NodeMap = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();

        try {

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = documentBuilder.parse(file);
            doc.getDocumentElement().normalize(); //normalizes document


            //Defines all nodes...

            NodeList xnodeList = doc.getElementsByTagName("Node"); //grab all "Node" from XML-file
            for(int i = 0; i < xnodeList.getLength(); i++){
                Node xNode = xnodeList.item(i);



                if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) xNode;

                    //Extract elements of node
                    int id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    editor.model.Node.NodeType type = editor.model.Node.NodeType.valueOf(element.getElementsByTagName("Tag").item(0).getTextContent());
                    double x = Double.parseDouble(element.getElementsByTagName("X").item(0).getTextContent());
                    double y = Double.parseDouble(element.getElementsByTagName("Y").item(0).getTextContent());
                    //Store the extracted Node

                    editor.model.Node node = new editor.model.Node(id,x,y, editor.model.Node.DEFAULT_RADIUS, Color.RED,type);
                    NodeMap.put(id,node);
                }

            }




            //Defines all edges...
            HashMap<Integer,ArrayList<Integer>> edgeMap = new HashMap<>(); //HashMap used for easy fix of duplicate edges.
            xnodeList = doc.getElementsByTagName("Edge");//grab all "Edge" from XML-file
            for(int i = 0; i < xnodeList.getLength(); i++){
                Node xNode = xnodeList.item(i);

                if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) xNode;

                    //Extract elements of node
                    int startID = Integer.parseInt(element.getElementsByTagName("StartID").item(0).getTextContent());
                    int endID =  Integer.parseInt(element.getElementsByTagName("EndID").item(0).getTextContent());
                    if(edgeMap.get(startID)==null) {
                        edgeMap.put(startID, new ArrayList<Integer>());
                    }
                    if (!edgeMap.get(startID).contains(endID)) {
                        //Store the extracted Edge
                        editor.model.Node startNode = NodeMap.get(startID);
                        editor.model.Node endNode = NodeMap.get(endID);

                        edges.add(new editor.model.Edge(startNode, endNode));

                        edgeMap.get(startID).add(endID);
                    }
                }
            }


        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException: ");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("SAXException: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: ");
            e.printStackTrace();
        }

        //Returns the entries as an ArrayList
        Pair pair = new Pair(new ArrayList<>(NodeMap.values()),edges);
        return pair;
    }
    public static String[] LoadTags(String path) {
        String[] tags = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = documentBuilder.parse(new File(path));
            doc.getDocumentElement().normalize(); //normalizes document


            //Defines all nodes...
/*
            NodeList xnodeList = doc.getElementsByTagName("Node"); //grab all "Node" from XML-file
            for (int i = 0; i < xnodeList.getLength(); i++) {
                Node xNode = xnodeList.item(i);


                if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) xNode;

                    //Extract elements of node
                    int id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    editor.model.Node.NodeType type = editor.model.Node.NodeType.valueOf(element.getElementsByTagName("Tag").item(0).getTextContent());
                    double x = Double.parseDouble(element.getElementsByTagName("X").item(0).getTextContent());
                    double y = Double.parseDouble(element.getElementsByTagName("Y").item(0).getTextContent());
                    //Store the extracted Node

                    editor.model.Node node = new editor.model.Node(id, x, y, editor.model.Node.DEFAULT_RADIUS, Color.RED, type);

                }

            }
*/
            NodeList tagNode = doc.getElementsByTagName("Tag");
            int size = tagNode.getLength();
            tags = new String[size];
            for(int i = 0;i < size;i++){
                Node item = tagNode.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    tags[i] = element.getTextContent();

                }
            }

        }
        catch (Exception e){e.printStackTrace();}
        return tags;
    }

    public static void SaveTags(String[] tags, String path){
        try {
            DocumentBuilderFactory documentBuilderFactoryFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = documentBuilderFactoryFact.newDocumentBuilder();
            Document doc = build.newDocument();


            Element elemTags = doc.createElement("Tags");
            for(int i = 0; i < tags.length; i++) {
                Element elemTag = doc.createElement("Tag");
                elemTag.appendChild(doc.createTextNode(tags[i]));
                elemTags.appendChild(elemTag);
            }

            doc.appendChild(elemTags);


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

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
}

