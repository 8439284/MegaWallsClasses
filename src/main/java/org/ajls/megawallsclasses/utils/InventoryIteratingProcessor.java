package org.ajls.megawallsclasses.utils;

import org.bukkit.inventory.ItemStack;

public interface InventoryIteratingProcessor {
    public void process(ItemStack itemStack, int index);
}
