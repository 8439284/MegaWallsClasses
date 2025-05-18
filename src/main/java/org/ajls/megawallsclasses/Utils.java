package org.ajls.megawallsclasses;

import org.bukkit.util.Vector;

import java.util.Random;

public class Utils {
    static Random random = new Random();
    static long seed = 0;
    public static int random(int min , int max, boolean alsoNegative){
        if (seed == 0) {
            seed = System.currentTimeMillis();
            random.setSeed(seed);
        }
        /*
        long randomInt = random.nextLong();
        randomInt = Math.abs(randomInt);
//        long absRandomInt = Math.abs(randomInt);
        int inRangeRandomInt = Math.abs((int) randomInt % (max - min + 1));
        int result = inRangeRandomInt  + min;//random.nextInt(max - min + 1) + min;
        //-7 % 3 = -1 so need abs function
//        seed = randomInt;
//        if (!alsoNegative) {
//            if (result < 0) {
//                result = -result;
//            }
//        }
         */
        int result = random.nextInt(max - min + 1) + min;

        return result;
    }

    public static int random(int min , int max){
        return random(min, max, false);
    }




}
