package JGEngine;

public class Line2D {
    Vector2D start, end;
    float xFactor, yBias;
    float yFactor, xBias;

    public Line2D(Vector2D start, Vector2D end) {
        this.start = start;
        this.end = end;
        xFactor = (end.y - start.y) / (end.x - start.x);
        yBias = start.y - start.x * xFactor;
        yFactor = (end.x - start.x) / (end.y - start.y);
        xBias = start.x - start.y * yFactor;
    }

    public float pointValueX(float x) {
        return xFactor * x + yBias;
    }

    public float pointValueY(float y) {
        return yFactor * y + xBias;
    }

    public boolean hasPoint(Vector2D point) {
        float ymax = Math.max(end.y, start.y), xmax = Math.max(end.x, start.x);
        float ymin = Math.min(end.y, start.y), xmin = Math.min(end.x, start.x);
        if(!Float.isInfinite(xFactor)) {
            return point.y == pointValueX(point.x) && point.x < xmax && point.x > xmin;
        }
        else {
            return point.x == pointValueY(point.y) && point.y < ymax && point.y > ymin;
        }
    }

    public float length() {
        return Vector2D.distance(start, end);
    }

    public Vector2D getDirectionVector() {
        return Vector2D.sub(end, start);
    }

    public float distanceFromPoint(Vector2D point) {
        Ray2D ray = new Ray2D(this);
        float res = ray.distanceFromPoint(point);
        if(!Ray2D.isIntersected(ray.getNormal(point), this)) {
            return Math.min(Vector2D.distance(start, point), Vector2D.distance(end, point));
        }
        return res;
    }

    public static boolean isIntersected(Line2D line1, Line2D line2) {
        if(line1.xFactor == line2.xFactor) {
            return line1.yBias == line2.yBias;
        }
        if(line1.yFactor == line2.yFactor) {
            return line1.xBias == line2.xBias;
        }
        Vector2D intersectionPoint = getIntersectionPoint(line1, line2);
        if(intersectionPoint == null) {
            return false;
        }
        return line1.hasPoint(intersectionPoint) && line2.hasPoint(intersectionPoint);
    }

    public static Vector2D getIntersectionPoint(Line2D line1, Line2D line2) {
        return Ray2D.getIntersectionPoint(new Ray2D(line1), new Ray2D(line2));
    }

    public int wherePoint(Vector2D point) {
        if(hasPoint(point)) {
            return 0;
        }
        return ((end.x - start.x) * (point.y - start.y) - (point.x - start.x) * (end.y - start.y) > 0) ? 1 : -1;
    }
}
