package JGEngine;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class Input {
    class Axis {
        float value = 0;
        KeyCode m, p;

        Axis(KeyCode m, KeyCode p) {
            this.m = m;
            this.p = p;
        }
    }

    static Input input;

    HashMap<KeyCode, Boolean> keys = new HashMap<>();
    HashMap<KeyCode, Boolean> previousKeys;
    HashMap<String, Axis> axises = new HashMap<>();

    Input() {
        input = this;
        axises.put("Horizontal", new Axis(KeyCode.LEFT, KeyCode.RIGHT));
        axises.put("Vertical", new Axis(KeyCode.DOWN, KeyCode.UP));
    }

    void addKey(KeyCode c, boolean b) {
        keys.put(c, b);
    }

    void updateKeys() {
        for(Map.Entry<String, Axis> axis : axises.entrySet()) {
            axis.getValue().value = (keys.get(axis.getValue().m) != null && keys.get(axis.getValue().m) ? -1 : 0) +
                    (keys.get(axis.getValue().p) != null && keys.get(axis.getValue().p) ? 1 : 0);
        }
        previousKeys = keys;
    }

    public boolean getKey(KeyCode c) {
        return previousKeys.containsKey(c);
    }

    public float getAxis(String axis) {
        if(axises.containsKey(axis)) {
            return axises.get(axis).value;
        }
        return 0;
    }

    public static Input getInput() {
        return input;
    }
}
