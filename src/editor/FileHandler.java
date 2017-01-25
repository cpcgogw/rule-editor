package editor;

import editor.controller.NodeController;
import editor.model.*;
import javafx.scene.paint.*;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;

/**
 * Created by alija on 2017-01-25.
 */

public class FileHandler {

    public static void SaveNodes(ArrayList<editor.model.Node> nodes) {

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

            // Save the document to the disk file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // format the XML nicely
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            try {
                // location and name of XML file you can change as per need
                FileWriter fos = new FileWriter(((String)JOptionPane.showInputDialog(new

                        JFrame("Save name"), "What's the save name?"))+
                ".xml");
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);

            } catch (IOException e) {

                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        } catch (TransformerException ex) {
            System.out.println("Error outputting document");

        } catch (ParserConfigurationException ex) {
            System.out.println("Error building document");
        }
    }
    public static ArrayList<editor.model.Node> LoadNodes(String path){
        HashMap<Integer,editor.model.Node> NodeMap = new HashMap<>();

        try {

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = documentBuilder.parse(new File(path));
            doc.getDocumentElement().normalize(); //normalizes document


            //Defines all nodes...

            NodeList xnodeList = doc.getElementsByTagName("Node"); //grab all "Node" from XML-file
            for(int i = 0; i < xnodeList.getLength(); i++){
                Node xNode = xnodeList.item(i);



                if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) xNode;

                    //Extract elements of node
                    int id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    String Type = element.getElementsByTagName("Tag").item(0).getTextContent();

                    //Store the extracted Node
                    editor.model.Node node = new editor.model.Node(editor.model.Node.NodeType.valueOf(Type),id);
                    NodeMap.put(id,node);
                }

            }


            //Defines all edges...
            HashMap<Integer,Integer> edgeMap = new HashMap<>(); //HashMap used for easy fix of duplicate edges.
            xnodeList = doc.getElementsByTagName("Edge");//grab all "Edge" from XML-file
            for(int i = 0; i < xnodeList.getLength(); i++){
                Node xNode = xnodeList.item(i);

                if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) xNode;

                    //Extract elements of node
                    int startID = Integer.parseInt(element.getElementsByTagName("StartID").item(0).getTextContent());
                    int endID =  Integer.parseInt(element.getElementsByTagName("EndID").item(0).getTextContent());

                    if(edgeMap.get(startID)==null) {
                        //Store the extracted Edge
                        editor.model.Node startNode = NodeMap.get(startID);
                        editor.model.Node endNode = NodeMap.get(endID);
                        editor.model.Edge edge = new editor.model.Edge(startNode, endNode);

                        edgeMap.put(startID,endID);
                    }
                }
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Returns the entries as an ArrayList
        return  new ArrayList<editor.model.Node>(NodeMap.values());
    }

}
