package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class FPromoteCommand extends FCommand {

    public FPromoteCommand() {
        super(
                "promote",
                Arrays.asList("officer"),
                "/f promote <player>",
                "Promote a member of your faction to officer",
                null,
                2,
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

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.leaderRequired());
            return;
        }

        String namedPlayer = args[1];

        new BukkitRunnable() {
            public void run() {
                try {
                    UUID uuid = UUIDFetcher.getUUIDOf(namedPlayer);

                    if(uuid != null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        new BukkitRunnable() {
                            public void run() {
                                if(uuid == null || offlinePlayer == null) {
                                    player.sendMessage(Messages.playerNotFound());
                                    return;
                                }

                                String properUsername = Bukkit.getOfflinePlayer(uuid).getName();

                                if(!faction.getRoster(false).contains(player.getUniqueId())) {
                                    player.sendMessage(Messages.playerNotInFaction());
                                    return;
                                }

                                if(faction.getOfficers().contains(uuid)) {
                                    player.sendMessage(Messages.alreadyOfficer());
                                    return;
                                }

                                faction.getOfficers().add(uuid);

                                faction.sendMessage(Messages.newOfficer(player.getName(), properUsername));

                                Logger.log(Level.INFO, player.getName() + " promoted " + properUsername + " to officer in the faction " + faction.getDisplayName());
                            }
                        }.runTask(FP.getInstance());
                    }

                    else {
                        new BukkitRunnable() {
                            public void run() {
                                player.sendMessage(Messages.playerNotFound());
                                return;
                            }
                        }.runTask(FP.getInstance());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
