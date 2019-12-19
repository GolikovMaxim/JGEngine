package JGEngine;

import javafx.scene.image.Image;

public class UIImage extends Component {
    Image image;

    public UIImage(Image image) {
        this.image = image;
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).setImage(image);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        gameObject.getComponent(Renderer2D.class).setImage(image);
    }
}
