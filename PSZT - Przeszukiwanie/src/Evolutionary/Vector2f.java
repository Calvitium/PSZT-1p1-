package Evolutionary;

import static Evolutionary.Parameters.R;
import static java.lang.Math.*;

public class Vector2f {
    public float x;
    public float y;

    Vector2f(float x, float y){
        this.x = x;
        this.y  =y;
    }
    Vector2f(Pile pile){
        this.x = pile.getX();
        this.y  =pile.getY();
    }

    public Vector2f() {
        x = y = 0;
    }

    public Vector2f(Vector2f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
    }

    public static Vector2f random() {
        Vector2f result = new Vector2f();
        float a = (float)(Math.random() * 2 *PI);
        float r = (float)(R * sqrt(Math.random()));
        result.x = (float) (r * cos(a));
        result.y = (float)(r * sin(a));
        return result;
    }
}