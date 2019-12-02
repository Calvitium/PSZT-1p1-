import Evolutionary.Algorithm1p1;

import java.io.IOException;

import static Evolutionary.Parameters.collectInput;
import static Evolutionary.Parameters.showParameters;


public class Main {

    public static void main(String[] args) throws IOException {
        collectInput("Input");
        showParameters();
        Algorithm1p1 alg = new Algorithm1p1();
        alg.run();
        alg.showResult();
    }


}
