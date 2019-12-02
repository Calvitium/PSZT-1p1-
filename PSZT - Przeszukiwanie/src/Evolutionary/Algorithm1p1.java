package Evolutionary;

import java.util.ArrayDeque;
import java.util.Queue;

import static Evolutionary.Parameters.*;
import static java.lang.Math.pow;

public class Algorithm1p1 extends Thread {
    private final int M_LAST_ITERATIONS = 10;
    private final static float C1 = 0.82f;
    private final static float C2 = 1.2f;
    private final static float FI_BORDER = 0.2f;
    private final static float SIGMA_0 = 0.1f;
    private float fi;
    private float sigma;
    private int generation;
    private int chosenY;
    private Candidate result;
    private Queue<Character> M_LastCandidates;

    public Algorithm1p1() {
        sigma = SIGMA_0;
        chosenY = generation = 0;
        fi = 0;
        M_LastCandidates = new ArrayDeque<>();
    }

    @Override
    public void run() {
        for (int i = 1; i <= N; i++) {
            try {
                Candidate temp = runProcedure(i);
                result = J(result) > J(temp) ? temp : result;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private Candidate runProcedure(int N) throws CloneNotSupportedException {
        Candidate x, y;
        /*1*/
        x = new Candidate(N);
        do {
            /*2*/
            y = generateDescendant(x);
            /*3*/
            x = chooseBetterCandidate(x, y);
            /*4*/
            fi = chosenY / M_LAST_ITERATIONS;
            if (generation % M_LAST_ITERATIONS == 0)
                /*5*/ updateSigma();
            /*6*/
        } while (sigma >= SIGMA_0);
        M_LastCandidates.clear();
        chosenY = 0;
        return x;
    }

    private void updateSigma() {
        if (fi < FI_BORDER)
            sigma *= C1;
        else if (fi > FI_BORDER)
            sigma *= C2;
    }

    private Candidate chooseBetterCandidate(Candidate x, Candidate y) {
        Candidate result;
        if (J(x) > J(y)) {
            result = y;
            M_LastCandidates.add('y');
            chosenY++;
        } else {
            result = x;
            M_LastCandidates.add('x');
        }
        if (M_LastCandidates.size() > M_LAST_ITERATIONS) {
            char removed = M_LastCandidates.remove();
            if (removed == 'y')
                chosenY--;
        }
        return result;
    }

    private Candidate generateDescendant(Candidate x) throws CloneNotSupportedException {
        Candidate descentant = (Candidate) x.clone();
        descentant.mutate(sigma);
        return descentant;
    }

    private static float J(Candidate candidate) {

        if (candidate == null)
            return Float.MAX_VALUE;
        float value = 0.0f;
        for (Pile p : candidate.getPiles())
            value += (W1 + W3) * (float) pow(p.getRadius(), 2) + W2 * p.getDistanceFromTree();
        return value;
    }

    public void showResult() {
        System.out.print("Result:\n");
        for (Pile p : result.getPiles())
            System.out.println("x = " + p.getX() + ", y = " + p.getY() + ", r = " + p.getRadius());
    }
}
