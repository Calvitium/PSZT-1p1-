package Evolutionary;


import static Evolutionary.Parameters.*;

import java.util.ArrayList;
import java.util.List;

public class Candidate implements Cloneable {

    private List<Pile> piles;
    private List<Pile> pilesSortedByX;
    private List<Pile[]> triangles;
    private List<Vector2f> BorderCover;


    Candidate(int n)  {
        piles = new ArrayList<>();
        for(int i = 0; i<n; i++)
            piles.add(new Pile());
        while(!isCircleCovered())
            resize();
    }

    private Candidate() {
        piles = new ArrayList<>();
    }

    @Override
    public Object clone() throws
                CloneNotSupportedException
    {
        Candidate candidate = new Candidate();
        for(Pile p : piles)
            candidate.getPiles().add(new Pile(p));
        return candidate;
    }

    List<Pile> getPiles() {
        return piles;
    }

    List<Pile> getSortedPiles() {
        return pilesSortedByX;
    }

    public boolean isCircleCovered() {
        checkIfNotContained();
        if(!checkIfCoversBorder())
            return false;
        createDelaunayTriangulation();

        return true;
    }

    private void checkIfNotContained() { // checks whether a bigger circle doesn't contain a smaller one
        for (Pile pile : getPiles()) {
            for (var i = getPiles().indexOf(pile) + 1; i < getPiles().size(); i++) {
                float distance = distanceBetweenPiles(getPiles().get(i), pile);
                if (distance + getPiles().get(i).getRadius() <= pile.getRadius()) {
                    getSortedPiles().remove(getPiles().get(i));
                    getPiles().remove(i);
                }
            }
        }
    }

    private boolean checkIfCoversBorder() {
        Pile tree = new Pile(0.0f, 0.0f, RD);
        Pile assistant = new Pile(0.0f, RD, 0.0f);
        for (Pile pile : getPiles()) {
            float[] intersections = findIntersections(tree, pile);
            if (intersections.length == 4) {
                float offset = (float) Math.acos(calculateCos(intersections[0], intersections[2], 0.0f, RD));
                float offset2 = (float) Math.acos(calculateCos(intersections[1], intersections[3], 0.0f, RD));
                if (intersections[0] < 0.0f)
                    offset += Math.PI;
                if (intersections[2] < 0.0f)
                    offset2 += Math.PI;
                float start, end;
                if (Math.abs((offset2 - offset)) > Math.PI) {
                    start = Math.max(offset, offset2);
                    end = Math.min(offset, offset2) + EPS;
                } else {
                    start = Math.min(offset, offset2);
                    end = Math.max(offset, offset2) + EPS;
                }
                if (start > end) {
                    float start2 = 0.0f;
                    float end2 = end;
                    end = (float) (2 * Math.PI);
                    addToBorderCover(start, end);
                    addToBorderCover(start2, end2);
                }
            }
        }
        if (BorderCover.size() == 1 && BorderCover.get(0).x < 0.0f + EPS && BorderCover.get(0).y > 2 * Math.PI - EPS)
            return true;
        return false;
    }

    private void addToBorderCover(float start, float end) {
        if (BorderCover.size() == 0)
            BorderCover.add(new Vector2f(start, end));
        else {
            for (Vector2f section : BorderCover) {
                if (section.x > start) {
                    if (section.x > end) {
                        BorderCover.add(BorderCover.indexOf(section), new Vector2f(start, end));
                        break;
                    } else if (section.y > end) {
                        section.x = start;
                        break;
                    } else {
                        BorderCover.remove(section);
                    }
                } else {
                    if (section.y > end)
                        break;
                    else if (section.y <= end) {
                        start = section.x;
                        BorderCover.remove(section);
                    }
                }

            }
        }


    }

    public float calculateCos(float ax, float ay, float bx, float by) {
        float scalarProduct, distAC, distBC;
        scalarProduct = ax * bx + ay * by;
        distAC = (float) (Math.sqrt(ax * ax + ay * ay));
        distBC = (float) (Math.sqrt(bx * bx + by * by));

        return scalarProduct / (distAC * distBC);
    }

    private float[] findIntersections(Pile pile1, Pile pile2) {
        float x = pile2.getX() - pile1.getX(); // translate to 0.0 0.0
        float y = pile2.getY() - pile1.getY();

        float a = -2 * x;
        float b = -2 * y;
        float c = x * x + y * y + pile1.getRadius() * pile1.getRadius() - pile2.getRadius() * pile2.getRadius();

        float[] intersections = calculateIntersections(a, b, c, pile1.getRadius());
        if (intersections.length == 4) // translate back
        {
            intersections[0] += pile1.getX();
            intersections[1] += pile1.getX();
            intersections[2] += pile1.getY();
            intersections[3] += pile1.getY();

        }
        return intersections;

    }

