package gg.revival.factions.threads.cont;

import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;

import java.util.HashSet;

public class TimerThread {

    public static void run() {
        HashSet<Faction> factionCache = (HashSet<Faction>)FactionManager.getFactions().clone();
        HashSet<FPlayer> playerCache = (HashSet<FPlayer>)PlayerManager.getPlayers().clone();

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
