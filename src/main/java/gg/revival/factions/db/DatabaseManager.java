package gg.revival.factions.db;

import com.mongodb.client.MongoCollection;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class DatabaseManager {

    @Getter @Setter static MongoCollection<Document> factionsCollection;
    @Getter @Setter static MongoCollection<Document> playersCollection;
    @Getter @Setter static MongoCollection<Document> claimsCollection;
    @Getter @Setter static MongoCollection<Document> subclaimsCollection;

}
