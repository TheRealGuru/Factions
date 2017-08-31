package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FStuckCommand extends FCommand {

    public FStuckCommand() {
        super(
                "stuck",
                Arrays.asList("unstuck"),
                "/f stuck",
                "Teleport to a safer location",
                null,
                1,
                1,
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

        if(ClaimManager.getClaimAt(player.getLocation(), true) == null) {
            // TODO: Send not inside a claim message
            return;
        }

        // TODO: Attempt to execute faction stuck process in Factions core
    }

}
