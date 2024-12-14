package org.ajls.megawallsclasses.commands;

import org.ajls.megawallsclasses.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("这个指令用了可能会出bug,若执意要用请输入/start confirm");
        }
        else if (args[0].equalsIgnoreCase("confirm")) {
            GameManager.Start();
        }
        return true;
    }
}
