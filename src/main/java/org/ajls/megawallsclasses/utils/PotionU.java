package org.ajls.megawallsclasses.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class PotionU {
    public static int getDuration(ItemStack itemStack, int index) {
        int duration = -1;
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta() instanceof PotionMeta) {
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                duration = meta.getCustomEffects().get(index).getDuration();
            }
        }
        return duration;
    }

    public static int getDuration(ItemStack itemStack) {
        return getDuration(itemStack, 0);
    }

    public static int getAmplifier(ItemStack itemStack, int index) {
        int amplifier = -1;
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta() instanceof PotionMeta) {
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                amplifier = meta.getCustomEffects().get(index).getAmplifier();
                return amplifier;
            }
        }
        return amplifier;
    }

    public static int getAmplifier(ItemStack itemStack) {
        return getAmplifier(itemStack, 0);
    }
}
