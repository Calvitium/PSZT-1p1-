package Evolutionary;


import static Evolutionary.Parameters.*;

import java.util.ArrayList;
import java.util.List;

public class Candidate implements Cloneable {
    private List<Pile> piles;

    Candidate(int n) {
        piles = new ArrayList<>();
        pilesR = new ArrayList<>();
        pilesSortedByX = new ArrayList<>();
        triangles = new ArrayList<>();
        BorderCover = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            piles.add(new Pile());
            pilesR.add(piles.get(i));
            pilesSortedByX.add(piles.get(i));
        }
        while (!probIsCircleCovered(1000))
            resize();
    }

    private Candidate() {
        piles = new ArrayList<>();
        pilesR = new ArrayList<>();
    }

    @Override
    public Object clone() {
        Candidate candidate = new Candidate();
        for (Pile p : piles) {
            candidate.getPiles().add(new Pile(p));
            candidate.getPilesR().add(new Pile(p));
        }
        return candidate;
    }

    boolean probIsCircleCovered(int n) {
        Vector2f point;
        for (int i = 0; i < n; i++) {
            point = new Vector2f(Vector2f.random());
            for (Pile p : piles) {
                if (p.isPointInRange(point))
                    break;
                else if (piles.get(piles.size() - 1) == p && !p.isPointInRange(point))
                    return false;
            }
        }
        return true;
    }

    void resize() {
        for (Pile p : piles) {
            float scale = 1.01f + (float) Math.cbrt((Math.abs(R - p.getRadius()) / R));
            p.setRadius(p.getRadius() * scale);
        }
    }

    void mutate(float sigma) {
        for (Pile p : piles)
            p.mutate(sigma);
    }

    void checkIfNotContained() { // checks whether a bigger circle doesn't contain a smaller one
        for (int j = 0; j < getPiles().size(); j++) {
            Pile pile = getPiles().get(j);
            for (int i = getPiles().indexOf(pile) + 1; i < getPiles().size(); i++)
                if (distanceBetweenPiles(getPiles().get(i), pile) + getPiles().get(i).getRadius() <= pile.getRadius())
                    getPiles().remove(i);
        }
    }

    List<Pile> getPiles(){
        return piles;
    }

    List<Pile> getPilesR() {
        return pilesR;
    }

    List<Pile> getSortedPiles() {
        return pilesSortedByX;
    }

    boolean isCircleCovered() {
        sortPiles();
        checkIfNotContained();
        if (!checkIfCoversBorder())
            return false;
        if (piles.size() > 2) {
            createDelaunayTriangulation();
            if (!checkTriangles())
                return false;
        }
        return true;
    }

    private void sortPiles() {
        piles.sort((o1, o2) -> (int) (o2.getRadius() - o1.getRadius()));
        pilesSortedByX.sort((o1, o2) -> (int) (o1.getX() - o2.getX()));
    }

    private boolean checkIfCoversBorder() {
        Pile tree = new Pile(0.0f, 0.0f, R);
        Pile assistant = new Pile(0.0f, R, 0.0f);
        for (Pile pile : getPilesR()) {
            float distance = distanceBetweenPiles(tree, pile);
            if (distance + R <= pile.getRadius())
                return true;

            float[] intersections = findIntersections(tree, pile);
            if (intersections != null && intersections.length == 4) {
                float offset = (float) Math.acos(calculateCos(intersections[0], intersections[2], 0.0f, R));
                float offset2 = (float) Math.acos(calculateCos(intersections[1], intersections[3], 0.0f, R));
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
        if (BorderCover != null && BorderCover.size() == 1 && BorderCover.get(0).x < 0.0f + EPS && BorderCover.get(0).y > 2 * Math.PI - EPS)
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

    private float calculateCos(float ax, float ay, float bx, float by) {
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
        if (intersections != null && intersections.length == 4) // translate back
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

    private void createDelaunayTriangulation() { //create triangulation only for 3+ piles
        Pile pile1 = getSortedPiles().get(0);
        Pile pile2 = getSortedPiles().get(1);
        boolean visited[] = new boolean[getSortedPiles().size()];
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

    private float calculateCos(Pile a, Pile b, Pile c) {
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
        return distanceBetweenToPoints(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private float distanceBetweenToPoints(float ax, float ay, float bx, float by) {
        return (float) (Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by)));
    }

    private boolean checkTriangles() {
        for (Pile[] triangle : triangles) {
            float[] intersections01 = findIntersections(triangle[0], triangle[1]);
            float[] intersections02 = findIntersections(triangle[0], triangle[2]);
            float[] intersections12 = findIntersections(triangle[2], triangle[1]);
            List<IntersectionPair> validIntersections = null;
            if (intersections01 != null)
                validIntersections.add(new IntersectionPair(findRightIntersection(intersections01, triangle[2]), triangle, 2));
            if (intersections02 != null)
                validIntersections.add(new IntersectionPair(findRightIntersection(intersections02, triangle[1]), triangle, 1));
            if (intersections12 != null)
                validIntersections.add(new IntersectionPair(findRightIntersection(intersections12, triangle[0]), triangle, 0));

            if (validIntersections == null || validIntersections.size() <= 1)
                return false;
            else if (validIntersections.size() == 2)//to finish
                if (!isTriangleCovered2(validIntersections))
                    return false;
                else { // validIntersections.size() == 3
                    if (!isTriangleCovered3(validIntersections))
                        return false;
                }

        }
        return true;
    }

    private boolean isTriangleCovered2(List<IntersectionPair> validIntersections) {
        int opp = validIntersections.get(0).opp;
        int i = (opp + 1) % 3;
        int j = (opp + 2) % 3;
        Straight straight = new Straight(validIntersections.get(0).piles[i], validIntersections.get(0).piles[j]);
        if (straight.isOverTheStraight(validIntersections.get(0).intersection)
                != straight.isOverTheStraight(new Vector2f(validIntersections.get(0).piles[opp])))
            return true;
        return false;

    }

    private boolean isTriangleCovered3(List<IntersectionPair> validIntersections) {
        for (IntersectionPair intersectionPair : validIntersections) {
            int opp = intersectionPair.opp;
            int i = (opp + 1) % 3;
            int j = (opp + 2) % 3;
            Straight straight = new Straight(intersectionPair.piles[i], intersectionPair.piles[j]);
            if (straight.isOverTheStraight(intersectionPair.intersection)
                    != straight.isOverTheStraight(new Vector2f(intersectionPair.piles[opp])))
                return false;
        }
        return true;
    }

    private Vector2f findRightIntersection(float[] intersection, Pile vertex) {
        float dis1 = distanceBetweenToPoints(intersection[0], intersection[2], vertex.getX(), vertex.getY());
        float dis2 = distanceBetweenToPoints(intersection[1], intersection[3], vertex.getX(), vertex.getY());
        if (dis1 > dis2)
            return new Vector2f(intersection[1], intersection[3]);
        else
            return new Vector2f(intersection[0], intersection[2]);
    }

    private List<Pile> pilesR;
    private List<Pile> pilesSortedByX;
    private List<Pile[]> triangles;
    private List<Vector2f> BorderCover;
}
