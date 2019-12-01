package Evolutionary;

import java.util.Scanner;

public class Parameters {

    public static float R;
    public static float W1;
    public static float W2;
    public static float W3;
    public static int N;
    public static final float EPS = 0.000001f;


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
