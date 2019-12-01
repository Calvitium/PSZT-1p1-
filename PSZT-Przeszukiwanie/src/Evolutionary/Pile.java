package Evolutionary;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Pile {
    private float X;
    private float Y;
    private float radius;

    public Pile(float x, float y, float radius) {
        this.X = x;
        this.Y = y;
        this.radius = radius;
    }
    float getX() {return X;}
    float getY() {return Y;}
    float getRadius() {
        return radius;
    }

    float getDistanceFromTree() {
        return (float)sqrt(pow(X, 2) + pow(Y, 2));
    }
}
