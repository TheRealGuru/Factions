package gg.revival.factions.obj;

import gg.revival.factions.FP;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerFaction extends Faction {

    @Getter
    UUID leader;
    @Getter
    @Setter
    List<UUID> officers, members, allies, pendingInvites, pendingAllies, factionChat, allyChat;
    @Getter
    @Setter
    String announcement;
    @Getter
    @Setter
    double balance;
    @Getter
    @Setter
    BigDecimal dtr;
    @Getter
    @Setter
    long regenTime, unfreezeTime;

    public PlayerFaction(UUID factionID, String displayName, UUID leader,
                         List<UUID> officers, List<UUID> members, List<UUID> allies,
                         List<UUID> pendingInvites, List<UUID> pendingAllies,
                         String announcement, double balance, BigDecimal dtr,
                         long unfreezeTime) {

        super(factionID, displayName);

        this.leader = leader;
        this.officers = officers;
        this.members = members;
        this.allies = allies;
        this.pendingInvites = pendingInvites;
        this.pendingAllies = pendingAllies;
        this.factionChat = new ArrayList<UUID>();
        this.allyChat = new ArrayList<UUID>();
        this.announcement = announcement;
        this.balance = balance;
        this.dtr = dtr;
        this.regenTime = System.currentTimeMillis();
        this.unfreezeTime = unfreezeTime;

        new BukkitRunnable() {
            public void run() {
                updateFreeze();
                updateRegen();
            }
        }.runTaskTimerAsynchronously(FP.getInstance(), 0L, 20L);
    }

    public List<UUID> getRoster(boolean onlineOnly) {
        List<UUID> roster = new ArrayList<UUID>();

        if (onlineOnly) {
            if (Bukkit.getPlayer(this.leader).isOnline()) {
                roster.add(this.leader);
            }

            for (UUID officer : officers) {
                if (Bukkit.getPlayer(officer).isOnline()) {
                    roster.add(officer);
                }
            }

            for (UUID member : members) {
                if (Bukkit.getPlayer(member).isOnline()) {
                    roster.add(member);
                }
            }
        } else {
            roster.add(this.leader);

            for (UUID officer : officers) {
                roster.add(officer);
            }

            for (UUID member : members) {
                roster.add(member);
            }
        }

        return roster;
    }

    public void updateRegen() {
        if (isFrozen()) return; //They are frozen
        if (getDtr().doubleValue() >= getMaxDTR()) return; //They have max DTR
        if (getRegenTime() > System.currentTimeMillis()) return; //Regen time hasnt been reached yet

        int baseTime = this.getRoster(false).size() * 20;
        int playerMultiplyer = 0, nextRegenInSeconds = 0;

        if (!this.getRoster(false).isEmpty()) {
            playerMultiplyer = this.getRoster(false).size() * 15;
        }

        nextRegenInSeconds = (baseTime - playerMultiplyer) * 1000;

        this.regenTime = System.currentTimeMillis() + nextRegenInSeconds;

        this.dtr = getDtr().add(BigDecimal.valueOf(0.1));
    }

    public void updateFreeze() {
        if (isFrozen()) return; //Still frozen

        if (getUnfreezeTime() < System.currentTimeMillis()) {
            this.unfreezeTime = 0;

            //TODO: Notify of power regenerating
        }
    }

    public double getMaxDTR() {
        double max = Configuration.DTR_PLAYER_VALUE * getRoster(false).size();

        if (max > Configuration.DTR_MAX) {
            max = Configuration.DTR_MAX;
        }

        return max;
    }

    public void sendMessage(String message) {
        for (UUID onlinePlayers : getRoster(true)) {
            Player player = Bukkit.getPlayer(onlinePlayers);

            player.sendMessage(message);
        }
    }

    public boolean isFrozen() {
        if (this.unfreezeTime != 0 && this.unfreezeTime > System.currentTimeMillis()) {
            return true;
        }

        return false;
    }

    public boolean isRaidable() {
        if (this.dtr.doubleValue() < 0.0) {
            return true;
        }

        return false;
    }
}
