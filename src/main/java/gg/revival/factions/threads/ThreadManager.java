package gg.revival.factions.threads;

import gg.revival.factions.FP;
import gg.revival.factions.threads.cont.LocationThread;
import gg.revival.factions.threads.cont.SaveThread;
import gg.revival.factions.threads.cont.TimerThread;
import org.bukkit.scheduler.BukkitRunnable;

public class ThreadManager {

    public static void startThreads() {
        new BukkitRunnable() {
            public void run() {
                LocationThread.run();
            }
        }.runTaskTimer(FP.getInstance(), 0L, 20L);

        new BukkitRunnable() {
            public void run() {
                TimerThread.run();
            }
        }.runTaskTimerAsynchronously(FP.getInstance(), 0L, 1L);

        new BukkitRunnable() {
            public void run() {
                SaveThread.run();
            }
        }.runTaskTimer(FP.getInstance(), 0L, 300 * 20L);
    }

}
