package org.ajls.megawallsclasses.commands;

import org.ajls.megawallsclasses.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager.Start();
        return true;
    }
}
