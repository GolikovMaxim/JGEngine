package JGEngine;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class RenderSystem {
    public static final RenderSystem renderSystem = new RenderSystem();

    HashMap<GameObject, Boolean> renderGameObjects = new HashMap<>();
    ArrayList<GameObject> uiGameObjects = new ArrayList<>();
    Vector2D windowSize = new Vector2D(640, 480);
    Group visibleObjects = new Group();
    final ArrayList<Runnable> renderings = new ArrayList<>();
    Scene scene;

    private RenderSystem() {

    }

    void render() {
        Platform.runLater(() -> {
            synchronized (renderings) {
                try {
                    for (Runnable runnable : renderings) {
                        runnable.run();
                    }
                }
                catch (ConcurrentModificationException ignored) {

                }
                renderings.clear();
            }
        });
    }

    public float getScreenRatio() {
        return windowSize.y / windowSize.x;
    }

    public void setWindowSize(Vector2D windowSize) {
        Vector2D decorationSize = new Vector2D((float)(Main.stage.getWidth() - this.windowSize.x),
                (float)(Main.stage.getHeight() - this.windowSize.y));
        this.windowSize = windowSize;
        Platform.runLater(() -> {
            Main.stage.setWidth(windowSize.x + decorationSize.x);
            Main.stage.setHeight(windowSize.y + decorationSize.y);
        });
    }

    public void setWindowTitle(String title) {
        Platform.runLater(() -> Main.stage.setTitle(title));
    }

    public void setBackground(Paint color) {
        Platform.runLater(() -> scene.setFill(color));
    }
}
