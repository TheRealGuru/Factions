package gg.revival.factions.listeners.cont;

import gg.revival.factions.chat.ChatChannel;
import gg.revival.factions.chat.ChatChannelManager;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatEventsListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());
        ChatChannel channel = ChatChannelManager.getChannel(player.getUniqueId());

        event.setCancelled(true);

        // TODO: Get ranks and rank colors from api and apply them to chat messages

        if(faction != null && faction.getFactionChat().contains(player.getUniqueId())) {
            faction.sendMessage(Messages.formattedFactionChat(faction.getDisplayName(), player.getName(), event.getMessage()));
            return;
        }


        if(faction != null && faction.getAllyChat().contains(player.getUniqueId())) {
            faction.sendMessage(Messages.formattedAllyChat(faction.getDisplayName(), player.getName(), event.getMessage()));

            for(UUID allyId : faction.getAllies()) {
                Faction allyFaction = FactionManager.getFactionByUUID(allyId);

                if(!(allyFaction instanceof PlayerFaction)) continue;

                PlayerFaction allyPlayerFaction = (PlayerFaction)allyFaction;

                allyPlayerFaction.sendMessage(Messages.formattedAllyChat(faction.getDisplayName(), player.getName(), event.getMessage()));
            }

            return;
        }

        if(channel != null && channel.getChatroom().contains(player.getUniqueId())) {
            channel.sendMessage(Messages.formattedChatChannel(channel.getChannelName(), player.getName(), event.getMessage()));
            return;
        }

        for(Player players : event.getRecipients()) {
            if(faction == null) {
                if(players.getUniqueId().equals(player.getUniqueId())) {
                    players.sendMessage(Messages.formattedGlobalFaction("-", player.getName(), event.getMessage()));
                    continue;
                }

                players.sendMessage(Messages.formattedGlobalEnemy("-", player.getName(), event.getMessage()));
                continue;
            }

            PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(players.getUniqueId());

            if(playerFaction != null && playerFaction.getFactionID().equals(faction.getFactionID())) {
                players.sendMessage(Messages.formattedGlobalFaction(faction.getDisplayName(), player.getName(), event.getMessage()));
                continue;
            }

            if(playerFaction != null && playerFaction.getAllies().contains(faction.getFactionID())) {
                players.sendMessage(Messages.formattedGlobalAlly(faction.getDisplayName(), player.getName(), event.getMessage()));
                continue;
            }

            if(players.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Messages.formattedGlobalFaction(faction.getDisplayName(), player.getName(), event.getMessage()));
                continue;
            }

            players.sendMessage(Messages.formattedGlobalEnemy(faction.getDisplayName(), player.getName(), event.getMessage()));
        }
    }

}
