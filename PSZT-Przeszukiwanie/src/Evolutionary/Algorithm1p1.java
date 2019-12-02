package Evolutionary;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Queue;

import static Evolutionary.Parameters.*;
import static java.lang.Math.pow;

public class Algorithm1p1 extends Thread {
    private final static float FI_BORDER = 0.2f;
    private final static float SIGMA_0 = 0.1f;
    private final static float C1 = 0.82f;
    private final static float C2 = 1.2f;
    private final int M_LAST_ITERATIONS = 20;
    private float sigma;
    private float fi;
    private int generation;
    private int chosenY;
    private Candidate result;
    private ArrayDeque<Character> M_LastCandidates;

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
        if(W1+W3>0 && W2>0)
         result.checkIfNotContained();
    }

    private Candidate runProcedure(int N) throws CloneNotSupportedException {
        resetCounters();
        Candidate x, y;
/*1*/   x = new Candidate(N);
        do {
/*2*/      y = generateDescendant(x);
/*3*/      x = chooseBetterCandidate(x, y);
/*4*/      fi = (float)chosenY / M_LAST_ITERATIONS;
           if(++generation % M_LAST_ITERATIONS == 0)
/*5*/           updateSigma();
/*6*/   } while (generation<200);
        return x;
    }

    private void resetCounters() {
        M_LastCandidates.clear();
        chosenY = 0;
        generation = 0;
        sigma = SIGMA_0;
    }

    private Candidate generateDescendant(Candidate x) {
        Candidate descendant = (Candidate) x.clone();
        descendant.mutate(sigma);
        while(!descendant.probIsCircleCovered(1000))
            descendant.mutate(sigma);
        return descendant;
    }

    private Candidate chooseBetterCandidate(Candidate x, Candidate y) {
        if (J(x) > J(y)) {
            M_LastCandidates.add('y');
            chosenY++;
        } else
            M_LastCandidates.add('x');

        if (M_LastCandidates.size() > M_LAST_ITERATIONS) {
            char removed = M_LastCandidates.remove();
            if (removed == 'y')
                chosenY--;
        }
        return M_LastCandidates.getLast() == 'y' ? y : x;
    }

    private void updateSigma() {
        if (fi < FI_BORDER)
            sigma *= C1;
        else if (fi > FI_BORDER)
            sigma *= C2;
    }

    private static float J(Candidate candidate) {
        float value = 0.0f;
        if (candidate == null)
            return Float.MAX_VALUE;
        for (Pile p : candidate.getPiles())
            value += (W1 + W3) * (float) pow(p.getRadius(), 2) + W2 * p.getDistanceFromTree();
        return value;
    }

    public void showResult() {
        DecimalFormat format = new DecimalFormat();

        int i = 1;
        System.out.println("================ RESULT: " + result.getPiles().size() + " pile(s) =================");

        for (Pile p : result.getPiles())
            System.out.println(String.format("%2s",  new Integer(i++).toString())
                    + ". x = " + String.format("%10s", new Float(p.getX()).toString())
                    + ", y = " + String.format("%10s", new Float(p.getY()).toString())
                    + ", r = " + String.format("%10s", new Float(p.getRadius()).toString()));
        System.out.println("====================================================");
        System.out.print("\nFinal value of the cost function: J(x) = " + J(result));
    }
}
