package JGEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObject {
    static ArrayList<GameObject> gameObjects;
    static final GameObject worldCenter = gameObject2D("WorldCenter");

    HashMap<Class, Component> components = new HashMap<>();
    GameObject parent;
    ArrayList<GameObject> children = new ArrayList<>();
    public String name;

    GameObject(String name) {
        this.parent = worldCenter;
        this.name = name;
    }

    public static GameObject gameObject2D(String name) {
        GameObject res = new GameObject(name);
        res.addComponent(new Transform2D());
        return res;
    }

    public static GameObject gameObject2D(String name, Vector2D pos) {
        GameObject res = new GameObject(name);
        res.addComponent(new Transform2D(pos));
        return res;
    }

    public void addChild(GameObject gameObject) {
        if(gameObject.parent != null) {
            gameObject.parent.children.remove(gameObject);
        }
        children.add(gameObject);
        gameObject.parent = this;
    }

    public GameObject getChild(int i) {
        return children.get(i);
    }

    public GameObject getChild(String name) {
        for(GameObject gameObject : children) {
            if(gameObject.name.equals(name)) return gameObject;
        }
        return null;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        if(components.get(type) == null) {
            return null;
        }
        return (T)components.get(type);
    }

    public <T extends Component> void addComponent(T component) {
        if(getComponent(component.getClass()) == null) {
            components.put(component.getClass(), component);
            component.gameObject = this;
            if(Engine.gameStarted) {
                component.onStart();
            }
        }
    }

    void addComponent(Object obj) {
        Component component = (Component)obj;
        addComponent(component);
    }

    public <T extends Component> void removeComponent(Class<T> type) {
        if(type == Transform2D.class) {
            return;
        }
        components.remove(type);
    }

    public static GameObject findGameObject(String name) {
        for(GameObject gameObject : gameObjects) {
            if(gameObject.name.equals(name)) {
                return gameObject;
            }
        }
        return null;
    }
}
