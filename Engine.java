package JGEngine;

import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;

import java.util.Map;

public class Engine extends Thread {
    static Engine engine;

    static boolean gameStarted = false;

    Engine() {
        new Input();
        RenderSystem.renderSystem.windowSize = new Vector2D(640, 480);
        RenderSystem.renderSystem.scene = new Scene(RenderSystem.renderSystem.visibleObjects,
                640, 480,
                true, SceneAntialiasing.BALANCED);
        RenderSystem.renderSystem.scene.setFill(Color.GRAY);
        gameStarted = true;
    }

    void update() {
        Physics.process();
        Input.input.updateKeys();
        synchronized (GameObject.gameObjects) {
            GameObject.processAddedGameObjects();
            GameObject.processRemovedGameObjects();
            for(int i = 0; i < GameObject.gameObjects.size(); i++) {
                GameObject gameObject = GameObject.gameObjects.get(i);
                if(gameObject.isActive) {
                    gameObject.processAddedComponents();
                    synchronized (gameObject.components) {
                        for (Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                            if (!entry.getValue().isStarted) {
                                entry.getValue().isStarted = true;
                                entry.getValue().onStart();
                            }
                            entry.getValue().update();
                        }
                    }
                }
            }
        }
        RenderSystem.renderSystem.render();
        Time.deltaTime = (System.nanoTime() - Time.lastFrame) / 1000000000f;
        Time.lastFrame = System.nanoTime();
    }
}
