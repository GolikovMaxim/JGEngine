package JGEngine;

public class Rigidbody2D extends Component {
    public Vector2D velocity;
    public boolean useGravity;
    public float mass;

    public Rigidbody2D() {
        velocity = new Vector2D();
        useGravity = false;
        mass = 1;
    }

    @Override
    public void onStart() {
        if(Physics.physicGameObjects.get(gameObject) != null) {
            Physics.physicGameObjects.put(gameObject, true);
        }
    }

    @Override
    public void onDestroy() {
        Physics.physicGameObjects.replace(gameObject, false);
    }
}
