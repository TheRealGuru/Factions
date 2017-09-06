package gg.revival.factions.obj;

import gg.revival.factions.locations.FLocation;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class FPlayer {

    @Getter UUID uuid;
    @Getter @Setter double balance;
    @Getter @Setter FLocation location;
    @Getter @Setter HashSet<Timer> timers = new HashSet<>();

    public FPlayer(UUID uuid, double balance) {
        this.uuid = uuid;
        this.balance = balance;
        this.location = new FLocation(uuid);
        this.timers = new HashSet<Timer>();
    }

    public boolean isBeingTimed(TimerType type) {
        if (timers.isEmpty()) return false;

        for (Timer activeTimers : timers) {
            if (activeTimers.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    public Timer getTimer(TimerType type) {
        if(timers.isEmpty()) return null;

        for (Timer activeTimers : timers) {
            if (activeTimers.getType().equals(type)) {
                return activeTimers;
            }
        }

        return null;
    }

    public void addTimer(Timer timer) {
        if (isBeingTimed(timer.getType())) {
            removeTimer(timer.getType());
        }

        this.timers.add(timer);
    }

    public void removeTimer(TimerType type) {
        if (!isBeingTimed(type)) return;

        List<Timer> cache = new CopyOnWriteArrayList<>(timers);

        for (Timer activeTimers : cache) {
            if (activeTimers.getType().equals(type)) {
                timers.remove(activeTimers);
            }
        }
    }

}
