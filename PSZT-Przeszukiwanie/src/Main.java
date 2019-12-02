import Evolutionary.Algorithm1p1;

import static Evolutionary.Parameters.collectInput;
import static Evolutionary.Parameters.showParameters;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        collectInput("Input");
        showParameters();
       Algorithm1p1 alg = new Algorithm1p1();
        alg.run();
        alg.showResult();
       /* Vector<Algorithm1p1> attempts = new Vector<>(2);
        for(Algorithm1p1 attempt : attempts){
            attempt.run();
        }
        for(Algorithm1p1 attempt : attempts){
            attempt.join();
        }
        for(Algorithm1p1 attempt : attempts){
            attempt.showResult();
        }*/
    }


}
