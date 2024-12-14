package org.ajls.megawallsclasses.commands;

import net.kyori.adventure.util.TriState;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.PassiveSkills;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Locale;
import java.util.UUID;

import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Bukkit.getServer().getPluginManager().callEvent(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, -10));
//            player.setFoodLevel(1);

//            BlockDisplay blockDisplay = world.spawn(location, BlockDisplay.class);
//            blockDisplay.setBlock(Bukkit.createBlockData(Material.END_ROD));
//            PassiveSkills.elaina_passive_skill_1(player);

//            Slime slime = world.spawn(location, Slime.class);
//            slime.setSize(5);
//            slime.setPassenger(player);
//            slime.setAggressive(false);
//            slime.setWander(false);
//            slime.setMaxHealth(0.1);
//            slime.setHealth(0.1);
//            slime.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 255, false, false));
//            slime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 255, false, false));
//            slime.setGravity(false);
//
//            BukkitScheduler scheduler = Bukkit.getScheduler();
//            BukkitTask task = new BukkitRunnable() {
//                public void run() {
//                    if (!player.isOnline() || slime.getPassengers().isEmpty() || slime.isDead()) {
//                        PassiveSkills.elaina_passive_skill_1_disable(player);
//                        slime.remove();
//                        cancel();
//                        return;
//                    }
//
//                    slime.setVelocity(player.getLocation().getDirection().multiply(0.5));
//                }
//            }.runTaskTimer(MegaWallsClasses.plugin, 0, 1);

//            scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//                slime.setVelocity(player.getLocation().getDirection().multiply(0.5));
////                slime.setAggressive(false);
//
//
//            }, 0, 1);
        }
        return true;
    }
}
