package JGEngine;

import java.util.ArrayList;

public class Rect2D {
    Vector2D[] points;
    Vector2D size;
    Vector2D position;
    float rotation;

    public Rect2D(Vector2D position, Vector2D size, float rotation) {
        Vector2D[] points = getPoints(position, size);
        for(Vector2D point : points) {
            point.rotateAround(position, rotation);
        }
        this.points = points;
        this.size = size;
        this.position = position;
        this.rotation = rotation;
    }

    public Rect2D(Transform2D transform) {
        this(transform.getGlobalPosition(), transform.getGlobalScale(), transform.getGlobalRotation());
    }

    public Rect2D(Camera2D camera) {
        this(camera.gameObject.getComponent(Transform2D.class).getGlobalPosition(), camera.orthographicSize,
                camera.gameObject.getComponent(Transform2D.class).getGlobalRotation());
    }

    public Rect2D(BoxCollider2D collider) {
        this(collider.gameObject.getComponent(Transform2D.class).getGlobalPosition(), collider.getSize(),
                collider.gameObject.getComponent(Transform2D.class).getGlobalRotation());
    }

    public static Vector2D[] getPoints(Vector2D position, Vector2D size) {
        Vector2D[] res = new Vector2D[4];
        res[0] = new Vector2D(size.x / 2, size.y / 2);
        res[1] = new Vector2D(-size.x / 2, size.y / 2);
        res[2] = new Vector2D(-size.x / 2, -size.y / 2);
        res[3] = new Vector2D(size.x / 2, -size.y / 2);
        for(Vector2D point : res) {
            point.add(position);
        }
        return res;
    }

    public Line2D[] getSides() {
        Line2D[] res = new Line2D[4];
        for(int i = 0; i < 4; i++) {
            res[i] = new Line2D(points[i], points[(i + 1) % 4]);
        }
        return res;
    }

    public boolean isPointInside(Vector2D point) {
        Rect2D notRotatedRect = new Rect2D(position, size, 0);
        point.rotateAround(position, -rotation);
        Line2D[] sides = notRotatedRect.getSides();
        for(Line2D line : sides) {
            if(line.wherePoint(point) == -1) return false;
        }
        return true;
    }

    public Line2D getIntersectedSide(Ray2D ray) {
        for(Line2D side : getSides()) {
            if(Ray2D.isIntersected(ray, side)) {
                return side;
            }
        }
        return null;
    }

    public static boolean isInside(Rect2D rect1, Rect2D rect2) {
        Rect2D big, small;
        if(rect1.size.x > rect2.size.x && rect1.size.y > rect2.size.y) {
            big = rect1;
            small = rect2;
        }
        else if(rect1.size.x < rect2.size.x && rect1.size.y < rect2.size.y) {
            big = rect2;
            small = rect1;
        }
        else {
            return false;
        }
        for(Vector2D point : small.points) {
            if(!big.isPointInside(point)) return false;
        }
        return true;
    }

    public static boolean isIntersected(Rect2D rect1, Rect2D rect2) {
        for(Vector2D point : rect2.points) {
            if(rect1.isPointInside(point)) return true;
        }
        for(Vector2D point : rect1.points) {
            if(rect2.isPointInside(point)) return true;
        }
        return false;
    }

    public static Vector2D[] getInsidePoints(Rect2D rect1, Rect2D rect2) {
        if(!isIntersected(rect1, rect2)) {
            return null;
        }
        ArrayList<Vector2D> result = new ArrayList<>();
        for(Vector2D point : rect1.points) {
            if(rect2.isPointInside(point)) {
                result.add(point);
            }
        }
        return (Vector2D[])result.toArray();
    }

    public static Vector2D getBiasToUnintersect(Rect2D rect1, Rect2D rect2, Ray2D moveRay) {
        if(!isIntersected(rect1, rect2)) {
            return null;
        }
        Vector2D[] insidePoints = getInsidePoints(rect1, rect2);
        Vector2D intersectionPoint = null, farthestPoint = null;
        Line2D intersectedSide = null;
        for(Line2D line : rect2.getSides()) {
            if(Ray2D.isIntersected(moveRay, line)) {
                intersectedSide = line;
                break;
            }
        }
        intersectionPoint = Ray2D.getIntersectionPoint(moveRay, intersectedSide);
        for(Vector2D point : insidePoints) {
            if(farthestPoint == null || Vector2D.distance(intersectionPoint, point) >
                    Vector2D.distance(intersectionPoint, farthestPoint)) {
                farthestPoint = point;
            }
        }
        return Vector2D.sub(intersectionPoint, farthestPoint);
    }
}
