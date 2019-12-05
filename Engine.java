package JGEngine;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;

import java.util.Map;

public class Engine extends Thread {
    static Engine engine;

    static boolean gameStarted = false;

    Engine() {
        new Input();
        synchronized (GameObject.gameObjects) {
            for(int i = 0; i < GameObject.gameObjects.size(); i++) {
                GameObject gameObject = GameObject.gameObjects.get(i);
                for(int j = 0; j < gameObject.components.entrySet().size(); j++) {
                    Map.Entry<Class, Component> entry =
                            (Map.Entry<Class, Component>)gameObject.components.entrySet().toArray()[i];
                    entry.getValue().onStart();
                    entry.getValue().isStarted = true;
                }
                gameObject.processAddedComponents();
            }
            GameObject.processAddedGameObjects();
            GameObject.processRemovedGameObjects();
        }
        RenderSystem.renderSystem.scene = new Scene(RenderSystem.renderSystem.visibleObjects,
                RenderSystem.renderSystem.windowSize.x, RenderSystem.renderSystem.windowSize.y,
                true, SceneAntialiasing.BALANCED);
        gameStarted = true;
    }

    void update() {
        Physics.process();
        Input.input.updateKeys();
        synchronized (GameObject.gameObjects) {
            for(int i = 0; i < GameObject.gameObjects.size(); i++) {
                GameObject gameObject = GameObject.gameObjects.get(i);
                synchronized (gameObject.components) {
                    for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                        if(!entry.getValue().isStarted) {
                            entry.getValue().isStarted = true;
                            entry.getValue().onStart();
                        }
                        entry.getValue().update();
                    }
                }
                gameObject.processAddedComponents();
            }
            GameObject.processAddedGameObjects();
            GameObject.processRemovedGameObjects();
        }
        Platform.runLater(RenderSystem.renderSystem::render);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Time.deltaTime = (System.nanoTime() - Time.lastFrame) / 1000000000f;
        Time.lastFrame = System.nanoTime();
    }
}
