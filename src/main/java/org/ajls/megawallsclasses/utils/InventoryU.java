package org.ajls.megawallsclasses.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class InventoryU {
    public static Inventory setInventoryTitle(Inventory inventory, String title) {
        Inventory newInventory = Bukkit.createInventory(inventory.getHolder(), inventory.getSize(), title);
        for (int i = 0; i < inventory.getSize(); i++) {
            newInventory.setItem(i, inventory.getItem(i));
        }
        return newInventory;
    }
}
