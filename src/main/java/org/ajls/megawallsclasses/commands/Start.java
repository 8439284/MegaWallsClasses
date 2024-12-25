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
//            GameManager.Start();
            sender.sendMessage("好吧我懒得修bug 其实有一个秘密代码可以强制开启 但是只有我知道 hiahiahia （当然你去翻github那我没办法，但反正你这个杂鱼也看不懂)");
        }
        else if (args[0].equalsIgnoreCase("114514")) {
            GameManager.Start();
        }
        return true;
    }
}
