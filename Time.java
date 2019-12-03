package JGEngine;

public class Time {
    static long lastFrame = System.nanoTime();
    static float deltaTime = 0;

    public static float getDeltaTime() {
        return deltaTime;
    }
}