    private float[] calculateIntersections(float a, float b, float c, float R) {
        float x0 = -a * c / (a * a + b * b), y0 = -b * c / (a * a + b * b);
        if (c * c > R * R * (a * a + b * b) + EPS || Math.abs(c * c - R * R * (a * a + b * b)) < EPS) // none or one intersection
            return null;
        else {
            float d = R * R - c * c / (a * a + b * b);
            double mult = Math.sqrt(d / (a * a + b * b));
            float ax, ay, bx, by;
            ax = (float) (x0 + b * mult);
            bx = (float) (x0 - b * mult);
            ay = (float) (y0 - a * mult);
            by = (float) (y0 + a * mult);
            return new float[]{ax, bx, ay, by};
        }
    }

    //Not fully working yet
    private void createDelaunayTriangulation() { //create triangulation only for 3+ piles
        Pile pile1 = getSortedPiles().get(0);
        Pile pile2 = getSortedPiles().get(1);
        boolean visited[] = new boolean[]{false};
        visited[0] = true;
        visited[1] = true;

        for (int j = 0; j < getSortedPiles().size() - 2; j++) { // needs a fix
            float smallestCos = 1.0f;
            int smallestCosIndex = 0;
            for (int i = 2; i < getSortedPiles().size(); i++) {
                if (!visited[i]) {
                    float cos = calculateCos(pile1, pile2, getSortedPiles().get(i));
                    if (smallestCos > cos) {
                        smallestCos = cos;
                        smallestCosIndex = i;
                    }
                }
            }
            triangles.get(j)[0] = pile1;
            triangles.get(j)[1] = pile2;
            triangles.get(j)[2] = getSortedPiles().get(smallestCosIndex);

            visited[smallestCosIndex] = true;
            pile1 = pile2;
            pile2 = getSortedPiles().get(smallestCosIndex);


        }


    }

    private float calculateCos(Pile a, Pile b, Pile c) // calculates cos of the angle between ac and bc
    {
        float acX, acY, bcX, bcY;//create vectors ac and ab
        acX = c.getX() - a.getX();
        acY = c.getY() - a.getY();
        bcX = c.getX() - b.getX();
        bcY = c.getY() - b.getY();

        float scalarProduct, distAC, distBC;
        scalarProduct = acX * bcX + acY * bcY;
        distAC = distanceBetweenPiles(c, a);
        distBC = distanceBetweenPiles(c, b);

        return scalarProduct / (distAC * distBC);

    }


    private float distanceBetweenPiles(Pile a, Pile b) {
        return distanceBetweenToPoints(a.getX(),a.getY(),b.getX(),b.getY());
    }
    private float distanceBetweenToPoints(float ax,float ay,float bx,float by){
        return (float) (Math.sqrt(ax - bx) * (ax - bx) + (ay - by) * (ay - by));
    }

    private boolean checkTriangles(){
        for(Pile[] triangle:triangles) {
            float[] intersections01 = findIntersections(triangle[0],triangle[1]);
            float[] intersections02 = findIntersections(triangle[0],triangle[2]);
            float[] intersections12 = findIntersections(triangle[2],triangle[1]);
            List<Vector2f> validIntersections = null;
            if(intersections01.length > 0)
                validIntersections.add(findRightIntersection(intersections01,triangle[2]));
            if(intersections02.length >0)
                validIntersections.add(findRightIntersection(intersections02,triangle[1]));
            if(intersections12.length >0)
                validIntersections.add(findRightIntersection(intersections12,triangle[0]));

            if(validIntersections.size() <=1)
                return false;
            else if(validIntersections.size() == 2){//to finish
                return false;
            }




        }
        return false;
    }
    private Vector2f findRightIntersection(float[] intersection,Pile vertex){
        float dis1 = distanceBetweenToPoints(intersection[0],intersection[2],vertex.getX(),vertex.getY());
        float dis2 = distanceBetweenToPoints(intersection[1],intersection[3],vertex.getX(),vertex.getY());
        if(dis1 > dis2)
           return new Vector2f(intersection[1],intersection[3]);
        else
            return new Vector2f(intersection[0],intersection[2]);
    }



    private void resize() {
        for(Pile p : piles)
            p.setRadius(p.getRadius()*1.1f);
    }

    void mutate(float sigma) {
        for(Pile p : piles)
            p.mutate(sigma);
    }

}
