package org.ajls.megawallsclasses.commands;

import net.kyori.adventure.util.TriState;
import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.PassiveSkills;
import org.ajls.megawallsclasses.custommusic.ClaudeMusic;
import org.ajls.megawallsclasses.custommusic.MidiMusicPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Color color = Color.fromRGB(255, 255, 255);
            double pixelSize = 0.5F;
            Location location = player.getLocation();
            Location locationRounded = new Location(location.getWorld(), location.getBlockX() + pixelSize/16, location.getBlockY() + 0.001, location.getBlockZ(), 180, -90);
            spawnPixel(
                    color, // Bukkit Color
                    locationRounded,
                    pixelSize
            );
            location.add(0,0.1,0);
            location.setPitch(-90);
            location.setYaw(0);
            Block block = location.getBlock();
            Location location1 = block.getLocation();
            //pixel size : 1 = 1/8 block
//            for (int i = 0; i < 16; i ++) {
//                for (int j = 0; j < 16; j ++) {
//                    Location location2 = location1.clone().add((double) i /16 + pixelSize/16, 0.1, (double) j /16 +pixelSize/16 + (double) 1 /16);
//                    location2.setPitch(-90);
//                    spawnPixel(
//                            color, // Bukkit Color
//                            location2,
//                            pixelSize
//                    );
//                }
//            }
//            spawnPixel(
//                    color, // Bukkit Color
//                    location,
//                    pixelSize
//            );
            return true;

            /*
//            ClaudeMusic claudeMusic = new ClaudeMusic();
//            claudeMusic.playMidi(player, "sss");
            List<Player> players = new ArrayList<>();
            players.add(player);
            MidiMusicPlayer midiPlayer = new MidiMusicPlayer(MegaWallsClasses.getPlugin());
            try {
                midiPlayer.playMidiToPlayers(new File(MegaWallsClasses.getPlugin().getDataFolder(), "midis/sss.mid"), players);
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;

             */
            /*
            if (args[0].equalsIgnoreCase("1")) {
                player.sendMessage("test");
                ItemStack diamond1 = new ItemStack(Material.DIAMOND);
                ItemStackU.setStringPersistentData(diamond1, NameSpacedKeys.ITEM_TYPE, "1");
                ItemStack diamond2 = new ItemStack(Material.DIAMOND);
                ItemStackU.setStringPersistentData(diamond2, NameSpacedKeys.ITEM_TYPE, "2");
                player.getInventory().setItem(10, diamond1);
                player.getInventory().setItem(11, diamond2);
            }
            else if(args[0].equalsIgnoreCase("2")) {
                for (int i = 0; i < player.getInventory().getSize(); i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (itemStack != null) {
                        player.sendMessage(ItemStackU.getDisplayName(itemStack) + ": " + ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE));
                    }
                }
            }

             */


//            Bukkit.getServer().getPluginManager().callEvent(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, -10));



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

    private TextDisplay spawnPixel(Color color, Location location, double pixelSize) {
        TextDisplay textDisplay = location.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setBackgroundColor(color);
        textDisplay.setText(" ");
        textDisplay.setBrightness(new Display.Brightness(15, 15));

        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(pixelSize, pixelSize / 2, pixelSize);
        textDisplay.setTransformation(transformation);
        return textDisplay;
    }
}
