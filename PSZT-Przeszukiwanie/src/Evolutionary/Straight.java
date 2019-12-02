package Evolutionary;

public class Straight {
    private float a;
    private float b;

    Straight(Pile pile1, Pile pile2){
        a = (pile1.getY() - pile2.getY())/(pile1.getX() - pile2.getX());
        b = pile1.getY() - a*pile1.getX();
    }
    public boolean isOverTheStaright(Vector2f point){
        float temp = a*point.x +b;
        if(temp > point.y)
            return false;
        return true;

    }
}
