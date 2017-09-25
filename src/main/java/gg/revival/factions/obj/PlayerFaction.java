package gg.revival.factions.obj;

import gg.revival.factions.FP;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerFaction extends Faction {

    @Getter @Setter UUID leader;
    @Getter @Setter List<UUID> officers, members, allies, pendingInvites, pendingAllies, factionChat, allyChat;
    @Getter @Setter String announcement;
    @Getter @Setter double balance;
    @Getter @Setter BigDecimal dtr;
    @Getter @Setter long regenTime, unfreezeTime;
    @Getter @Setter Location homeLocation;
    @Getter @Setter List<Timer> timers;

    public PlayerFaction(UUID factionID, String displayName, Location homeLocation, UUID leader,
                         List<UUID> officers, List<UUID> members, List<UUID> allies,
                         List<UUID> pendingInvites, List<UUID> pendingAllies,
                         String announcement, double balance, BigDecimal dtr,
                         long unfreezeTime) {

        super(factionID, displayName);

        this.homeLocation = homeLocation;
        this.leader = leader;
        this.officers = officers;
        this.members = members;
        this.allies = allies;
        this.pendingInvites = pendingInvites;
        this.pendingAllies = pendingAllies;
        this.factionChat = new ArrayList<>();
        this.allyChat = new ArrayList<>();
        this.announcement = announcement;
        this.balance = balance;
        this.dtr = dtr;
        this.regenTime = System.currentTimeMillis();
        this.unfreezeTime = unfreezeTime;
        this.timers = new ArrayList<>();

        new BukkitRunnable() {
            public void run() {
                updateFreeze();
                updateRegen();
                updateDTR();
            }
        }.runTaskTimerAsynchronously(FP.getInstance(), 0L, 20L);
    }

    public List<Timer> getTimersSnapshot() {
        List<Timer> foundTimers = new ArrayList<>();
        foundTimers.addAll(timers);
        return foundTimers;
    }

    public List<UUID> getRoster(boolean onlineOnly) {
        List<UUID> roster = new ArrayList<UUID>();

        if (onlineOnly) {
            if (Bukkit.getPlayer(leader) != null && Bukkit.getPlayer(leader).isOnline()) {
                roster.add(leader);
            }

            for (UUID officer : officers) {
                if (Bukkit.getPlayer(officer) != null && Bukkit.getPlayer(officer).isOnline()) {
                    roster.add(officer);
                }
            }

            for (UUID member : members) {
                if (Bukkit.getPlayer(member) != null && Bukkit.getPlayer(member).isOnline()) {
                    roster.add(member);
                }
            }
        } else {
            roster.add(leader);
            roster.addAll(officers);
            roster.addAll(members);
        }

        return roster;
    }

    public void updateDTR() {
        if(dtr.doubleValue() > getMaxDTR()) dtr = BigDecimal.valueOf(getMaxDTR());
    }

    public void updateRegen() {
        if (isFrozen() || getDtr().doubleValue() >= getMaxDTR() || getRegenTime() > System.currentTimeMillis()) return;

        int baseTime = this.getRoster(false).size() * 20;
        int playerMultiplyer = 0, nextRegenInSeconds = 0;

        if (!this.getRoster(false).isEmpty())
            playerMultiplyer = this.getRoster(false).size() * 15;

        nextRegenInSeconds = (baseTime - playerMultiplyer) * 1000;

        this.regenTime = System.currentTimeMillis() + nextRegenInSeconds;

        this.dtr = getDtr().add(BigDecimal.valueOf(0.1));
    }

    public void updateFreeze() {
        if (isFrozen()) return; //Still frozen

        if (getUnfreezeTime() != 0 && getUnfreezeTime() < System.currentTimeMillis()) {
            this.unfreezeTime = 0;

            sendMessage(Messages.powerThawedOther());
        }
    }

    public double getMaxDTR() {
        double max = Configuration.DTR_PLAYER_VALUE * getRoster(false).size();

        if (max > Configuration.DTR_MAX)
            max = Configuration.DTR_MAX;

        return max;
    }

    public Timer getTimer(TimerType type) {
        if (!isBeingTimed(type))
            return null;

        for(Timer timer : getTimersSnapshot()) {
            if(timer.getType().equals(type)) {
                return timer;
            }
        }

        return null;
    }

    public boolean isBeingTimed(TimerType type) {
        if (timers.isEmpty())
            return false;

        for(Timer timer : getTimersSnapshot()) {
            if(timer.getType().equals(type))
                return true;
        }

        return false;
    }

    public void sendMessage(String message) {
        for (UUID onlinePlayers : getRoster(true)) {
            Player player = Bukkit.getPlayer(onlinePlayers);

            player.sendMessage(message);
        }
    }

    public boolean isFrozen() {
        return this.unfreezeTime != 0 && this.unfreezeTime > System.currentTimeMillis();
    }

    public boolean isRaidable() {
        return this.dtr.doubleValue() < 0.0;
    }
}
