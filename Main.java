package JGEngine;

import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        SceneManager.sceneManager.loadScene(0);
        stage.addEventHandler(KeyEvent.ANY, event ->
                Input.getInput().addKey(event.getCode(), !event.getEventType().equals(KeyEvent.KEY_RELEASED)));
        Engine.engine = new Engine();
        Timer updater = new Timer();
        updater.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Engine.engine.update();
            }
        }, 0, 17);
        Main.stage.setScene(RenderSystem.renderSystem.scene);
        Main.stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
