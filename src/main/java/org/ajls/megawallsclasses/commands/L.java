package org.ajls.megawallsclasses.commands;

import org.ajls.megawallsclasses.utils.CommandU;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class L implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandU.runCommand(sender, "plugman reload MegaWallsClasses"); //"reload confirm"
//        sender.
        return true;
    }
}
