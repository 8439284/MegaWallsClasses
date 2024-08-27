package org.ajls.megawallsclasses.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;
import static org.ajls.megawallsclasses.PassiveSkills.null_passive_skill_disable;

public class ChangeClass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inventory = Bukkit.createInventory(player, 18, "Select Class");
            ItemStack zombie = new ItemStack(Material.ROTTEN_FLESH, 1);
            setDisplayName(zombie,"免费送的满级");
            ItemStack herobrine = new ItemStack(Material.DIAMOND_SWORD, 1);
            setDisplayName(herobrine, "雷神");
            ItemStack skeleton = new ItemStack(Material.BONE, 1);
            setDisplayName(skeleton, "抢钻工具人");
            ItemStack enderman = new ItemStack(Material.ENDER_PEARL, 1);
            setDisplayName(enderman, "小心你背后快");
            ItemStack n5ll = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            setDisplayName(n5ll, "跑男");
            inventory.setItem(0,zombie);
            inventory.setItem(1,herobrine);
            inventory.setItem(2,skeleton);
            inventory.setItem(3,enderman);
            inventory.setItem(4,n5ll);
//            player.openInventory(inventory);
            null_passive_skill_disable(player);
        }
        return true;
    }
}
