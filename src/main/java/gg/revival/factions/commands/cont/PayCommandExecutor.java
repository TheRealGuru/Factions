package gg.revival.factions.commands.cont;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PayCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if(command.getName().equalsIgnoreCase("pay")) {
            if(args.length != 2) {
                sender.sendMessage(ChatColor.RED + "/pay <player> <amount>");
                return false;
            }


        }

        return false;
    }
}
