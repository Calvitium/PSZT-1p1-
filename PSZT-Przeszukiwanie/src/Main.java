import Evolutionary.Parameters;
import Evolutionary.Candidate;
import java.util.Scanner;

import static Evolutionary.Algorithm1p1.J;


public class Main {

    public static void main(String[] args) {
        Candidate x, y;
        collectInput(); //0
        //1. generateFirstEntity;
        //2. y = x + sigm*N(0,I)
          x = J(x) > J(y) ? y : x; //3
        //4. update FI
        //5. update SIGMA
        //6. stop || goto 2.
    }

    private static void collectInput() {
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
