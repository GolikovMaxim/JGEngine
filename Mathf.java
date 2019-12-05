package JGEngine;

import java.util.Random;

public class Mathf {
    static final Random random = new Random();

    public static final float pi = 3.14159265359f;
    public static final float radian = 180 / pi;

    public static float sqr(float x) {
        return x*x;
    }

    public static float randomFloat() {
        return random.nextFloat();
    }

    public static int nextRandInt(int bound) {
        return random.nextInt(bound);
    }

    public static Vector2D randomVector2D(float boundX, float boundY) {
        return new Vector2D(random.nextFloat() * boundX, random.nextFloat() * boundY);
    }

    public static Vector2D randomVector2D(Vector2D bounds) {
        return new Vector2D(random.nextFloat() * bounds.x, random.nextFloat() * bounds.y);
    }
}
