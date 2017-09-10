package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FSethomeForCommand extends FCommand {

    public FSethomeForCommand() {
        super(
                "sethomefor",
                null,
                "/f sethomefor <faction>",
                "Set the home for another faction",
                Permissions.ADMIN,
                CmdCategory.STAFF,
                2,
                2,
                true
        );
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

        if (FactionManager.getFactionByName(factionName) == null) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        Faction faction = FactionManager.getFactionByName(factionName);

        if (!(faction instanceof PlayerFaction)) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;

        for (Claim claims : playerFaction.getClaims()) {
            if (!claims.inside(player.getLocation(), true)) continue;

            playerFaction.setHomeLocation(player.getLocation());

            player.sendMessage(Messages.homeSet());
            playerFaction.sendMessage(Messages.homeSetOther(player.getName()));

            Logger.log(Level.INFO, player.getName() + " set the home location for " + faction.getDisplayName());

            return;
        }

        player.sendMessage(Messages.homeOutsideClaims());
    }

}
