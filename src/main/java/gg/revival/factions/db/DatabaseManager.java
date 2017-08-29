package gg.revival.factions.db;

import com.mongodb.client.MongoCollection;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseManager {

    @Getter @Setter static MongoCollection<Document> factionsCollection;
    @Getter @Setter static MongoCollection<Document> playersCollection;
    @Getter @Setter static MongoCollection<Document> claimsCollection;

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

                if(MongoAPI.isConnected())
                {
                    factionsCollection = MongoAPI.getCollection(Configuration.DB_DATABASE, "factions");
                    playersCollection = MongoAPI.getCollection(Configuration.DB_DATABASE, "players");
                    claimsCollection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
