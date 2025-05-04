package org.ajls.megawallsclasses.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class Energetic implements CommandExecutor {
    public static HashSet<UUID> energeticPlayers = new HashSet<>();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!energeticPlayers.contains(player.getUniqueId())) {
                energeticPlayers.add(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "Energetic enabled");
            }
            else {
                energeticPlayers.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Energetic disabled");
            }
//            if (player.getInventory().contains(Material.GOLDEN_APPLE)) {
//                player.sendMessage("You have the Energetic class!");
//                // Add your logic for the Energetic class here
//            } else {
//                player.sendMessage("You do not have the Energetic class.");
//            }
        } else {
            commandSender.sendMessage("This command can only be used by players.");
        }
        return true;
    }
}
