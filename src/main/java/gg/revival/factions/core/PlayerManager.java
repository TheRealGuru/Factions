package gg.revival.factions.core;

import com.google.common.collect.Sets;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.db.DatabaseManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerManager {

    @Getter static Set<FPlayer> activePlayers = Sets.newConcurrentHashSet();

    public static boolean isLoaded(UUID uuid) {
        return getPlayer(uuid) != null;
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

    public static void saveAllProfiles(boolean unsafe, boolean unload) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (unsafe)
                unsafeSaveProfile(PlayerManager.getPlayer(players.getUniqueId()));
            else
                saveProfile(PlayerManager.getPlayer(players.getUniqueId()), unload);
        }
    }

    public static void loadProfile(UUID uuid, boolean unsafe) {
        if (!Configuration.DB_ENABLED) {
            loadPlayer(uuid, 0.0);
            return;
        }

        if(unsafe) {
            if (DatabaseManager.getPlayersCollection() == null)
                DatabaseManager.setPlayersCollection(MongoAPI.getCollection(Configuration.DB_NAME, "players"));

            MongoCollection<Document> collection = DatabaseManager.getPlayersCollection();
            FindIterable<Document> query;

            try {
                query = MongoAPI.getQueryByFilter(collection, "uuid", uuid.toString());
            } catch (LinkageError err) {
                loadProfile(uuid, unsafe);
                return;
            }

            Document document = query.first();

            if (document != null) {
                double balance = document.getDouble("balance");

                loadPlayer(uuid, balance);
            } else {
                loadPlayer(uuid, 0.0);
            }
        }

        else {
            new BukkitRunnable() {
                public void run() {
                    if (DatabaseManager.getPlayersCollection() == null)
                        DatabaseManager.setPlayersCollection(MongoAPI.getCollection(Configuration.DB_NAME, "players"));

                    MongoCollection<Document> collection = DatabaseManager.getPlayersCollection();
                    FindIterable<Document> query;

                    try {
                        query = MongoAPI.getQueryByFilter(collection, "uuid", uuid.toString());
                    } catch (LinkageError err) {
                        loadProfile(uuid, unsafe);
                        return;
                    }

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
    }

    public static void saveProfile(FPlayer player, boolean unloadOnCompletion) {
        if (!Configuration.DB_ENABLED)
            return;

        if (player == null)
            return;

        new BukkitRunnable() {
            public void run() {
                if (DatabaseManager.getPlayersCollection() == null)
                    DatabaseManager.setPlayersCollection(MongoAPI.getCollection(Configuration.DB_NAME, "players"));

                MongoCollection<Document> collection = DatabaseManager.getPlayersCollection();
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
                    new BukkitRunnable() {
                        public void run() {
                            if (Bukkit.getPlayer(player.getUuid()) == null || !Bukkit.getPlayer(player.getUuid()).isOnline()) {
                                unloadPlayer(player.getUuid());
                            }
                        }
                    }.runTask(FP.getInstance());
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    /**
     * Runs a save on the main thread, should only be used in the onDisable method
     *
     * @param player
     */
    public static void unsafeSaveProfile(FPlayer player) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Runnable saveTask = () -> {
            if (!Configuration.DB_ENABLED)
                return;

            if (player == null)
                return;

            if (DatabaseManager.getPlayersCollection() == null)
                DatabaseManager.setPlayersCollection(MongoAPI.getCollection(Configuration.DB_NAME, "players"));

            MongoCollection<Document> collection = DatabaseManager.getPlayersCollection();
            FindIterable<Document> query = collection.find(Filters.eq("uuid", player.getUuid().toString()));
            Document document = query.first();

            Document newDoc = new Document("uuid", player.getUuid().toString())
                    .append("balance", player.getBalance());

            if (document != null) {
                collection.replaceOne(document, newDoc);
            } else {
                collection.insertOne(newDoc);
            }
        };

        executorService.submit(saveTask);
    }
}
