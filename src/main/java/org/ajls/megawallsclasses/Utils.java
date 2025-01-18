package org.ajls.megawallsclasses;

import org.bukkit.util.Vector;

import java.util.Random;

public class Utils {
    static Random random = new Random();
    static long seed = 0;
    public static int random(int min , int max){
        if (seed == 0) {
            seed = System.currentTimeMillis();
        }
        random.setSeed(seed);
        int randomInt = random.nextInt();
        int result = randomInt % (max - min + 1) + min;//random.nextInt(max - min + 1) + min;
        seed = randomInt;
        return result;
    }




}
