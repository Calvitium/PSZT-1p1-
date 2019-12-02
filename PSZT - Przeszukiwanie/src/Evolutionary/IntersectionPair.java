package Evolutionary;

class IntersectionPair {
    Pile[] piles;
    Vector2f intersection;
    int opp;

    IntersectionPair(Vector2f intersection, Pile[] piles, int opp){
        this.intersection = intersection;
        this.opp = opp;
        this.piles = piles;
    }
}
