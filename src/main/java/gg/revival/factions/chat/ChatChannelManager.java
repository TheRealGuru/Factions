package gg.revival.factions.chat;

import gg.revival.factions.tools.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class ChatChannelManager {

    @Getter
    public static HashSet<ChatChannel> activeChatChannels = new HashSet<>();

    public static ChatChannel getChannel(UUID uuid) {
        for (ChatChannel activeChannels : activeChatChannels) {
            if (activeChannels.getChannelMembers().contains(uuid)) {
                return activeChannels;
            }
        }

        return null;
    }

    public static ChatChannel getChannelByName(String query) {
        for (ChatChannel activeChannels : activeChatChannels) {
            if (activeChannels.getChannelName().equalsIgnoreCase(query)) {
                return activeChannels;
            }
        }

        return null;
    }

    public static void addToChannel(UUID uuid, ChatChannel channel) {
        if (getChannel(uuid) != null)
            return;

        channel.getChannelMembers().add(uuid);

        if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
            Logger.log(Level.INFO, Bukkit.getPlayer(uuid).getName() + " joined chat-channel '" + channel.getChannelName() + "'");
        }
    }

    public static void removeFromChannel(UUID uuid) {
        ChatChannel channel = getChannel(uuid);

        if (channel == null)
            return;

        channel.getChannelMembers().remove(uuid);

        if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
            Logger.log(Level.INFO, Bukkit.getPlayer(uuid).getName() + " left chat-channel '" + channel.getChannelName() + "'");
        }
    }

    public static void createChannel(String name, String password, UUID creator) {
        ChatChannel channel = new ChatChannel(creator, name, password);

        channel.getChannelMembers().add(creator);
        activeChatChannels.add(channel);

        Logger.log(Level.INFO, "Chat channel '" + channel.getChannelName() + "' has been created by " + Bukkit.getPlayer(creator).getName());
    }

    public static void deleteChannel(ChatChannel channel) {
        activeChatChannels.remove(channel);
        channel.getChannelMembers().clear();

        Logger.log(Level.INFO, "Chat channel '" + channel.getChannelName() + "' has been deleted");
    }
}
