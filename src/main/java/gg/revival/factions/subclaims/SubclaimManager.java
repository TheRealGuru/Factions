package gg.revival.factions.subclaims;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.db.DatabaseManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SubclaimManager {

    @Getter static HashSet<Subclaim> activeSubclaims = new HashSet<>();
    @Getter static HashMap<UUID, SubclaimGUI> subclaimEditor = new HashMap<>();

    public static Subclaim getSubclaimAt(Location location) {
        ImmutableList<Subclaim> cache = ImmutableList.copyOf(activeSubclaims);

        for (Subclaim subclaims : cache) {
            if (!subclaims.getLocation().equals(location)) continue;

            return subclaims;
        }

        return null;
    }

    public static boolean isEditingSubclaim(UUID uuid) {
        return getEditedSubclaim(uuid) != null;
    }

    public static SubclaimGUI getEditedSubclaim(UUID uuid) {
        if (subclaimEditor.containsKey(uuid)) {
            return subclaimEditor.get(uuid);
        }

        return null;
    }

    public static boolean subclaimExists(Subclaim subclaim) {
        ImmutableList<Subclaim> cache = ImmutableList.copyOf(activeSubclaims);

        for (Subclaim active : cache) {
            if (active.getSubclaimID().equals(subclaim.getSubclaimID())) {
                return true;
            }
        }

        return false;
    }

    public static void addSubclaim(Subclaim subclaim) {
        if (subclaimExists(subclaim)) return;

        activeSubclaims.add(subclaim);
        subclaim.getSubclaimHolder().getSubclaims().add(subclaim);
    }

    public static void removeSubclaim(Subclaim subclaim) {
        if (!subclaimExists(subclaim)) return;

        ImmutableList<Subclaim> serverCache = ImmutableList.copyOf(activeSubclaims);
        ImmutableList<Subclaim> factionCache = ImmutableList.copyOf(subclaim.getSubclaimHolder().getSubclaims());

        for (Subclaim subclaims : serverCache) {
            if (subclaims.getSubclaimID().equals(subclaim.getSubclaimID()))
                activeSubclaims.remove(subclaims);
        }

        for (Subclaim subclaims : factionCache) {
            if (subclaims.getSubclaimID().equals(subclaim.getSubclaimID()))
                subclaim.getSubclaimHolder().getSubclaims().remove(subclaims);
        }
    }

    public static void performUpdate(Player player, Subclaim subclaim) {
        new BukkitRunnable() {
            public void run() {
                if (player.getOpenInventory() != null)
                    player.closeInventory();

                removeSubclaim(subclaim);
                addSubclaim(subclaim);

                SubclaimGUI newGUI = new SubclaimGUI(player.getUniqueId(), subclaim);

                newGUI.open();

                getSubclaimEditor().put(player.getUniqueId(), newGUI);
            }
        }.runTaskLater(FP.getInstance(), 1L);
    }

    public static void loadSubclaims(PlayerFaction faction) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                if (DatabaseManager.getSubclaimsCollection() == null)
                    DatabaseManager.setSubclaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "subclaims"));

                MongoCollection<Document> collection = DatabaseManager.getSubclaimsCollection();
                FindIterable<Document> query = collection.find();
                Iterator<Document> iterator = query.iterator();

                while (iterator.hasNext()) {
                    Document document = iterator.next();

                    if (!UUID.fromString(document.getString("factionID")).equals(faction.getFactionID())) continue;

                    UUID subclaimID = UUID.fromString(document.getString("subclaimID"));
                    String worldName = document.getString("worldName");
                    double x = document.getDouble("x");
                    double y = document.getDouble("y");
                    double z = document.getDouble("z");
                    List<UUID> accessPlayers = (List<UUID>) document.get("accessPlayers");
                    boolean officerBypass = document.getBoolean("officerBypass");

                    new BukkitRunnable() {
                        public void run() {
                            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

                            Subclaim subclaim = new Subclaim(subclaimID, faction, location, accessPlayers, officerBypass);

                            activeSubclaims.add(subclaim);
                            faction.getSubclaims().add(subclaim);
                        }
                    }.runTask(FP.getInstance());
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void saveSubclaim(Subclaim subclaim) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                if (DatabaseManager.getSubclaimsCollection() == null)
                    DatabaseManager.setSubclaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "subclaims"));

                MongoCollection<Document> collection = DatabaseManager.getSubclaimsCollection();
                FindIterable<Document> query = collection.find(Filters.eq("subclaimID", subclaim.getSubclaimID().toString()));
                Document document = query.first();

                Document newDoc = new Document("subclaimID", subclaim.getSubclaimID().toString())
                        .append("worldName", subclaim.getLocation().getWorld().getName())
                        .append("x", subclaim.getLocation().getX())
                        .append("y", subclaim.getLocation().getY())
                        .append("z", subclaim.getLocation().getZ())
                        .append("factionID", subclaim.getSubclaimHolder().getFactionID().toString())
                        .append("accessPlayers", subclaim.getPlayerAccess())
                        .append("officerBypass", subclaim.isOfficerAccess());

                if (document != null) {
                    collection.replaceOne(document, newDoc);
                } else {
                    collection.insertOne(newDoc);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void unsafeSaveSubclaim(Subclaim subclaim) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Runnable saveTask = () -> {
            if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
                return;

            if (DatabaseManager.getSubclaimsCollection() == null)
                DatabaseManager.setSubclaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "subclaims"));

            MongoCollection<Document> collection = DatabaseManager.getSubclaimsCollection();
            FindIterable<Document> query = collection.find(Filters.eq("subclaimID", subclaim.getSubclaimID().toString()));
            Document document = query.first();

            Document newDoc = new Document("subclaimID", subclaim.getSubclaimID().toString())
                    .append("worldName", subclaim.getLocation().getWorld().getName())
                    .append("x", subclaim.getLocation().getX())
                    .append("y", subclaim.getLocation().getY())
                    .append("z", subclaim.getLocation().getZ())
                    .append("factionID", subclaim.getSubclaimHolder().getFactionID().toString())
                    .append("accessPlayers", subclaim.getPlayerAccess())
                    .append("officerBypass", subclaim.isOfficerAccess());

            if (document != null) {
                collection.replaceOne(document, newDoc);
            } else {
                collection.insertOne(newDoc);
            }
        };

        executorService.execute(saveTask);
    }

    public static void deleteSubclaim(Subclaim subclaim) {
        if (Configuration.DB_ENABLED && MongoAPI.isConnected()) {
            new BukkitRunnable() {
                public void run() {
                    if (DatabaseManager.getSubclaimsCollection() == null)
                        DatabaseManager.setSubclaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "subclaims"));

                    MongoCollection<Document> collection = DatabaseManager.getSubclaimsCollection();
                    FindIterable<Document> query = collection.find(Filters.eq("subclaimID", subclaim.getSubclaimID().toString()));
                    Document document = query.first();

                    if (document != null) {
                        collection.findOneAndDelete(document);
                    }
                }
            }.runTaskAsynchronously(FP.getInstance());
        }

        activeSubclaims.remove(subclaim);
    }
}
