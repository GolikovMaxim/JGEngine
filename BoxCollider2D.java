package JGEngine;

public class BoxCollider2D extends Component {
    Vector2D size;
    float bounciness = 0;
    public boolean isTrigger = false;

    public BoxCollider2D() {
        size = new Vector2D(1, 1);
    }

    public BoxCollider2D(Vector2D size) {
        this.size = size;
    }

    @Override
    public void onStart() {
        if(gameObject.components.get(Rigidbody2D.class) != null) {
            Physics.physicGameObjects.put(gameObject, true);
        }
        else {
            Physics.physicGameObjects.put(gameObject, false);
        }
    }

    @Override
    public void onDestroy() {
        Physics.physicGameObjects.remove(gameObject);
    }

    public Vector2D getSize() {
        return Vector2D.vector2Dmul(size, gameObject.getComponent(Transform2D.class).getGlobalScale());
    }

    public void setBounciness(float bounciness) {
        this.bounciness = bounciness;
    }
}
