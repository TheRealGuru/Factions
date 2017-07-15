package gg.revival.factions.timers;

import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.PlayerFaction;

public class TimerManager {

    public static Timer createTimer(TimerType type, int dur) {
        long ms = System.currentTimeMillis() + (dur * 1000);

        Timer timer = new Timer(type, ms);

        return timer;
    }

    public static void finishTimer(FPlayer player, TimerType type) {
        player.removeTimer(type);
    }

    public static void finishTimer(PlayerFaction faction, TimerType type) {
        faction.getTimers().remove(faction.getTimer(type));
    }

}
