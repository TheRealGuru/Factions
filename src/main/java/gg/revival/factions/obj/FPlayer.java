package gg.revival.factions.obj;

import gg.revival.factions.locations.FLocation;
import gg.revival.factions.scoreboards.FScoreboard;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.UUID;

public class FPlayer {

    @Getter
    UUID uuid;
    @Getter
    @Setter
    double balance;
    @Getter
    @Setter
    FScoreboard scoreboard;
    @Getter
    @Setter
    FLocation location;
    @Getter
    @Setter
    HashSet<Timer> timers = new HashSet<Timer>();

    public FPlayer(UUID uuid, double balance) {
        this.uuid = uuid;
        this.balance = balance;
        this.scoreboard = new FScoreboard("Scoreboard Title"); //TODO: Update scoreboard title
        this.location = new FLocation(uuid);
        this.timers = new HashSet<Timer>();
    }

    public boolean isBeingTimed(TimerType type) {
        if (this.timers.isEmpty()) return false;

        for (Timer activeTimers : this.timers) {
            if (activeTimers.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    public void addTimer(Timer timer) {
        if (isBeingTimed(timer.getType())) {
            removeTimer(timer.getType());
        }

        this.timers.add(timer);
    }

    public void removeTimer(TimerType type) {
        if (!isBeingTimed(type)) return;

        for (Timer activeTimers : this.timers) {
            if (activeTimers.getType().equals(type)) {
                this.timers.remove(activeTimers);
            }
        }
    }

}
