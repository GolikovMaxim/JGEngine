package JGEngine;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameObject {
    static ArrayList<GameObject> gameObjects = null;
    static ArrayList<GameObject> addedGameObjects = null;
    static ArrayList<GameObject> removedGameObjects = null;
    static final GameObject worldCenter = new GameObject();

    HashMap<Class, Component> components = new HashMap<>();
    HashMap<Class, Component> addedComponents = new HashMap<>();
    GameObject parent;
    ArrayList<GameObject> children = new ArrayList<>();
    public String name;
    boolean isActive = true;

    GameObject() {
        this.parent = this;
        addComponent(new Transform2D());
    }

    GameObject(String name) {
        this.parent = worldCenter;
        this.name = name;
        addedGameObjects.add(this);
    }

    public static GameObject gameObject2D(String name) {
        GameObject res = new GameObject(name);
        res.addComponent(new Transform2D());
        res.processAddedComponents();
        return res;
    }

    public static GameObject gameObject2D(String name, Vector2D pos) {
        GameObject res = new GameObject(name);
        res.addComponent(new Transform2D(pos));
        res.processAddedComponents();
        return res;
    }

    public static GameObject UIGameObject2D(String name) {
        GameObject res = new GameObject(name);
        UITransform2D uit = new UITransform2D();
        res.addComponent(uit);
        res.components.put(Transform2D.class, uit);
        res.processAddedComponents();
        return res;
    }

    public static GameObject UIGameObject2D(String name, Vector2D pos) {
        GameObject res = new GameObject(name);
        UITransform2D uit = new UITransform2D(pos);
        res.addComponent(uit);
        res.components.put(Transform2D.class, uit);
        res.processAddedComponents();
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
        if(components.get(type) == null && addedComponents.get(type) == null) {
            return null;
        }
        else if(components.get(type) == null) {
            return (T)addedComponents.get(type);
        }
        return (T)components.get(type);
    }

    public <T extends Component> void addComponent(T component) {
        if(getComponent(component.getClass()) == null) {
            addedComponents.put(component.getClass(), component);
            component.gameObject = this;
        }
    }

    public <T extends Component> void addComponent(Class<T> _class, T component) {
        if(getComponent(_class) == null) {
            addedComponents.put(_class, component);
            component.gameObject = this;
        }
    }

    void addComponent(Object obj) {
        Component component = (Component)obj;
        addComponent(component);
    }

    void processAddedComponents() {
        for(Map.Entry<Class, Component> entry : addedComponents.entrySet()) {
            components.put(entry.getKey(), entry.getValue());
        }
        addedComponents.clear();
    }

    public <T extends Component> void removeComponent(Class<T> type) {
        if(type == Transform2D.class) {
            return;
        }
        components.remove(type);
    }

    public void delete() {
        removedGameObjects.add(this);
    }

    public void setActive(boolean active) {
        if(getComponent(Renderer2D.class) != null) {
            getComponent(Renderer2D.class).setRender(active);
        }
        isActive = active;
    }

    public static GameObject findGameObject(String name) {
        for(GameObject gameObject : gameObjects) {
            if(gameObject.name.equals(name)) {
                return gameObject;
            }
        }
        return null;
    }

    static void processAddedGameObjects() {
        for(GameObject gameObject : addedGameObjects) {
            gameObjects.add(gameObject);
        }
        addedGameObjects.clear();
    }

    static void processRemovedGameObjects() {
        for(GameObject gameObject : removedGameObjects) {
            if(gameObject.components.containsKey(Renderer2D.class)) {
                Platform.runLater(() ->
                        RenderSystem.renderSystem.visibleObjects.getChildren().
                                remove(gameObject.getComponent(Renderer2D.class).imageView)
                );
            }
            gameObjects.remove(gameObject);
        }
        removedGameObjects.clear();
    }
}
