package Evolutionary;

import static Evolutionary.Parameters.R;
import static java.lang.Math.*;

class Vector2f {
    float x;
    float y;

    Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vector2f(Pile pile) {
        this.x = pile.getX();
        this.y = pile.getY();
    }

    private Vector2f() {
        x = y = 0;
    }

    Vector2f(Vector2f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
    }

    static Vector2f random() {
        Vector2f result = new Vector2f();
        float a = (float) (Math.random() * 2 * PI);
        float r = (float) (R * sqrt(Math.random()));
        result.x = (float) (r * cos(a));
        result.y = (float) (r * sin(a));
        return result;
    }
}