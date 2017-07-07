package gg.revival.factions.core;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.tools.Configuration;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class PlayerManager {

    private static HashSet<FPlayer> activePlayers = new HashSet<FPlayer>();

    public static HashSet<FPlayer> getPlayers() {
        return activePlayers;
    }

    public static boolean isLoaded(UUID uuid) {
        if (getPlayer(uuid) != null) {
            return true;
        }

        return false;
    }

    public static FPlayer getPlayer(UUID uuid) {
        for (FPlayer players : activePlayers) {
            if (players.getUuid().equals(uuid)) {
                return players;
            }
        }

        return null;
    }

    public static void loadPlayer(UUID uuid, double balance) {
        if (isLoaded(uuid)) return;

        FPlayer player = new FPlayer(uuid, balance);

        activePlayers.add(player);
    }

    public static void unloadPlayer(UUID uuid) {
        if (!isLoaded(uuid)) return;

        activePlayers.remove(getPlayer(uuid));
    }

    public static void updatePlayer(FPlayer player) {
        unloadPlayer(player.getUuid());

        activePlayers.add(player);
    }

    public static void loadProfile(UUID uuid) {
        if (!Configuration.DB_ENABLED) {
            loadPlayer(uuid, 0.0);
            return;
        }

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "players");
                FindIterable<Document> query = collection.find(Filters.eq("uuid", uuid.toString()));
                Document document = query.first();

                if (document != null) {
                    double balance = document.getDouble("balance");

                    loadPlayer(uuid, balance);
                } else {
                    loadPlayer(uuid, 0.0);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void saveProfile(FPlayer player, boolean unloadOnCompletion) {
        if (!Configuration.DB_ENABLED)
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "players");
                FindIterable<Document> query = collection.find(Filters.eq("uuid", player.getUuid().toString()));
                Document document = query.first();

                Document newDoc = new Document("uuid", player.getUuid().toString())
                        .append("balance", player.getBalance());

                if (document != null) {
                    collection.replaceOne(document, newDoc);
                } else {
                    collection.insertOne(newDoc);
                }

                if (unloadOnCompletion) {
                    unloadPlayer(player.getUuid());
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
