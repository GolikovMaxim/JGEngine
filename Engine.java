package JGEngine;

import javafx.application.Platform;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

public class Engine extends Thread {
    static Engine engine;

    static boolean gameStarted = false;

    Engine() {
        new Input();
        synchronized (GameObject.gameObjects) {
            for(GameObject gameObject : GameObject.gameObjects) {
                for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                    entry.getValue().onStart();
                }
            }
        }
        gameStarted = true;
    }

    void update() {
        Physics.process();
        Input.input.updateKeys();
        synchronized (GameObject.gameObjects) {
            for(GameObject gameObject : GameObject.gameObjects) {
                synchronized (gameObject.components) {
                    for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                        entry.getValue().update();
                    }
                }
            }
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
