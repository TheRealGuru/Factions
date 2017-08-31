package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class FLeaderCommand extends FCommand {

    public FLeaderCommand() {
        super(
                "leader",
                null,
                "/f leader <new leader>",
                "Assign a new faction leader",
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

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId())) {
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
                                if(uuid == null || offlinePlayer == null || offlinePlayer.getName() == null) {
                                    player.sendMessage(Messages.playerNotFound());
                                    return;
                                }

                                String properUsername = Bukkit.getOfflinePlayer(uuid).getName();

                                if(!faction.getRoster(false).contains(uuid)) {
                                    player.sendMessage(Messages.playerNotInFaction());
                                    return;
                                }

                                faction.setLeader(uuid);
                                faction.getOfficers().add(player.getUniqueId());

                                faction.sendMessage(Messages.newLeader(player.getName(), properUsername));
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
