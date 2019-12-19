package JGEngine;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;

public class Renderer2D extends Component {
    ImageView imageView;
    Label uiText = null;
    TextField field = null;
    ListView scrollPane = null;
    ComboBox comboBox = null;

    boolean isVisible = false;
    boolean render = true;

    public Renderer2D() {
        this.imageView = new ImageView();
    }

    public Renderer2D(Image renderLook) {
        this.imageView = new ImageView(renderLook);
    }

    public void setImage(Image renderLook) {
        imageView.setImage(renderLook);
    }

    public void setImageEffect(Effect effect) {
        imageView.setEffect(effect);
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setField(TextField field) {
        this.field = field;
    }

    public void setListView(ListView scrollPane) {
        this.scrollPane = scrollPane;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void update() {
        synchronized (RenderSystem.renderSystem.renderings) {
            RenderSystem.renderSystem.renderings.add(() -> {
                if(render) {
                    if(Camera2D.main.isVisible(gameObject)) {
                        if(!isVisible) {
                            isVisible = true;
                            for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                                entry.getValue().onBecomeVisible();
                            }
                            RenderSystem.renderSystem.visibleObjects.getChildren().add(imageView);
                            if(uiText != null) {
                                RenderSystem.renderSystem.visibleObjects.getChildren().add(uiText);
                            }
                            if(field != null) {
                                RenderSystem.renderSystem.visibleObjects.getChildren().add(field);
                            }
                            if(comboBox != null) {
                                RenderSystem.renderSystem.visibleObjects.getChildren().add(comboBox);
                            }
                            if(scrollPane != null) {
                                RenderSystem.renderSystem.visibleObjects.getChildren().add(scrollPane);
                            }
                        }
                        Vector2D position = Camera2D.main.worldSpaceToScreen(gameObject);
                        float rotation = Camera2D.main.worldRotationToScreen(gameObject);
                        Vector2D scale = Camera2D.main.worldScaleToScreen(gameObject);
                        imageView.setX(position.x - scale.x / 2);
                        imageView.setY(position.y - scale.y / 2);
                        imageView.setFitWidth(scale.x);
                        imageView.setFitHeight(scale.y);
                        imageView.setRotate(-rotation * Mathf.radian);
                        imageView.setTranslateZ(gameObject.getComponent(Transform2D.class).zFactor);
                        if(uiText != null) {
                            uiText.setLayoutX(position.x - scale.x / 2);
                            uiText.setLayoutY(position.y - scale.y / 2);
                            uiText.setPrefWidth(scale.x);
                            uiText.setPrefHeight(scale.y);
                            uiText.setRotate(-rotation * Mathf.radian);
                            uiText.setTranslateZ(gameObject.getComponent(Transform2D.class).zFactor - 1);
                        }
                        if(field != null) {
                            field.setLayoutX(position.x - scale.x / 2);
                            field.setLayoutY(position.y - scale.y / 2);
                            field.setPrefWidth(scale.x);
                            field.setPrefHeight(scale.y);
                            field.setRotate(-rotation * Mathf.radian);
                            field.setTranslateZ(gameObject.getComponent(Transform2D.class).zFactor - 1);
                        }
                        if(scrollPane != null) {
                            scrollPane.setLayoutX(position.x - scale.x / 2);
                            scrollPane.setLayoutY(position.y - scale.y / 2);
                            scrollPane.setPrefWidth(scale.x);
                            scrollPane.setPrefHeight(scale.y);
                            scrollPane.setRotate(-rotation * Mathf.radian);
                            scrollPane.setTranslateZ(gameObject.getComponent(Transform2D.class).zFactor - 1);
                        }
                        if(comboBox != null) {
                            comboBox.setLayoutX(position.x - scale.x / 2);
                            comboBox.setLayoutY(position.y - scale.y / 2);
                            comboBox.setPrefWidth(scale.x);
                            comboBox.setPrefHeight(scale.y);
                            comboBox.setRotate(-rotation * Mathf.radian);
                            comboBox.setTranslateZ(gameObject.getComponent(Transform2D.class).zFactor - 1);
                        }
                    }
                    else {
                        if(isVisible) {
                            isVisible = false;
                            for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                                entry.getValue().onBecomeInvisible();
                            }
                            deleteRenders();
                        }
                    }
                }
            });
        }
    }

    private void deleteRenders() {
        RenderSystem.renderSystem.visibleObjects.getChildren().remove(imageView);
        if(uiText != null) {
            RenderSystem.renderSystem.visibleObjects.getChildren().remove(uiText);
        }
        if(field != null) {
            RenderSystem.renderSystem.visibleObjects.getChildren().remove(field);
        }
        if(scrollPane != null) {
            RenderSystem.renderSystem.visibleObjects.getChildren().remove(scrollPane);
        }
        if(comboBox != null) {
            RenderSystem.renderSystem.visibleObjects.getChildren().remove(comboBox);
        }
    }

    public void setRender(boolean _render) {
        if(render && !_render) {
            Platform.runLater(this::deleteRenders);
        }
        render = _render;
    }

    @Override
    public void onDestroy() {
        setRender(false);
    }
}
