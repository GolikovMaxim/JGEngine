package JGEngine;

public class Camera2D extends Component {
    static Camera2D main = null;

    float orthographicWidth;
    Vector2D orthographicSize;

    public Camera2D() {
        orthographicWidth = 10;
        orthographicSize = new Vector2D(orthographicWidth,
                orthographicWidth * RenderSystem.renderSystem.getScreenRatio());
    }

    public Camera2D(float orthographicWidth) {
        this();
        this.orthographicWidth = orthographicWidth;
        orthographicSize = new Vector2D(orthographicWidth,
                orthographicWidth * RenderSystem.renderSystem.getScreenRatio());
    }

    public static Camera2D getMain() {
        return main;
    }

    public void setOrthographicWidth(float orthographicWidth) {
        this.orthographicWidth = orthographicWidth;
        orthographicSize = new Vector2D(orthographicWidth,
                orthographicWidth * RenderSystem.renderSystem.getScreenRatio());
    }

    public boolean isVisible(GameObject gameObject) {
        if(gameObject.getComponent(UITransform2D.class) != null) {
            return true;
        }
        Rect2D camRect = new Rect2D(this);
        Rect2D objRect = new Rect2D(gameObject.getComponent(Transform2D.class));
        return Rect2D.isIntersected(camRect, objRect);
    }

    public Vector2D worldSpaceToScreen(GameObject gameObject) {
        Vector2D globalPosition, leftUpVert;
        if(gameObject.getComponent(UITransform2D.class) != null) {
            return Vector2D.vector2Dmul(
                    gameObject.getComponent(Transform2D.class).getGlobalPosition(),
                    RenderSystem.renderSystem.windowSize
            );
        }
        globalPosition = gameObject.getComponent(Transform2D.class).getGlobalPosition();
        Rect2D cameraRect = new Rect2D(this);
        Rect2D notRotated = new Rect2D(cameraRect.position, cameraRect.size, 0);
        globalPosition.rotateAround(notRotated.position, -cameraRect.rotation);
        leftUpVert = notRotated.points[1];
        return Vector2D.vector2Dmul(
                Vector2D.vector2Ddiv(
                        Vector2D.sub(globalPosition, leftUpVert),
                        Vector2D.vector2Dmul(
                                orthographicSize,
                                new Vector2D(1, -1)
                        )
                ),
                RenderSystem.renderSystem.windowSize);
    }

    public float worldRotationToScreen(GameObject gameObject) {
        return gameObject.getComponent(Transform2D.class).getGlobalRotation() -
                this.gameObject.getComponent(Transform2D.class).getGlobalRotation();
    }

    public Vector2D worldScaleToScreen(GameObject gameObject) {
        if(gameObject.getComponent(UITransform2D.class) != null) {
            return Vector2D.mul(gameObject.getComponent(Transform2D.class).getGlobalScale(),
                    RenderSystem.renderSystem.windowSize.x);
        }
        return Vector2D.vector2Dmul(Vector2D.vector2Ddiv(gameObject.getComponent(Transform2D.class).getGlobalScale(),
                orthographicSize), RenderSystem.renderSystem.windowSize);
    }

    public void setMain() {
        main = this;
    }
}
