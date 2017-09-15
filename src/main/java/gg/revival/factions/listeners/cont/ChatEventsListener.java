package gg.revival.factions.listeners.cont;

import gg.revival.core.Revival;
import gg.revival.core.accounts.Account;
import gg.revival.core.ranks.Rank;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatEventsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        event.setCancelled(true);

        Rank rank = Revival.getRankManager().getRank(player);
        String tag = "";

        if (rank != null) {
            tag = ChatColor.translateAlternateColorCodes('&', rank.getTag());

            if (player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.MOD) || player.hasPermission(Permissions.MANAGER)) {
                tag = ChatColor.WHITE + "[" + tag + rank.getName() + ChatColor.WHITE + "]" + tag;
            }
        }

        if (faction != null && faction.getFactionChat().contains(player.getUniqueId())) {
            faction.sendMessage(Messages.formattedFactionChat(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
            return;
        }


        if (faction != null && faction.getAllyChat().contains(player.getUniqueId())) {
            faction.sendMessage(Messages.formattedAllyChat(faction.getDisplayName(), tag + player.getName(), event.getMessage()));

            for (UUID allyId : faction.getAllies()) {
                Faction allyFaction = FactionManager.getFactionByUUID(allyId);

                if (!(allyFaction instanceof PlayerFaction)) continue;

                PlayerFaction allyPlayerFaction = (PlayerFaction) allyFaction;

                allyPlayerFaction.sendMessage(Messages.formattedAllyChat(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
            }

            return;
        }

        Account playerAccount = Revival.getAccountManager().getAccount(player.getUniqueId());

        if (playerAccount.isHideGlobalChat()) {
            player.sendMessage(ChatColor.RED + "You have global chat toggled off");
            return;
        }

        for (Player players : event.getRecipients()) {
            Account playersAccounts = Revival.getAccountManager().getAccount(players.getUniqueId());

            if (playerAccount != null && playerAccount.getBlockedPlayers().contains(players.getUniqueId())) continue;
            if (playersAccounts != null && playersAccounts.getBlockedPlayers().contains(player.getUniqueId())) continue;
            if (playerAccount != null && playersAccounts.isHideGlobalChat()) continue;

            if (faction == null) {
                if (players.getUniqueId().equals(player.getUniqueId())) {
                    if (player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.MOD)) {
                        players.sendMessage(tag + player.getName() + ChatColor.RESET + ": " + event.getMessage());
                    } else {
                        players.sendMessage(Messages.formattedGlobalFaction("-", tag + player.getName(), event.getMessage()));
                    }

                    continue;
                }

                if (player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.MOD)) {
                    players.sendMessage(tag + player.getName() + ChatColor.RESET + ": " + event.getMessage());
                } else {
                    players.sendMessage(Messages.formattedGlobalEnemy("-", tag + player.getName(), event.getMessage()));
                }

                continue;
            }

            PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(players.getUniqueId());

            if (playerFaction != null && playerFaction.getFactionID().equals(faction.getFactionID())) {
                players.sendMessage(Messages.formattedGlobalFaction(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
                continue;
            }

            if (playerFaction != null && playerFaction.getAllies().contains(faction.getFactionID())) {
                players.sendMessage(Messages.formattedGlobalAlly(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
                continue;
            }

            if (players.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Messages.formattedGlobalFaction(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
                continue;
            }

            players.sendMessage(Messages.formattedGlobalEnemy(faction.getDisplayName(), tag + player.getName(), event.getMessage()));
        }
    }

}
