package gg.revival.factions.threads.cont;

import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.core.bastion.tasks.LogoutTask;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tasks.HomeTask;
import gg.revival.factions.tasks.StuckTask;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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

                    if(timers.getType().equals(TimerType.HOME)) {
                        new BukkitRunnable() {
                            public void run() {
                                if(Bukkit.getPlayer(players.getUuid()) != null) {
                                    HomeTask.sendHome(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if(timers.getType().equals(TimerType.STUCK)) {
                        new BukkitRunnable() {
                            public void run() {
                                if(Bukkit.getPlayer(players.getUuid()) != null) {
                                    StuckTask.unstuck(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if(timers.getType().equals(TimerType.LOGOUT)) {
                        new BukkitRunnable() {
                            public void run() {
                                if(Bukkit.getPlayer(players.getUuid()) != null) {
                                    LogoutTask.logoutPlayer(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }
                }
            }
        }
    }

}
