package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.OfflinePlayerLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FKickCommand extends FCommand {

    public FKickCommand() {
        super(
                "kick",
                null,
                "/f kick <player>",
                "Kick a player from your faction",
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

        String namedPlayer = args[1];

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if (!playerFaction.getLeader().equals(player.getUniqueId()) && !playerFaction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if (Bukkit.getPlayer(namedPlayer) != null) {
            Player kickPlayer = Bukkit.getPlayer(namedPlayer);
            FPlayer facPlayer = PlayerManager.getPlayer(kickPlayer.getUniqueId());

            if (facPlayer.isBeingTimed(TimerType.TAG)) {
                player.sendMessage(Messages.cantWhilePlayerIsTagged());
                return;
            }
        }

        if (playerFaction.isFrozen()) {
            player.sendMessage(Messages.unfrozenRequired());
            return;
        }

        OfflinePlayerLookup.getOfflinePlayerByName(namedPlayer, (uuid, username) -> {
            if (uuid == null || username == null) {
                player.sendMessage(Messages.playerNotFound());
                return;
            }

            if (!playerFaction.getRoster(false).contains(uuid)) {
                player.sendMessage(Messages.playerNotInFaction());
                return;
            }

            if (playerFaction.getLeader().equals(uuid)) {
                player.sendMessage(Messages.cantKickLeader());
                return;
            }

            playerFaction.getOfficers().remove(uuid);
            playerFaction.getMembers().remove(uuid);

            if(Bukkit.getPlayer(uuid) != null) {
                Bukkit.getPlayer(uuid).sendMessage(Messages.playerKickedOther());
            }

            playerFaction.sendMessage(Messages.playerKicked(player.getName(), username));
        });
    }

}
