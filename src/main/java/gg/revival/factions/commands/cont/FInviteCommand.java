package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.OfflinePlayerLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FInviteCommand extends FCommand {

    public FInviteCommand() {
        super(
                "invite",
                Arrays.asList("inv"),
                "/f invite <player>",
                "Invite a player to your faction",
                null,
                CmdCategory.BASICS,
                2,
                2,
                true);
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

        if (!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        String namedPlayer = args[1];

        OfflinePlayerLookup.getOfflinePlayerByName(namedPlayer, (uuid, username) -> {
            if (uuid == null || username == null) {
                player.sendMessage(Messages.playerNotFound());
            }

            if (FactionManager.getFactionByPlayer(uuid) != null) {
                player.sendMessage(Messages.alreadyInFactionOther());
                return;
            }

            if (faction.getPendingInvites().contains(uuid)) {
                player.sendMessage(Messages.playerAlreadyInvited());
                return;
            }

            faction.getPendingInvites().add(uuid);

            if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
                Messages.sendFactionInvite(Bukkit.getPlayer(uuid), faction.getDisplayName(), player.getName());
            }

            faction.sendMessage(Messages.invitedPlayer(player.getName(), username));

            Logger.log(Level.INFO, player.getName() + " invited " + username + " to join " + faction.getDisplayName());
        });
    }

}
