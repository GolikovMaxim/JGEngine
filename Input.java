package JGEngine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Input {
    class Axis {
        float value = 0;
        ArrayList<KeyCode> m = new ArrayList<>(), p = new ArrayList<>();

        Axis(KeyCode m, KeyCode p) {
            this.m.add(m);
            this.p.add(p);
        }

        public void addKeycodes(KeyCode m, KeyCode p) {
            this.m.add(m);
            this.p.add(p);
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
        axises.get("Horizontal").addKeycodes(KeyCode.A, KeyCode.D);
        axises.get("Vertical").addKeycodes(KeyCode.S, KeyCode.W);
    }

    void addKey(KeyCode c, boolean b) {
        keys.put(c, b);
    }

    void updateKeys() {
        for(Map.Entry<String, Axis> axis : axises.entrySet()) {
            for(int i = 0; i < axis.getValue().m.size(); i++) {
                if(keys.get(axis.getValue().m.get(i)) != null || keys.get(axis.getValue().p.get(i)) != null) {
                    axis.getValue().value = (keys.get(axis.getValue().m.get(i)) != null && keys.get(axis.getValue().m.get(i)) ? -1 : 0) +
                            (keys.get(axis.getValue().p.get(i)) != null && keys.get(axis.getValue().p.get(i)) ? 1 : 0);
                    break;
                }
            }
        }
        previousKeys = keys;
        keys = new HashMap<>();
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
