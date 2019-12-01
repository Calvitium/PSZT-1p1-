package Evolutionary;

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

}
