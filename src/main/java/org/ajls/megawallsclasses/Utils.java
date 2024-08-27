package org.ajls.megawallsclasses;

import org.bukkit.util.Vector;

import java.util.Random;

public class Utils {

    public static int random(int min , int max){
        Random random = new Random();;
        int result = random.nextInt(max - min + 1) + min;
        return result;
    }




}
