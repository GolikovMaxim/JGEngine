package JGEngine;

import java.util.HashMap;
import java.util.Map;

public class Physics {
    static HashMap<GameObject, Boolean> physicGameObjects = new HashMap<>();

    static void process() {
        for(Map.Entry<GameObject, Boolean> physicObj1 : physicGameObjects.entrySet()) {
            if(physicObj1.getValue()) {
                physicObj1.getKey().getComponent(Transform2D.class).translate(
                        Vector2D.mul(physicObj1.getKey().getComponent(Rigidbody2D.class).velocity, Time.getDeltaTime()));
            }
            for(Map.Entry<GameObject, Boolean> physicObj2 : physicGameObjects.entrySet()) {
                Rect2D rect1 = new Rect2D(physicObj1.getKey().getComponent(BoxCollider2D.class));
                Rect2D rect2 = new Rect2D(physicObj2.getKey().getComponent(BoxCollider2D.class));
                if(Rect2D.isIntersected(rect1, rect2)) {
                    if (!physicObj1.getValue() && !physicObj2.getValue()) {
                        continue;
                    }
                    if (physicObj1.getValue() && physicObj2.getValue()) {
                        moveDynamicObjects(physicObj1.getKey(), physicObj2.getKey());
                    }
                    else {
                        if(physicObj1.getValue()) {
                            moveOnlyDynamicObject(physicObj1.getKey(), physicObj2.getKey());
                        }
                        else {
                            moveOnlyDynamicObject(physicObj2.getKey(), physicObj1.getKey());
                        }
                    }
                    for(Map.Entry<Class, Component> entry : physicObj1.getKey().components.entrySet()) {
                        entry.getValue().onCollision();
                    }
                    for(Map.Entry<Class, Component> entry : physicObj2.getKey().components.entrySet()) {
                        entry.getValue().onCollision();
                    }
                }
            }
        }
    }

    static void moveDynamicObjects(GameObject obj1, GameObject obj2) {
        Rect2D rect1 = new Rect2D(obj1.getComponent(BoxCollider2D.class));
        Rect2D rect2 = new Rect2D(obj2.getComponent(BoxCollider2D.class));
        Vector2D moveObj1, moveObj2;
        float mass1 = obj1.getComponent(Rigidbody2D.class).mass;
        float mass2 = obj1.getComponent(Rigidbody2D.class).mass;
        moveObj1 = Rect2D.getBiasToUnintersect(rect1, rect2,
            new Ray2D(obj1.getComponent(Rigidbody2D.class).velocity, obj1.getComponent(Transform2D.class).position));
        if(moveObj1 == null) {
            moveObj2 = Rect2D.getBiasToUnintersect(rect2, rect1,
                new Ray2D(obj2.getComponent(Rigidbody2D.class).velocity, obj2.getComponent(Transform2D.class).position));
            moveObj1 = Vector2D.mul(moveObj2, -1);
        }
        else {
            moveObj2 = Vector2D.mul(moveObj1, -1);
        }
        obj1.getComponent(Transform2D.class).translate(Vector2D.interpolate(moveObj1, moveObj2, mass1 / mass2));
        obj2.getComponent(Transform2D.class).translate(Vector2D.interpolate(moveObj2, moveObj1, mass1 / mass2));
        Vector2D intersectedSide = rect2.getIntersectedSide(new Ray2D(moveObj1,
                obj1.getComponent(Transform2D.class).position)).getDirectionVector();
        if(intersectedSide == null) {
            intersectedSide = rect1.getIntersectedSide(new Ray2D(moveObj2,
                    obj2.getComponent(Transform2D.class).position)).getDirectionVector();
        }
        moveObj1.rotateAround(obj1.getComponent(Transform2D.class).position,
                Vector2D.angleBetween(moveObj1, intersectedSide) * 2);
        moveObj2.rotateAround(obj2.getComponent(Transform2D.class).position,
                Vector2D.angleBetween(moveObj2, intersectedSide) * 2);
        obj1.getComponent(Transform2D.class).translate(
                Vector2D.mul(Vector2D.interpolate(moveObj1, moveObj2, mass1 / mass2),
                        obj1.getComponent(BoxCollider2D.class).bounciness));
        obj2.getComponent(Transform2D.class).translate(
                Vector2D.mul(Vector2D.interpolate(moveObj2, moveObj1, mass1 / mass2),
                        obj2.getComponent(BoxCollider2D.class).bounciness));
    }

    static void moveOnlyDynamicObject(GameObject obj1, GameObject obj2) {
        Rect2D rect1 = new Rect2D(obj1.getComponent(BoxCollider2D.class));
        Rect2D rect2 = new Rect2D(obj2.getComponent(BoxCollider2D.class));
        Vector2D moveObj;
        moveObj = Rect2D.getBiasToUnintersect(rect1, rect2,
                new Ray2D(obj1.getComponent(Rigidbody2D.class).velocity, obj1.getComponent(Transform2D.class).position));
        if(moveObj == null) {
            moveObj = Rect2D.getBiasToUnintersect(rect2, rect1,
                    new Ray2D(obj1.getComponent(Rigidbody2D.class).velocity, obj1.getComponent(Transform2D.class).position));
        }
        obj1.getComponent(Transform2D.class).translate(moveObj);
    }
}
