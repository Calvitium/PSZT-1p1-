package Evolutionary;

import static Evolutionary.Parameters.*;
import static java.lang.Math.pow;

public final class Algorithm1p1 {
    public final static int M = 10;
    public final static float C1 = 0.82f;
    public final static float C2 = 1.2f;
    public final static float FI_BORDER = 0.2f;
    public static float FI;
    public static int GENERATION = 0;
    public static Algorithm1p1 Algorithm1p1;

    private Algorithm1p1() {
    }

    public static float J(Candidate candidate) {
        float value = Integer.MAX_VALUE;
        for (Pile p : candidate.getPiles())
            value += (W1 + W3) * (float) pow(p.getRadius(), 2) + W2 * p.getDistanceFromTree();
        return value;
    }

}
