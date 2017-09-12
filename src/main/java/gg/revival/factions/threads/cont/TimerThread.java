package gg.revival.factions.threads.cont;

import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.core.bastion.combatprotection.CombatProtection;
import gg.revival.factions.core.bastion.logout.tasks.LogoutTask;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tasks.HomeTask;
import gg.revival.factions.tasks.StuckTask;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerThread {

    public static void run() {
        for(Faction factions : FactionManager.getActiveFactionsSnapshot()) {
            if (!(factions instanceof PlayerFaction) || ((PlayerFaction) factions).getTimers().isEmpty()) continue;

            PlayerFaction faction = (PlayerFaction) factions;

            for(Timer timers : faction.getTimersSnapshot()) {
                if (timers.isPaused()) {
                    timers.setExpire(System.currentTimeMillis() + timers.getPauseDiff());
                }

                if (timers.getExpire() <= System.currentTimeMillis()) {
                    TimerManager.finishTimer(faction, timers.getType());
                }
            }
        }

        for(FPlayer players : PlayerManager.getActivePlayersSnapshot()) {
            if (players == null || players.getTimersSnapshot() == null || players.getTimersSnapshot().isEmpty()) continue;

            for(Timer timers : players.getTimersSnapshot()) {
                if (timers.isPaused())
                    timers.setExpire(System.currentTimeMillis() + timers.getPauseDiff());

                if (timers.getExpire() <= System.currentTimeMillis()) {
                    TimerManager.finishTimer(players, timers.getType());

                    if (timers.getType().equals(TimerType.HOME)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    HomeTask.sendHome(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.STUCK)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    StuckTask.unstuck(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.LOGOUT)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    LogoutTask.logoutPlayer(players.getUuid());
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.ENDERPEARL)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    Bukkit.getPlayer(players.getUuid()).sendMessage(ChatColor.GREEN + "Your enderpearls have been unlocked");
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.TAG)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    Bukkit.getPlayer(players.getUuid()).sendMessage(ChatColor.GREEN + "Your combat-tag has expired");
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.PVPPROT)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    CombatProtection.takeProtection(Bukkit.getPlayer(players.getUuid()));
                                }
                            }
                        }.runTask(FP.getInstance());
                    }

                    if (timers.getType().equals(TimerType.SAFETY)) {
                        new BukkitRunnable() {
                            public void run() {
                                if (Bukkit.getPlayer(players.getUuid()) != null) {
                                    CombatProtection.takeSafety(Bukkit.getPlayer(players.getUuid()));
                                }
                            }
                        }.runTask(FP.getInstance());
                    }
                }
            }
        }
    }

}
