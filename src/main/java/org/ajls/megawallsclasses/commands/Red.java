package org.ajls.megawallsclasses.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getTeam;

public class Red implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            getTeam("red_team").addEntry(player.getName());
            //player.teleport(BedWars.getLoc_red_bed());
        }
        return true;
    }
}
