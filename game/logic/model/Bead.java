package game.logic.model;

//******************************************
//Niloufar Zarabian 9912762256
//Kourosh Hassanzadeh 9912762552
//Mohammad Amin Afsharian Shandiz 9912762790
//******************************************

public class Bead extends Element {

    private boolean isRed;

    public Bead(boolean isRed) {
        this.isRed = isRed;
    }

    public boolean isRed() {
        return isRed;
    }

    public boolean isBlue() {
        return !isRed;
    }
}
