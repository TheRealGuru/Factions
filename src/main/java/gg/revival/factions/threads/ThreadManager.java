package gg.revival.factions.threads;

import gg.revival.factions.FP;
import gg.revival.factions.locations.LocationManager;
import gg.revival.factions.threads.cont.LocationThread;
import org.bukkit.scheduler.BukkitRunnable;

public class ThreadManager {

    public static void startThreads() {
        /*
            1 Tick Threads
         */

        new BukkitRunnable() {
            public void run() {

            }
        }.runTaskTimer(FP.getInstance(), 0L, 1L);

        /*
            5 Tick Threads
         */

        new BukkitRunnable() {
            public void run() {

            }
        }.runTaskTimer(FP.getInstance(), 0L, 5L);

        /*
            1 Second Threads
         */

        new BukkitRunnable() {
            public void run() {
                LocationThread.run();
            }
        }.runTaskTimer(FP.getInstance(), 0L, 20L);
    }

}
