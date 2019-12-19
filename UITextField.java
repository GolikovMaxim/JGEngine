package JGEngine;

import javafx.scene.control.TextField;

public class UITextField extends Component {
    TextField field;
    Runnable onEnter;
    boolean isAction = false;

    public UITextField(TextField field, Runnable onEnter) {
        this.field = field;
        this.onEnter = onEnter;
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).setField(field);
        field.setOnAction(event -> isAction = true);
    }

    @Override
    public void update() {
        if(isAction) {
            onEnter.run();
            isAction = false;
        }
    }

    public TextField getField() {
        return field;
    }
}
