package org.ajls.megawallsclasses.utils;

import org.bukkit.inventory.Inventory;

public class InventoryIterator {
    public void Iterate(Inventory inventory, InventoryIteratingProcessor inventoryIteratingProcess) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventoryIteratingProcess.process(inventory.getItem(i), i);
//            if (inventory.getItem(i) != null) {
//                try {
//                    InventoryIteratingProcessor operation = inventoryIteratingProcess.getDeclaredConstructor().newInstance();
//                    operation.operate(inventory.getItem(i));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    //InventoryIterator.iterate(inventory, (inv, slot, item) -> {
    //    if (item != null) {
    //        // Example: Replace all diamonds with emeralds
    //        if (item.getType() == Material.DIAMOND) {
    //            inv.setItem(slot, new ItemStack(Material.EMERALD, item.getAmount()));
    //        }
    //    }
    //});
}
