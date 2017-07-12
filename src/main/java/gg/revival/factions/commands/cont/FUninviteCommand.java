package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.UUID;

public class FUninviteCommand extends FCommand {

    public FUninviteCommand() {
        super(
                "uninvite",
                Arrays.asList("revoke"),
                "/f uninvite <player>",
                "Revoke a players invitation to your faction",
                null,
                2,
                2,
                true);
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

        String namedPlayer = args[1];

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        new BukkitRunnable() {
            public void run() {
                try {
                    UUID uuid = UUIDFetcher.getUUIDOf(namedPlayer);
                    String properUsername = Bukkit.getOfflinePlayer(uuid).getName();

                    new BukkitRunnable() {
                        public void run() {
                            if(uuid == null || properUsername == null) {
                                player.sendMessage(Messages.playerNotFound());
                                return;
                            }

                            if(!faction.getPendingInvites().contains(uuid)) {
                                player.sendMessage(Messages.noPendingInvite());
                                return;
                            }

                            faction.getPendingInvites().remove(uuid);

                            faction.sendMessage(Messages.uninvitedPlayer(player.getName(), properUsername));

                            if(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
                                Bukkit.getPlayer(uuid).sendMessage(Messages.invitationRevoked(faction.getDisplayName()));
                            }
                        }
                    }.runTask(FP.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }
}
