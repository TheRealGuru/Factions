package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FHelpCommand extends FCommand {

    public FHelpCommand() {
        super(
                "help",
                null,
                "/f help [category]",
                "View information about Factions commands",
                null,
                CmdCategory.INFO,
                1,
                2,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if(!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player)sender;

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if(args.length == 1) {
            Messages.sendHelpPage(player, null);
            return;
        }

        for(CmdCategory categories : CmdCategory.values()) {
            if(categories.toString().equalsIgnoreCase(args[1])) {
                Messages.sendHelpPage(player, categories);
                return;
            }
        }

        Messages.sendHelpPage(player, null);
    }

}
