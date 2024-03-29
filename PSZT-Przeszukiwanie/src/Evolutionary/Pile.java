package Evolutionary;

import java.util.Random;

import static Evolutionary.Parameters.R;
import static java.lang.Math.*;

class Pile {
    private float X;
    private float Y;
    private float radius;

    Pile() {
        float a = (float) (Math.random() * 2 * PI);
        float r = (float) (R * sqrt(Math.random()));
        X = (float) (r * cos(a));
        Y = (float) (r * sin(a));
        radius = (float) (Math.random()*R);
    }

    Pile(float x, float y, float radius) {
        this.X = x;
        this.Y = y;
        this.radius = radius;
    }

    Pile(Pile p) {
        this.X = p.X;
        this.Y = p.Y;
        this.radius = p.radius;
    }

    void mutate(float sigma) {
        float tempX, tempY;
        Random r = new Random();
        do {
            tempX = X + (float) r.nextGaussian() * sigma * R;
            tempY = Y + (float) r.nextGaussian() * sigma * R;
        } while ((float) (sqrt(pow(tempX, 2) + pow(tempY, 2))) > R);
        X = tempX;
        Y = tempY;
        radius = (float) (radius + r.nextGaussian() * sigma * R);
        if (radius < 0)
            radius = -radius;
    }

    float getX() {
        return X;
    }

    float getY() {
        return Y;
    }

    float getRadius() {
        return radius;
    }

    float getDistanceFromTree() {
        return (float) sqrt(pow(X, 2) + pow(Y, 2));
    }

    void setRadius(float newRadius) {
        this.radius = newRadius;
    }

    boolean isPointInRange(Vector2f point) {
        return sqrt(pow(X - point.x, 2) + pow(Y - point.y, 2)) <= radius;
    }
}
