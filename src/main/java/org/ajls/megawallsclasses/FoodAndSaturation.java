package org.ajls.megawallsclasses;

import org.bukkit.entity.Player;

public class FoodAndSaturation {
    public static void addExhaustion(Player player, float amount) {
        float exhaustion = player.getExhaustion();
        float current_exhaustion = exhaustion + amount;
        //exhaustion reaches 4 decrase saturation
        while (current_exhaustion < 0) {
            // increase foodlevel
            current_exhaustion = current_exhaustion + 4;
            addFoodLevel(player, 1);
        }
        while (current_exhaustion > 4) {
            // decrease saturation
            current_exhaustion = current_exhaustion - 4;
            addSaturation(player, -1);
        }
        player.setExhaustion(current_exhaustion);
    }

    public static void addFoodLevel(Player player, int amount) {
        // increase saturation if more , saturation cap at foodlevel if decrease
        int current_food_level = player.getFoodLevel() + amount;
        float saturation = player.getSaturation();
        if (current_food_level < saturation) {
            // saturation must cap at food level
            if (current_food_level < 0) {
                // starve
                current_food_level = 0;
                saturation = 0;
            }
            else {
                // cap at food level
                saturation = current_food_level;
            }
        }
        if (current_food_level > 20) {
            // increase saturation
            saturation = saturation + current_food_level - 20;
            // saturation added the more part
            if (saturation > 20) {
                saturation = 20;
                // cap at 20
            }
            current_food_level = 20;
            // cap at 20
        }
        player.setFoodLevel(current_food_level);
        player.setSaturation(saturation);
    }

    public static void addSaturation(Player player, float amount) {
        // increase foodlevel and saturation if more, decrease foodlevel if decrease
        float current_saturation = player.getSaturation() + amount;
        int food_level = player.getFoodLevel();
        while (current_saturation < 0 && food_level > 0) {
            //saturation decrease , so food level decrease until touches 0 or saturation bigger than 0
            // note, if the current saturation is -0.5 and food level is 1 , because foodlevel can only be int and saturation must cap at food level so saturation set to 0.5 then set to 0 and food level set to 0
            food_level = food_level - 1;
            current_saturation = current_saturation + 1;
        }
        if (food_level == 0) {
            // food level gone starve or saturation between [0,1)
            current_saturation = 0;
        }
        while (current_saturation > food_level && food_level < 20) {
            // increase foodlevel and saturation at the same time
            // foodlevel added more
            current_saturation = current_saturation - 1;
            food_level = food_level + 1;
        }
        if (current_saturation > 20) {
            // too full
            current_saturation = 20 ;
        }
        player.setFoodLevel(food_level);
        player.setSaturation(current_saturation);
    }
}
