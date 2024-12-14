package org.ajls.megawallsclasses.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandU {
    public static void runCommand(CommandSender commandSender, String command) {  //runConsoleCommand
        Bukkit.dispatchCommand(commandSender, command);  //Bukkit.getConsoleSender()
    }

    public static void runConsoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
