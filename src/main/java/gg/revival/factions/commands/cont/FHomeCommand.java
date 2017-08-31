package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FHomeCommand extends FCommand {

    public FHomeCommand() {
        super(
                "home",
                Arrays.asList("base", "h"),
                "/f home",
                "Warp to your faction home",
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
        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if(faction == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        if(faction.getHomeLocation() == null) {
            // TODO: Send home not set message
            return;
        }

        // TODO: Attempt to execute faction home process in Factions core
    }

}
