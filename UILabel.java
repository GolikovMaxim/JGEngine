package JGEngine;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class UILabel extends Component {
    Label label;

    public UILabel(String text) {
        this.label = new Label(text);
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).uiText = label;
    }

    public void setTextFont(Font font) {
        Platform.runLater(() -> label.setFont(font));
    }

    public void setText(String text) {
        Platform.runLater(() -> label.setText(text));
    }
}
