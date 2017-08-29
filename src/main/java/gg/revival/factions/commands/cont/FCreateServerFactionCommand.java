package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FCreateServerFactionCommand extends FCommand {

    public FCreateServerFactionCommand() {
        super("createserver",
                Arrays.asList("createsystem", "cs"),
                "/f createserver <name> <safezone/road/event>",
                "Create a server-side faction",
                Permissions.ADMIN,
                3,
                3,
                true);
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(getPermission())) {
            player.sendMessage(Messages.noPermission());
            return;
        }

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String factionName = args[1];
        String factionTypeAsString = args[2].toUpperCase();
        ServerClaimType type = null;

        switch (factionTypeAsString) {
            case "SAFEZONE":
                type = ServerClaimType.SAFEZONE;
                break;
            case "ROAD":
                type = ServerClaimType.ROAD;
                break;
            case "EVENT":
                type = ServerClaimType.EVENT;
                break;
        }

        if (type == null) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (!StringUtils.isAlphanumeric(factionName)) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (factionName.length() < Configuration.MIN_FAC_NAME_SIZE || factionName.length() > Configuration.MAX_FAC_NAME_SIZE) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (factionName.contains(" ")) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (FactionManager.getFactionByName(factionName) != null) {
            player.sendMessage(Messages.facNameInUse());
            return;
        }

        FactionManager.createSystemFaction(player.getName(), factionName, type);
    }

}
