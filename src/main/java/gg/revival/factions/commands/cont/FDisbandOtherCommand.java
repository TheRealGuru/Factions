package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FDisbandOtherCommand extends FCommand {

    public FDisbandOtherCommand() {
        super(
                "disbandfor",
                Arrays.asList("delete"),
                "/f disbandfor <name>",
                "Disband other players factions",
                Permissions.ADMIN,
                2,
                2,
                false);
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        String disbander = "Console";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            disbander = player.getName();

            if (!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }
        }

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String factionName = args[1];

        if (FactionManager.getFactionByName(factionName) == null) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        Faction faction = FactionManager.getFactionByName(factionName);

        FactionManager.disbandFaction(disbander, faction);
    }
}
