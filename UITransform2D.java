package JGEngine;

public class UITransform2D extends Transform2D {
    UITransform2D() {
        this.zFactor = -10;
        this.scale = new Vector2D(0.1f, 0.1f);
    }

    UITransform2D(Vector2D pos) {
        this();
        position = pos;
    }

    public Vector2D getGlobalPosition() {
        return position;
    }

    public float getGlobalRotation() {
        return rotation;
    }

    public Vector2D getGlobalScale() {
        return scale;
    }
}
