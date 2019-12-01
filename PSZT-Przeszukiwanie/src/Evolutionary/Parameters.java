package Evolutionary;

import java.util.Scanner;

public class Parameters {
    static float R;
    static float W1;
    static float W2;
    static float W3;
    static int N;

    public static void collectInput() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the max. number of piles: ");
        N = reader.nextInt();
        System.out.print("Enter the radius: ");
        R = reader.nextFloat();
        System.out.print("Enter W1: ");
        W1 = reader.nextFloat();
        System.out.print("Enter W2: ");
        W2 = reader.nextFloat();
        System.out.print("Enter W3: ");
        W3 = reader.nextFloat();
    }
}
