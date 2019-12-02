package Evolutionary;

 class Straight {
    private float a;
    private float b;

    Straight(Pile pile1, Pile pile2){
        a = (pile1.getY() - pile2.getY())/(pile1.getX() - pile2.getX());
        b = pile1.getY() - a*pile1.getX();
    }
    boolean isOverTheStraight(Vector2f point){
        float temp = a*point.x +b;
        return !(temp > point.y);

    }
}
