package Evolutionary;
import static Evolutionary.Parameters.R;
import static java.lang.Math.*;

public class Pile {
    private float X;
    private float Y;
    private float radius;

    public Pile() {
        float a = (float)(Math.random() * 2 *PI);
        float r = (float)(R * sqrt(Math.random()));
        X = (float) (r * cos(a));
        Y = (float)(r * sin(a));
        radius = (float)(Math.random() * R);
    }
    public Pile(float x, float y, float radius) {
        this.X = x;
        this.Y = y;
        this.radius = radius;
    }

    public Pile(Pile p) {
        this.X = p.X;
        this.Y = p.Y;
        this.radius = p.radius;
    }

    float getRadius() {
        return radius;
    }

    float getDistanceFromTree() {
        return (float)sqrt(pow(X, 2) + pow(Y, 2));
    }

    public void setRadius(float newRadius) {
        this.radius = newRadius;
    }

    public void mutate(float sigma) {

    }
}
