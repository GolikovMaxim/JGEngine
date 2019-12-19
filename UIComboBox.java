package JGEngine;

import javafx.scene.control.ComboBox;

public class UIComboBox extends Component {
    ComboBox box;
    Runnable onEnter;
    boolean isAction = false;

    public UIComboBox(ComboBox box, Runnable onEnter) {
        this.box = box;
        this.onEnter = onEnter;
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).comboBox = box;
        box.setOnAction(event -> isAction = true);
        box.setValue(box.getItems().get(0));
    }

    @Override
    public void update() {
        if(isAction) {
            onEnter.run();
            isAction = false;
        }
    }

    public Object getFromBox() {
        return box.getValue();
    }
}
