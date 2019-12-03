package JGEngine;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class RenderSystem {
    public static final RenderSystem renderSystem = new RenderSystem();

    HashMap<GameObject, Boolean> renderGameObjects = new HashMap<>();
    Vector2D windowSize = new Vector2D(640, 480);
    Group visibleObjects = new Group();
    Scene scene = new Scene(visibleObjects, windowSize.x, windowSize.y, true, SceneAntialiasing.BALANCED);

    private RenderSystem() {

    }

    void render() {
        if(Camera2D.main == null) {
            return;
        }
        synchronized (GameObject.gameObjects) {
            for(GameObject gameObject : GameObject.gameObjects) {
                if(Camera2D.main.isVisible(gameObject)) {
                    if(renderGameObjects.get(gameObject) != null && !renderGameObjects.get(gameObject)) {
                        visibleObjects.getChildren().add(gameObject.getComponent(Renderer2D.class).imageView);
                        for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                            entry.getValue().onBecomeVisible();
                        }
                    }
                    renderGameObjects.replace(gameObject, true);
                }
                else {
                    if(renderGameObjects.get(gameObject) != null && renderGameObjects.get(gameObject)) {
                        visibleObjects.getChildren().remove(gameObject.getComponent(Renderer2D.class).imageView);
                        for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                            entry.getValue().onBecomeInvisible();
                        }
                    }
                    renderGameObjects.replace(gameObject, false);
                }
            }
        }
        for(Map.Entry<GameObject, Boolean> entry : renderGameObjects.entrySet()) {
            if(entry.getValue() && entry.getKey().getComponent(Renderer2D.class) != null) {
                ImageView renderView = entry.getKey().getComponent(Renderer2D.class).imageView;
                if(renderView == null) {
                    continue;
                }
                Vector2D position = Camera2D.main.worldSpaceToScreen(entry.getKey());
                float rotation = Camera2D.main.worldRotationToScreen(entry.getKey());
                Vector2D scale = Camera2D.main.worldScaleToScreen(entry.getKey());
                renderView.setX(position.x - scale.x / 2);
                renderView.setY(position.y - scale.y / 2);
                renderView.setFitWidth(scale.x);
                renderView.setFitHeight(scale.y);
                renderView.setRotate(-rotation * Mathf.radian);
                renderView.setTranslateZ(entry.getKey().getComponent(Transform2D.class).zFactor);
            }
        }
        synchronized (Engine.engine) {
            Engine.engine.notify();
        }
    }

    public float getScreenRatio() {
        return windowSize.y / windowSize.x;
    }

    public void setWindowTitle(String title) {
        Main.stage.setTitle(title);
    }
}
