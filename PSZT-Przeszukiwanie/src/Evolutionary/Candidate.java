package Evolutionary;


import java.util.ArrayList;
import java.util.List;

public class Candidate implements Cloneable {
    private List<Pile> piles;

    Candidate(int n)  {
        piles = new ArrayList<>();
        for(int i = 0; i<n; i++)
            piles.add(new Pile());
        while(!isCircleCovered())
            resize();
    }

    private Candidate() {
        piles = new ArrayList<>();
    }

    @Override
    public Object clone() throws
                CloneNotSupportedException
    {
        Candidate candidate = new Candidate();
        for(Pile p : piles)
            candidate.getPiles().add(new Pile(p));
        return candidate;
    }

    List<Pile> getPiles() {
        return piles;
    }

    private void resize() {
        for(Pile p : piles)
            p.setRadius(p.getRadius()*1.1f);
    }

    private boolean isCircleCovered() {
        return true;
    }


    void mutate(float sigma) {
        for(Pile p : piles)
            p.mutate(sigma);
    }
}
