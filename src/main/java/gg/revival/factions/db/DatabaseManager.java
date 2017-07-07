package gg.revival.factions.db;

import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.tools.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseManager {

    public static void setupConnection() {
        if (!Configuration.DB_ENABLED)
            return;

        if (MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                if (Configuration.DB_USECREDS) {
                    MongoAPI.connect(
                            Configuration.DB_HOSTNAME,
                            Configuration.DB_PORT,
                            Configuration.DB_USERNAME,
                            Configuration.DB_PASSWORD,
                            Configuration.DB_DATABASE);
                } else {
                    MongoAPI.connect(
                            Configuration.DB_HOSTNAME,
                            Configuration.DB_PORT,
                            null, null, null
                    );
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
