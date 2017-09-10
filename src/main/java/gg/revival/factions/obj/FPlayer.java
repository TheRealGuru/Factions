package gg.revival.factions.obj;

import gg.revival.factions.locations.FLocation;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class FPlayer {

    @Getter UUID uuid;
    @Getter @Setter double balance;
    @Getter @Setter FLocation location;
    @Getter @Setter List<Timer> timers;

    public FPlayer(UUID uuid, double balance) {
        this.uuid = uuid;
        this.balance = balance;
        this.location = new FLocation(uuid);
        this.timers = new ArrayList<>();
    }

    public synchronized List<Timer> getTimersSnapshot() {
        List<Timer> foundTimers = new ArrayList<>();
        foundTimers.addAll(timers);
        return foundTimers;
    }

    public boolean isBeingTimed(TimerType type) {
        if (timers.isEmpty()) return false;

        for (Timer timer : getTimersSnapshot()) {
            if (timer.getType().equals(type))
                return true;
        }

        return false;
    }

    public Timer getTimer(TimerType type) {
        if (timers.isEmpty()) return null;

        for (Timer timer : getTimersSnapshot()) {
            if (timer.getType().equals(type))
                return timer;
        }

        return null;
    }

    public void addTimer(Timer timer) {
        if (isBeingTimed(timer.getType())) {
            removeTimer(timer.getType());
        }

        timers.add(timer);
    }

    public void removeTimer(TimerType type) {
        if (!isBeingTimed(type)) return;

        for (Timer timer : getTimersSnapshot()) {
            if (timer.getType().equals(type))
                timers.remove(timer);
        }
    }
}