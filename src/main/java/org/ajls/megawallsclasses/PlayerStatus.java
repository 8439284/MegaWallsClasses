package org.ajls.megawallsclasses;

import org.bukkit.entity.LivingEntity;

public class PlayerStatus {
    //add health

    //add fire tick
    public static void addFireTick(LivingEntity entity, int time) {
        int firePlayer = entity.getFireTicks();
        firePlayer = firePlayer + time;
        if (0 < firePlayer) {
            entity.setFireTicks(firePlayer);
        }
        else {
            entity.setFireTicks(0);
        }
    }
}
