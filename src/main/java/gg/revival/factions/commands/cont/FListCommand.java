package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FListCommand extends FCommand {

    public FListCommand() {
        super(
                "list",
                null,
                "/f list [page#]",
                "View a list of existing factions",
                null,
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

        int page = 0;

        if(args.length == 2) {
            if(!NumberUtils.isNumber(args[1])) {
                //TODO: Send not a number
                return;
            }

            page = Math.abs(Integer.valueOf(args[1]));

            if(page < 2) { //We do this so players could TECHNICALLY type /f list 1 and still view the first page
                page = 0;
            }
        }

        Messages.sendList(player, page);
    }

}
