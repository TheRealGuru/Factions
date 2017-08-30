package gg.revival.factions.threads.cont;

import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimerThread {

    public static void run() {
        List<Faction> factionCache = new CopyOnWriteArrayList<>(FactionManager.getActiveFactions());
        List<FPlayer> playerCache = new CopyOnWriteArrayList<>(PlayerManager.getActivePlayers());

        for(Faction factions : factionCache) {
            if(!(factions instanceof PlayerFaction) || ((PlayerFaction) factions).getTimers().isEmpty()) continue;

            PlayerFaction faction = (PlayerFaction)factions;

            for(Timer timers : faction.getTimers()) {
                if(timers.getExpire() <= System.currentTimeMillis()) {
                    TimerManager.finishTimer(faction, timers.getType());
                }
            }
        }

        for(FPlayer players : playerCache) {
            if(players.getTimers().isEmpty()) continue;

            for(Timer timers : players.getTimers()) {
                if(timers.getExpire() <= System.currentTimeMillis()) {
                    TimerManager.finishTimer(players, timers.getType());
                }
            }
        }
    }

}
