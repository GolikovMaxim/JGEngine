package JGEngine;

public class Transform2D extends Component {
    public Vector2D position;
    float rotation;
    public Vector2D scale;
    public float zFactor = 0;

    Transform2D() {
        position = new Vector2D();
        rotation = 0;
        scale = new Vector2D(1, 1);
    }

    Transform2D(Vector2D pos) {
        this();
        position = pos;
    }

    Transform2D(Vector2D pos, float rot) {
        this();
        position = pos;
        rotation = rot;
    }

    Transform2D(Vector2D pos, float rot, Vector2D scl) {
        this();
        position = pos;
        rotation = rot;
        scale = scl;
    }

    public void rotate(float angle) {
        setRotation(rotation + angle);
    }

    public void translate(Vector2D distance) {
        position.add(distance);
    }

    public void setRotation(float angle) {
        rotation = (angle % 360 + 360) % 360;
    }

    public Vector2D getGlobalPosition() {
        if(gameObject == GameObject.worldCenter) {
            return position;
        }
        return Vector2D.add(position, gameObject.parent.getComponent(Transform2D.class).position);
    }

    public float getGlobalRotation() {
        if(gameObject == GameObject.worldCenter) {
            return rotation;
        }
        return rotation + gameObject.parent.getComponent(Transform2D.class).rotation;
    }

    public Vector2D getGlobalScale() {
        if(gameObject == GameObject.worldCenter) {
            return scale;
        }
        return Vector2D.vector2Dmul(scale, gameObject.parent.getComponent(Transform2D.class).scale);
    }

    public void setGlobalPosition(Vector2D vec) {
        if(gameObject == GameObject.worldCenter) {
            position = vec;
            return;
        }
        position = Vector2D.sub(vec, gameObject.parent.getComponent(Transform2D.class).getGlobalPosition());
    }

    public void setGlobalRotation(float angle) {
        if(gameObject == GameObject.worldCenter) {
            rotation = angle;
            return;
        }
        setRotation(angle - gameObject.parent.getComponent(Transform2D.class).getGlobalRotation());
    }

    public void setGlobalScale(Vector2D scale) {
        if(gameObject == GameObject.worldCenter) {
            this.scale = scale;
            return;
        }
        this.scale = Vector2D.vector2Ddiv(scale, gameObject.parent.getComponent(Transform2D.class).getGlobalScale());
    }
}
