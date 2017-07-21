package gg.revival.factions.chat;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatChannel {

    @Getter UUID owner;
    @Getter @Setter String channelName;
    @Getter @Setter String channelPassword;
    @Getter List<UUID> channelMembers;
    @Getter List<UUID> channelInvites;

    public ChatChannel(UUID owner, String channelName, String channelPassword) {
        this.owner = owner;
        this.channelName = channelName;
        this.channelPassword = channelPassword;
        this.channelMembers = new ArrayList<>();
        this.channelInvites = new ArrayList<>();
    }

    public void sendMessage(String message) {
        for(UUID uuid : channelMembers) {
            if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) continue;

            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

}
