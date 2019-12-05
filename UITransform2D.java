package JGEngine;

public class UITransform2D extends Transform2D {
    UITransform2D() {
        this.zFactor = Integer.MIN_VALUE;
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
