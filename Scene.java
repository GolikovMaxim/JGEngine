package JGEngine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class Scene {
    private static final String SCENE_NODE_NAME = "scene";
    private static final String GAMEOBJECT_NODE_NAME = "gameObject";
    private static final String COMPONENT_NODE_NAME = "component";

    ArrayList<GameObject> gameObjects = new ArrayList<>();
    String name;

    Scene(File sceneFile) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xml = documentBuilder.parse(sceneFile);
            Element root = xml.getDocumentElement();
            if(!root.getTagName().equals(SCENE_NODE_NAME)) {
                throw new Exception();
            }
            name = root.getAttribute("name");
            NodeList sceneContent = root.getChildNodes();
            for(int i = 0; i < sceneContent.getLength(); i++) {
                Node node = sceneContent.item(i);
                if(node.getNodeName().equals(GAMEOBJECT_NODE_NAME)) {
                    NodeList gameObjectNodes = node.getChildNodes();
                    createGameObjectByNodes(gameObjectNodes, node.getAttributes().getNamedItem("name").getNodeValue());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GameObject createGameObjectByNodes(NodeList nodes, String name)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        GameObject gameObject = GameObject.gameObject2D(name);
        gameObjects.add(gameObject);
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getNodeName().equals(COMPONENT_NODE_NAME)) {
                gameObject.addComponent(Class.forName(node.getTextContent()).newInstance());
            }
            else if(node.getNodeName().equals(GAMEOBJECT_NODE_NAME)) {
                NodeList gameObjectNodes = node.getChildNodes();
                gameObject.addChild(createGameObjectByNodes(gameObjectNodes,
                        node.getAttributes().getNamedItem("name").getNodeValue()));
            }
        }
        return gameObject;
    }
}
