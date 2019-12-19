package JGEngine;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class UIButton extends Component {
    Image idle, active;
    Label label;
    Runnable onClick;
    boolean wasClicked = false;
    String text;
    Font font;


    public UIButton(Image idle, Image active, Runnable onClick, String text) {
        this.idle = idle;
        this.active = active;
        this.onClick = onClick;
        this.text = text;
        label = new Label(text);
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D(idle));
        }
        else {
            gameObject.getComponent(Renderer2D.class).setImage(idle);
        }
        gameObject.getComponent(Renderer2D.class).uiText = label;
        label.setAlignment(Pos.CENTER);
        label.setOnMouseClicked(event -> wasClicked = true);
        label.setOnMouseEntered(event -> gameObject.getComponent(Renderer2D.class).setImage(active));
        label.setOnMouseExited(event -> gameObject.getComponent(Renderer2D.class).setImage(idle));
    }

    @Override
    public void update() {
        if(wasClicked) {
            onClick.run();
            wasClicked = false;
        }
    }

    public void setTextFont(Font font) {
        label.setFont(font);
    }

    public void setTextColor(Paint color) {
        label.setTextFill(color);
    }
}
