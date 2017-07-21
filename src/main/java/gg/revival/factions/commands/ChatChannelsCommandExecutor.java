package gg.revival.factions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatChannelsCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if (command.getName().equalsIgnoreCase("chatchannel")) {
            if (args.length > 0) {
                if (CommandManager.getChatCommandByLabel(args[0]) != null) {
                    CommandManager.getChatCommandByLabel(args[0]).onCommand(sender, args);
                }
            } else {
                //TODO: Send help block
            }
        }

        return false;
    }

}
