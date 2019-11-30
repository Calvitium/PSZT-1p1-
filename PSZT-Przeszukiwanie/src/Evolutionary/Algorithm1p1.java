package Evolutionary;

import com.sun.istack.internal.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

import static Evolutionary.Parameters.*;
import static java.lang.Math.pow;

public class Algorithm1p1 {
    private final  int M_LAST_ITERATIONS = 10;
    private final static float C1 = 0.82f;
    private final static float C2 = 1.2f;
    private final static float FI_BORDER = 0.2f;
    private final static float SIGMA_0 = 0.1f;
    private float fi;
    private float sigma;
    private int generation;
    private int chosenX, chosenY;
    private Candidate x, y;
    private Queue<Character> M_LastCandidates;

    public Algorithm1p1() {
        sigma = SIGMA_0;
        chosenX = chosenY = generation = 0;
        fi = 0;
        M_LastCandidates = new ArrayDeque<Character>();
    }

    public Candidate runProcedure(int N) throws CloneNotSupportedException {
        collectInput(); //0
/*1*/   x = new Candidate(N);
        do{
/*2*/       y = generateDescendant(x);
/*3*/       x = chooseBetterCandidate();
/*4*/       fi = chosenY / M_LAST_ITERATIONS;
            if (generation % M_LAST_ITERATIONS == 0)
/*5*/           updateSigma();
/*6*/    }while(sigma >= SIGMA_0);
        return x;
    }

    private void updateSigma() {
        if(fi < FI_BORDER)
            sigma *= C1;
        else if(fi > FI_BORDER)
            sigma *= C2;
    }

    private Candidate chooseBetterCandidate() {
        Candidate result;
        if(J(x) > J(y)) {
            result = y;
            M_LastCandidates.add('y');
            chosenY++;
        }
        else {
            result = x;
            M_LastCandidates.add('x');
            chosenX++;
        }
        if(M_LastCandidates.size() > M_LAST_ITERATIONS) {
            char removed = M_LastCandidates.remove();
            if (removed == 'y')
                chosenY--;
            else
                chosenX--;
        }
        return result;
    }

    private Candidate generateDescendant(Candidate x) throws CloneNotSupportedException {
        Candidate descentant = (Candidate) x.clone();
        descentant.mutate(sigma);
        return descentant;
    }

    public static float J(Candidate candidate) {
        float value = Integer.MAX_VALUE;
        for (Pile p : candidate.getPiles())
            value += (W1 + W3) * (float) pow(p.getRadius(), 2) + W2 * p.getDistanceFromTree();
        return value;
    }

    private void collectInput() {
        float RD, W1, W2, W3;
        int N;
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the max. number of piles: ");
        N = reader.nextInt();
        System.out.print("Enter the radius: ");
        RD = reader.nextFloat();
        System.out.print("Enter W1: ");
        W1 = reader.nextFloat();
        System.out.print("Enter W2: ");
        W2 = reader.nextFloat();
        System.out.print("Enter W3: ");
        W3 = reader.nextFloat();
        new Parameters(RD, W1, W2, W3, N);
    }


}
