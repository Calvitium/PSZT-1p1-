
import static Evolutionary.Parameters.*;
import Evolutionary.Candidate;

import java.io.Console;
import java.util.Scanner;

import static Evolutionary.Algorithm1p1.J;

import Evolutionary.Algorithm1p1;
import java.util.Vector;
import static Evolutionary.Parameters.collectInput;



public class Main {

    public static void main(String[] args) throws InterruptedException {
        collectInput();
        Vector<Algorithm1p1> attempts = new Vector<>(2);
        for(Algorithm1p1 attempt : attempts){
            attempt.run();
        }
        for(Algorithm1p1 attempt : attempts){
            attempt.join();
        }
        for(Algorithm1p1 attempt : attempts){
            attempt.showResult();
        }
    }


}
