package game.core;

import javax.swing.*;

//******************************************
//Niloufar Zarabian 9912762256
//Kourosh Hassanzadeh 9912762552
//Mohammad Amin Afsharian Shandiz 9912762790
//******************************************

public class Utils {

    public static ImageIcon getImage(String fileName) {
        return new ImageIcon(Utils.class.getResource("../../assets/" + fileName));
    }

}
