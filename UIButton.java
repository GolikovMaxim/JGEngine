package JGEngine;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class UIButton extends Component {
    Image idle, active;
    Runnable onClick;
    boolean wasClicked = false;
    String text;
    Font font;


    public UIButton(Image idle, Image active, Runnable onClick, String text) {
        this.idle = idle;
        this.active = active;
        this.onClick = onClick;
        this.text = text;
    }

    @Override
    public void onStart() {
       initialize();
    }

    @Override
    public void update() {
        if(wasClicked) {
            onClick.run();
            wasClicked = false;
        }
    }

    public void initialize() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D(idle));
        }
        else {
            gameObject.getComponent(Renderer2D.class).setImage(idle);
        }
        gameObject.getComponent(Renderer2D.class).uiText = new Label(text);
        gameObject.getComponent(Renderer2D.class).uiText.setAlignment(Pos.CENTER);
        gameObject.getComponent(Renderer2D.class).uiText.setOnMouseClicked(event -> wasClicked = true);
        gameObject.getComponent(Renderer2D.class).uiText.setOnMouseEntered(event -> gameObject.getComponent(Renderer2D.class).setImage(active));
        gameObject.getComponent(Renderer2D.class).uiText.setOnMouseExited(event -> gameObject.getComponent(Renderer2D.class).setImage(idle));
        isStarted = true;
    }

    public void setTextFont(Font font) {
        gameObject.getComponent(Renderer2D.class).uiText.setFont(font);
    }

    public void setTextColor(Paint color) {
        gameObject.getComponent(Renderer2D.class).uiText.setTextFill(color);
    }
}
