package Evolutionary;

import java.io.*;
import java.util.Scanner;

public class Parameters {

     static float R;
     static float W1;
     static float W2;
    static float W3;
    static int N;
    static final float EPS = 0.000001f;


    public static void collectInput(String filePath){
        BufferedReader reader = null;
        try {
            // use buffered reader to read line by line
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(

            System.getProperty("user.dir")+"\\src\\Data\\" + filePath))));
            String line;
            String[] numbers;

            line = reader.readLine();
            numbers = line.split(" ", 5);
            R  = Float.valueOf(numbers[0].trim());
            W1 = Float.valueOf(numbers[1].trim());
            W2 = Float.valueOf(numbers[2].trim());
            W3 = Float.valueOf(numbers[3].trim());
            N  = Integer.valueOf(numbers[4].trim());
        } catch (IOException e) {
            System.err.println("Exception:" + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Exception:" + e.toString());
                }
            }
        }
    }

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

    public static void showParameters() {
        System.out.println("R = " + R + ", W1 = " + W1 + ", W2 = " + W2 + ", W3 = " + W3 + ", N = " + N);
    }
}
