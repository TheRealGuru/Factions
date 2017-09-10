package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.OfflinePlayerLookup;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FPromoteCommand extends FCommand {

    public FPromoteCommand() {
        super(
                "promote",
                Arrays.asList("officer"),
                "/f promote <player>",
                "Promote a member of your faction to officer",
                null,
                CmdCategory.MANAGE,
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

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if (!faction.getLeader().equals(player.getUniqueId()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.leaderRequired());
            return;
        }

        String namedPlayer = args[1];

        OfflinePlayerLookup.getOfflinePlayerByName(namedPlayer, (uuid, username) -> {
            if (uuid == null || username == null) {
                player.sendMessage(Messages.playerNotFound());
                return;
            }

            if (!faction.getRoster(false).contains(player.getUniqueId())) {
                player.sendMessage(Messages.playerNotInFaction());
                return;
            }

            if (faction.getOfficers().contains(uuid)) {
                player.sendMessage(Messages.alreadyOfficer());
                return;
            }

            faction.getMembers().remove(uuid);
            faction.getOfficers().add(uuid);

            faction.sendMessage(Messages.newOfficer(player.getName(), username));

            Logger.log(Level.INFO, player.getName() + " promoted " + username + " to officer in the faction " + faction.getDisplayName());
        });
    }

}
