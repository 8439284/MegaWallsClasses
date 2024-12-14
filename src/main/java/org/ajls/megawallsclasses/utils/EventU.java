package org.ajls.megawallsclasses.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public class EventU {
    public static double getFinalHealth(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        double finalDamage = event.getFinalDamage();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            double livingEntityHealthBefore = livingEntity.getHealth();
            double livingEntityAbsorptionBefore = livingEntity.getAbsorptionAmount();
            double livingEntityAbsorptionAfter;
            double livingEntityHealthAfter;// = livingEntity.getHealth() - finalDamage;
            if (livingEntityAbsorptionBefore >= finalDamage) {
                livingEntityAbsorptionAfter = livingEntityAbsorptionBefore - finalDamage;
                return livingEntityHealthBefore;
            }
            else {
                livingEntityAbsorptionAfter = 0;
                livingEntityHealthAfter = livingEntityHealthBefore + livingEntityAbsorptionBefore - finalDamage;
                if (livingEntityHealthAfter <= 0) return 0;
                return livingEntityHealthAfter;
            }

//            return livingEntityHealthAfter;
        }
        else {
            return -1;
        }


    }

    public static double getFinalDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        double finalDamage = event.getFinalDamage();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            double livingEntityHealthBefore = livingEntity.getHealth();
            double livingEntityAbsorptionBefore = livingEntity.getAbsorptionAmount();
            double livingEntityHPBefore = livingEntityHealthBefore + livingEntityAbsorptionBefore;
            if (livingEntityHPBefore >= finalDamage) {
                return finalDamage;
            }
            else {
                return livingEntityHPBefore;
            }
//            finalDamage -= livingEntityAbsorptionBefore;
//            finalDamage -= livingEntityHealthBefore;
//            if (finalDamage <= 0) return 0;

//            return livingEntityHealthAfter;
        }
        else {
            return -1;
        }
    }
}
