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
import java.util.Set;

public class TimerThread {

    public static void run() {
        Set<Faction> factionCache = new HashSet<>(); Set<FPlayer> playerCache = new HashSet<>();

        for(Faction factions : FactionManager.getActiveFactions()) {
            factionCache.add(factions);
        }

        for(FPlayer players : PlayerManager.getActivePlayers()) {
            playerCache.add(players);
        }

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
