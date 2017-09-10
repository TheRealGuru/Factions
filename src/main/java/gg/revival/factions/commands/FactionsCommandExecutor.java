package gg.revival.factions.commands;

import gg.revival.factions.tools.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if (command.getName().equalsIgnoreCase("faction")) {
            if (args.length > 0) {
                if (CommandManager.getFactionCommandByLabel(args[0]) != null) {
                    CommandManager.getFactionCommandByLabel(args[0]).onCommand(sender, args);
                }
            } else {
                if(sender instanceof Player) {
                    Messages.sendHelpPage((Player)sender, null);
                }
            }
        }

        return false;
    }

}
