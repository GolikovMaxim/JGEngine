package JGEngine;

public class Vector2D {
    public static final Vector2D zero = new Vector2D(0, 0);
    public static final Vector2D one = new Vector2D(1, 1);
    public static final Vector2D up = new Vector2D(0, 1);
    public static final Vector2D down = new Vector2D(0, -1);
    public static final Vector2D left = new Vector2D(-1, 0);
    public static final Vector2D right = new Vector2D(1, 0);

    public float x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
    }

    public void sub(Vector2D vec) {
        x -= vec.x;
        y -= vec.y;
    }

    public void mul(float factor) {
        x *= factor;
        y *= factor;
    }

    public void div(float factor) {
        x /= factor;
        y /= factor;
    }

    public void vector2Dmul(Vector2D vec) {
        x *= vec.x;
        y *= vec.y;
    }

    public void vector2Ddiv(Vector2D vec) {
        x /= vec.x;
        y /= vec.y;
    }

    public void rotateAround(Vector2D point, float rotation) {
        sub(point);
        float x1 = x;
        x = x * (float)Math.cos(rotation) - y * (float)Math.sin(rotation);
        y = x1 * (float)Math.sin(rotation) + y * (float)Math.cos(rotation);
        add(point);
    }

    public Vector2D normalized() {
        if(x == 0 && y == 0) {
            return new Vector2D(this);
        }
        return Vector2D.div(this, length());
    }

    public Vector2D round() {
        return new Vector2D((int)x, (int)y);
    }

    public Vector2D clip(float x, float y) {
        return new Vector2D((this.x % x + x) % x, (this.y % y + y) % y);
    }

    public Vector2D clip(Vector2D v) {
        return clip(v.x, v.y);
    }

    public float length() {
        return (float)Math.sqrt(x*x + y*y);
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(getClass())) {
            return x == ((Vector2D)obj).x && y == ((Vector2D)obj).y;
        }
        return false;
    }

    public static Vector2D add(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public static Vector2D sub(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x - vec2.x, vec1.y - vec2.y);
    }

    public static Vector2D mul(Vector2D vec, float factor) {
        return new Vector2D(vec.x * factor, vec.y * factor);
    }

    public static Vector2D div(Vector2D vec, float factor) {
        return new Vector2D(vec.x / factor, vec.y / factor);
    }

    public static Vector2D vector2Dmul(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x * vec2.x, vec1.y * vec2.y);
    }

    public static Vector2D vector2Ddiv(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x / vec2.x, vec1.y / vec2.y);
    }

    public static float scalarMul(Vector2D vec1, Vector2D vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    public static float distance(Vector2D vec1, Vector2D vec2) {
        return Vector2D.sub(vec1, vec2).length();
    }

    public static Vector2D interpolate(Vector2D vec1, Vector2D vec2, float factor) {
        if(factor > 1) {
            return vec2;
        }
        if(factor < 0) {
            return vec1;
        }
        return Vector2D.add(Vector2D.mul(vec1, factor), Vector2D.mul(vec2, 1 - factor));
    }

    public static float angleBetween(Vector2D vec1, Vector2D vec2) {
        return (float)Math.acos(scalarMul(vec1, vec2) / vec1.length() / vec2.length()) *
                Math.signum(vec1.x * vec2.y - vec1.y * vec2.x);
    }
}
