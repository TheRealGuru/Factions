package gg.revival.factions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if (command.getName().equalsIgnoreCase("faction")) {
            if (args.length > 0) {
                if (CommandManager.getFactionCommandByLabel(args[0]) != null) {
                    CommandManager.getFactionCommandByLabel(args[0]).onCommand(sender, args);
                }
            } else {
                //TODO: Send help block
            }
        }

        return false;
    }

}
