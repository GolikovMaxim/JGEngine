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
        Main.stage.setScene(RenderSystem.renderSystem.scene);
        Main.stage.show();
        Main.stage.setResizable(false);
        Main.stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        Timer updater = new Timer();
        updater.schedule(new TimerTask() {
            @Override
            public void run() {
                Engine.engine.update();
            }
        }, 0, 17);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
