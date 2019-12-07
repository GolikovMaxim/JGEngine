package JGEngine;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class UILabel extends Component {
    String text;

    public UILabel(String text) {
        this.text = text;
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).uiText = new Label(text);
    }

    public void setTextFont(Font font) {
        gameObject.getComponent(Renderer2D.class).uiText.setFont(font);
    }

    public void setText(String text) {
        this.text = text;
        Platform.runLater(() -> {
            gameObject.getComponent(Renderer2D.class).uiText.setText(text);
        });
    }
}
