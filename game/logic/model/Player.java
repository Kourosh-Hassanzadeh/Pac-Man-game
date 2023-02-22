package game.logic.model;

//******************************************
//Niloufar Zarabian 9912762256
//Kourosh Hassanzadeh 9912762552
//Mohammad Amin Afsharian Shandiz 9912762790
//******************************************

import java.util.ArrayList;

public class Player {

    private Bead bead;
    private int score;
    private final ArrayList<Limit> eatenLimits = new ArrayList<>();

    public void setBead(Bead bead) {
        this.bead = bead;
    }

    public Bead getBead() {
        return bead;
    }

    public void incrementScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void addLimit(Limit limit) {
        eatenLimits.add(limit);
    }

    public Limit getFirstLimit() {
        return eatenLimits.get(0);
    }

    public void removeFirstLimit() {
        eatenLimits.remove(0);
    }

    public boolean hasAnyEatenLimit() {
        return !eatenLimits.isEmpty();
    }
}

