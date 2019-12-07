package JGEngine;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;

public class Renderer2D extends Component {
    ImageView imageView;
    Label uiText = null;
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

    public Image getImage() {
        return imageView.getImage();
    }

    @Override
    public void onStart() {
        RenderSystem.renderSystem.renderGameObjects.put(gameObject, false);
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
                    }
                    else {
                        if(isVisible) {
                            isVisible = false;
                            for(Map.Entry<Class, Component> entry : gameObject.components.entrySet()) {
                                entry.getValue().onBecomeInvisible();
                            }
                            RenderSystem.renderSystem.visibleObjects.getChildren().remove(imageView);
                            if(uiText != null) {
                                RenderSystem.renderSystem.visibleObjects.getChildren().remove(uiText);
                            }
                        }
                    }
                }
            });
        }
    }

    public void setRender(boolean _render) {
        if(render && !_render) {
            Platform.runLater(() -> {
                RenderSystem.renderSystem.visibleObjects.getChildren().remove(imageView);
                if(uiText != null) {
                    RenderSystem.renderSystem.visibleObjects.getChildren().remove(uiText);
                }
            });
        }
        render = _render;
    }

    @Override
    public void onDestroy() {
        RenderSystem.renderSystem.renderGameObjects.remove(gameObject);
    }
}
