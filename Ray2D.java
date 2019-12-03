package JGEngine;

import java.util.Map;

public class Ray2D {
    Vector2D direction, point;
    float xFactor, yFactor, bias;

    public Ray2D(Vector2D direction, Vector2D point) {
        this.direction = direction;
        this.point = point;
        if(direction.y != 0) {
            xFactor = 1;
            yFactor = -direction.x / direction.y;
            bias = direction.x / direction.y * point.y - point.x;
        }
        else {
            xFactor = -direction.y / direction.x;
            yFactor = 1;
            bias = direction.y / direction.x * point.x - point.y;
        }
    }

    public Ray2D(Line2D line) {
        this(line.getDirectionVector(), line.start);
    }

    public Vector2D getNormal() {
        return new Vector2D(xFactor, yFactor);
    }

    public Ray2D getNormal(Vector2D point) {
        return new Ray2D(getNormal(), point);
    }

    public float distanceFromPoint(Vector2D point) {
        return Math.abs(xFactor * point.x + yFactor * point.y + bias) / (float)Math.sqrt(xFactor * xFactor + yFactor * yFactor);
    }

    public static boolean isIntersected(Ray2D ray, Line2D line) {
        if(!isIntersected(ray, new Ray2D(line))) {
            return false;
        }
        Vector2D intersectionPoint = getIntersectionPoint(ray, line);
        return line.hasPoint(intersectionPoint);
    }

    public static boolean isIntersected(Ray2D ray1, Ray2D ray2) {
        return !(ray1.xFactor == ray2.xFactor && ray1.yFactor == ray2.yFactor && ray1.bias != ray2.bias);
    }

    public static Vector2D getIntersectionPoint(Ray2D ray, Line2D line) {
        return getIntersectionPoint(ray, new Ray2D(line));
    }

    public static Vector2D getIntersectionPoint(Ray2D ray1, Ray2D ray2) {
        float a1 = ray1.xFactor, b1 = ray1.yFactor, c1 = ray1.bias;
        float a2 = ray2.xFactor, b2 = ray2.yFactor, c2 = ray2.bias;
        float x, y;
        if(!isIntersected(ray1, ray2)) {
            return null;
        }
        if(b2 != 0) {
            x = (c2 * b1 / b2 - c1) / (a1 - a2 * b1 / b2);
        }
        else {
            x = (c1 * b2 / b1 - c2) / (a2 - a1 * b2 / b1);
        }
        if(a2 != 0) {
            y = (c2 * a1 / a2 - c1) / (b1 - b2 * a1 / a2);
        }
        else {
            y = (c1 * a2 / a1 - c2) / (b2 - b1 * a2 / a1);
        }
        return new Vector2D(x, y);
    }
}
