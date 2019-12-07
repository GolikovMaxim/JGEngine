package JGEngine;

import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

public class SceneManager {
    private static final String scenesFile = ".scenes";
    private static final String sceneExtension = ".jgescene";
    public static final SceneManager sceneManager = new SceneManager();

    File scenesList;
    ArrayList<String> sceneFileNames = new ArrayList<>();
    ExtensionFilter extensionFilter = new ExtensionFilter(sceneExtension);
    int currentScene;

    SceneManager() {
        Scanner scanner = null;
        try {
            findScenesFile();
            scanner = new Scanner(new FileInputStream(scenesList));
            while (scanner.hasNextLine()) {
                try {
                    sceneFileNames.add(scanner.nextLine());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Wrong scene file");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot load scenes");
        }
    }

    private void findScenesFile() throws Exception {
        File projectDirectory = new File("./src");
        for (String dirName : projectDirectory.list()) {
            File directory = new File(projectDirectory.getAbsolutePath() + "/" + dirName);
            for (String fileName : directory.list()) {
                if (fileName.equals(scenesFile)) {
                    scenesList = new File(directory.getAbsolutePath() + "/" + fileName);
                    break;
                }
            }
        }
    }

    private Scene getScene(String fileName) throws Exception {
        if(!extensionFilter.accept(null, fileName)) {
            throw new Exception();
        }
        File sceneFile = new File("./src/" + fileName);
        return new Scene(sceneFile);
    }

    public void loadScene(int i) {
        currentScene = i;
        if(GameObject.gameObjects != null) {
            Platform.runLater(() -> RenderSystem.renderSystem.visibleObjects.getChildren().clear());
        }
        Scene scene = null;
        try {
            scene = getScene(sceneFileNames.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GameObject.gameObjects = scene.gameObjects;
        GameObject.addedGameObjects = scene.addedGameObjects;
        GameObject.removedGameObjects = scene.removedGameObjects;
        RenderSystem.renderSystem.setBackground(Color.GRAY);
    }

    private static class ExtensionFilter implements FilenameFilter {
        private String extension;

        public ExtensionFilter(String extension) {
            this.extension = extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(extension);
        }
    }
}
