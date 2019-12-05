package JGEngine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Renderer2D extends Component {
    ImageView imageView;

    public Renderer2D() {
        this.imageView = null;
    }

    public Renderer2D(Image renderLook) {
        this.imageView = new ImageView(renderLook);
    }

    public void setImage(Image renderLook) {
        imageView.setImage(renderLook);
    }

    @Override
    public void onStart() {
        RenderSystem.renderSystem.renderGameObjects.put(gameObject, false);
    }

    @Override
    public void onDestroy() {
        RenderSystem.renderSystem.renderGameObjects.remove(gameObject);
    }
}
