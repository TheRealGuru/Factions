package gg.revival.factions.core;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.db.DatabaseManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.subclaims.Subclaim;
import gg.revival.factions.subclaims.SubclaimManager;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.LocationSerialization;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

public class FactionManager {

    @Getter static Set<Faction> activeFactions = new HashSet<>();

    public static Faction getFactionByName(String query) {
        for (Faction factions : activeFactions) {
            if (factions.getDisplayName().equalsIgnoreCase(query)) {
                return factions;
            }
        }

        return null;
    }

    public static Faction getFactionByUUID(UUID query) {
        for (Faction factions : activeFactions) {
            if (factions.getFactionID().equals(query)) {
                return factions;
            }
        }

        return null;
    }

    public static Faction getFactionByPlayer(UUID query) {
        for (Faction factions : activeFactions) {
            if (!(factions instanceof PlayerFaction)) continue;

            PlayerFaction faction = (PlayerFaction) factions;

            if (faction.getRoster(false).contains(query)) {
                return factions;
            }
        }

        return null;
    }

    public static boolean isFactionMember(UUID playerOne, UUID playerTwo) {
        if (getFactionByPlayer(playerOne) == null || getFactionByPlayer(playerTwo) == null) return false;

        PlayerFaction factionOne = (PlayerFaction) getFactionByPlayer(playerOne);
        PlayerFaction factionTwo = (PlayerFaction) getFactionByPlayer(playerTwo);

        if (factionOne.getFactionID().equals(factionTwo.getFactionID())) return true;

        return false;
    }

    public static boolean isAllyMember(UUID playerOne, UUID playerTwo) {
        if (getFactionByPlayer(playerOne) == null || getFactionByPlayer(playerTwo) == null) return false;

        PlayerFaction factionOne = (PlayerFaction) getFactionByPlayer(playerOne);
        PlayerFaction factionTwo = (PlayerFaction) getFactionByPlayer(playerTwo);

        if (factionOne.getAllies().contains(factionTwo.getFactionID())) return true;

        return false;
    }

    public static void createSystemFaction(String creator, String displayName, ServerClaimType type) {
        UUID factionID = UUID.randomUUID();

        ServerFaction faction = new ServerFaction(factionID, displayName, type);

        activeFactions.add(faction);

        Bukkit.broadcastMessage(Messages.factionCreated(displayName, creator));
    }

    public static void createPlayerFaction(UUID leader, String displayName) {
        UUID factionID = UUID.randomUUID();

        List<UUID>
                officers = new ArrayList<UUID>(),
                members = new ArrayList<UUID>(),
                allies = new ArrayList<UUID>(),
                pendingInvites = new ArrayList<UUID>(),
                pendingAllies = new ArrayList<UUID>();

        PlayerFaction faction = new PlayerFaction(factionID, displayName, null, leader,
                officers, members, allies, pendingInvites, pendingAllies,
                null, 0.0, BigDecimal.valueOf(0.1), System.currentTimeMillis());

        activeFactions.add(faction);

        Bukkit.broadcastMessage(Messages.factionCreated(faction.getDisplayName(), Bukkit.getPlayer(leader).getName()));

        Logger.log(Level.INFO, faction.getDisplayName() + " has been created by " + Bukkit.getPlayer(leader).getName());
    }

    public static void disbandFaction(String disbander, Faction faction) {
        if (Configuration.DB_ENABLED && MongoAPI.isConnected()) {
            new BukkitRunnable() {
                public void run() {
                    MongoCollection<Document> collection = DatabaseManager.getFactionsCollection();
                    FindIterable<Document> query = collection.find(Filters.eq("factionID", faction.getFactionID().toString()));
                    Document document = query.first();

                    if (document != null) {
                        collection.deleteOne(document);
                    }
                }
            }.runTaskAsynchronously(FP.getInstance());
        }

        for (Claim claims : faction.getClaims()) {
            ClaimManager.deleteClaim(claims);
        }

        for (Subclaim subclaims : faction.getSubclaims()) {
            SubclaimManager.deleteSubclaim(subclaims);
        }

        activeFactions.remove(faction);

        Bukkit.broadcastMessage(Messages.factionDisbanded(faction.getDisplayName(), disbander));

        Logger.log(Level.INFO, faction.getDisplayName() + " has been disbanded by " + disbander);
    }

    public static void saveAllFactions(boolean unsafe, boolean unload) {
        if(!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        for(Faction factions : activeFactions) {
            if(unsafe) {
                unsafeSaveFaction(factions);
            }

            else {
                saveFaction(factions, unload);
            }
        }
    }

    public static void loadFactions() {
        if(!Configuration.DB_ENABLED)
            return;

        if(!MongoAPI.isConnected()) {
            Logger.log(Level.WARNING, "It appears you are trying to use a Database, but the connection has not yet been established. I'll wait a few seconds and try again!");
            new BukkitRunnable() {
                public void run() {
                    loadFactions();
                }
            }.runTaskLater(FP.getInstance(), 2 * 20L);

            return;
        }

        new BukkitRunnable() {
            public void run() {
                MongoCollection collection = DatabaseManager.getFactionsCollection();

                if(collection == null) {
                    Logger.log(Level.WARNING, "Collection response time was too slow... Waiting a few seconds and trying again!");

                    new BukkitRunnable() {
                        public void run() {
                            loadFactions();
                        }
                    }.runTaskLater(FP.getInstance(), 2 * 20L);

                    return;
                }

                FindIterable<Document> query = collection.find();
                Iterator<Document> iterator = query.iterator();

                while (iterator.hasNext()) {
                    Document current = iterator.next();

                    if (current.getString("type") != null) {
                        UUID factionID = UUID.fromString(current.getString("factionID"));
                        String displayname = current.getString("displayName");
                        String foundType = current.getString("type");
                        ServerClaimType type = null;

                        switch (foundType) {
                            case "SAFEZONE":
                                type = ServerClaimType.SAFEZONE;
                                break;
                            case "EVENT":
                                type = ServerClaimType.EVENT;
                                break;
                            case "ROAD":
                                type = ServerClaimType.ROAD;
                        }

                        ServerFaction faction = new ServerFaction(factionID, displayname, type);

                        if(getFactionByUUID(factionID) == null) {
                            activeFactions.add(faction);
                            ClaimManager.loadClaims(faction);
                        }
                    } else {
                        UUID factionID = UUID.fromString(current.getString("factionID"));
                        String displayName = current.getString("displayName");

                        Location homeLocation = null;
                        if(current.getString("homeLocation") != null && current.getString("homeLocation").length() > 0) {
                            homeLocation = LocationSerialization.deserializeLocation(current.getString("homeLocation"));
                        }

                        UUID leader = UUID.fromString(current.getString("leader"));
                        List<UUID> officers = (List<UUID>) current.get("officers");
                        List<UUID> members = (List<UUID>) current.get("members");
                        List<UUID> allies = (List<UUID>) current.get("allies");
                        List<UUID> pendingInvites = (List<UUID>) current.get("pendingInvites");
                        List<UUID> pendingAllies = (List<UUID>) current.get("pendingAllies");
                        String announcement = current.getString("announcement");
                        double balance = current.getDouble("balance");
                        double dtr = current.getDouble("dtr");
                        long unfreezeTime = current.getLong("unfreezeTime");

                        PlayerFaction faction = new PlayerFaction(factionID, displayName, homeLocation, leader,
                                officers, members, allies, pendingInvites, pendingAllies, announcement,
                                balance, BigDecimal.valueOf(dtr), unfreezeTime);

                        if(getFactionByUUID(factionID) == null) {
                            activeFactions.add(faction);
                            ClaimManager.loadClaims(faction);
                            SubclaimManager.loadSubclaims(faction);
                        }
                    }

                    Logger.log(Level.INFO, "Loaded " + activeFactions.size() + " Factions");
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void saveFaction(Faction faction, boolean unloadOnCompletion) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection collection = DatabaseManager.getFactionsCollection();
                FindIterable<Document> query = collection.find(Filters.eq("factionID", faction.getFactionID()));
                Document document = query.first();

                if (faction instanceof PlayerFaction) {
                    PlayerFaction playerFaction = (PlayerFaction) faction;

                    Document newDoc = new Document("factionID", faction.getFactionID().toString())
                            .append("displayName", faction.getDisplayName())
                            .append("leader", playerFaction.getLeader().toString())
                            .append("officers", playerFaction.getOfficers())
                            .append("members", playerFaction.getMembers())
                            .append("allies", playerFaction.getAllies())
                            .append("pendingInvites", playerFaction.getPendingAllies())
                            .append("pendingAllies", playerFaction.getPendingAllies())
                            .append("announcement", playerFaction.getAnnouncement())
                            .append("balance", playerFaction.getBalance())
                            .append("dtr", playerFaction.getDtr().doubleValue())
                            .append("unfreezeTime", playerFaction.getUnfreezeTime());

                    if(playerFaction.getHomeLocation() != null) {
                        newDoc.append("homeLocation", LocationSerialization.serializeLocation(playerFaction.getHomeLocation()));
                    }

                    if (document != null) {
                        collection.deleteOne(document);
                        collection.insertOne(newDoc);
                    } else {
                        collection.insertOne(newDoc);
                    }

                    if (!playerFaction.getClaims().isEmpty()) {
                        for (Claim claims : playerFaction.getClaims()) {
                            ClaimManager.saveClaim(claims);
                        }
                    }

                    if(!playerFaction.getSubclaims().isEmpty()) {
                        for (Subclaim subclaims : playerFaction.getSubclaims()) {
                            SubclaimManager.saveSubclaim(subclaims);
                        }
                    }
                } else {
                    ServerFaction serverFaction = (ServerFaction) faction;

                    Document newDoc = new Document("factionID", faction.getFactionID().toString())
                            .append("displayName", serverFaction.getDisplayName())
                            .append("type", serverFaction.getType().toString());

                    if (document != null) {
                        collection.deleteOne(document);
                        collection.insertOne(newDoc);
                    } else {
                        collection.insertOne(newDoc);
                    }

                    if (!serverFaction.getClaims().isEmpty()) {
                        for (Claim claims : serverFaction.getClaims()) {
                            ClaimManager.saveClaim(claims);
                        }
                    }
                }

                if (unloadOnCompletion) {
                    activeFactions.remove(faction);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    /**
     * Runs a save on the main thread, should only be used in the onDisable method
     * @param faction
     */
    public static void unsafeSaveFaction(Faction faction) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        MongoCollection collection = DatabaseManager.getFactionsCollection();
        FindIterable<Document> query = collection.find(Filters.eq("factionID", faction.getFactionID()));
        Document document = query.first();

        if (faction instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction) faction;

            Document newDoc = new Document("factionID", faction.getFactionID().toString())
                    .append("displayName", faction.getDisplayName())
                    .append("leader", playerFaction.getLeader().toString())
                    .append("officers", playerFaction.getOfficers())
                    .append("members", playerFaction.getMembers())
                    .append("allies", playerFaction.getAllies())
                    .append("pendingInvites", playerFaction.getPendingAllies())
                    .append("pendingAllies", playerFaction.getPendingAllies())
                    .append("announcement", playerFaction.getAnnouncement())
                    .append("balance", playerFaction.getBalance())
                    .append("dtr", playerFaction.getDtr().doubleValue())
                    .append("unfreezeTime", playerFaction.getUnfreezeTime());

            if(playerFaction.getHomeLocation() != null) {
                newDoc.append("homeLocation", LocationSerialization.serializeLocation(playerFaction.getHomeLocation()));
            }

            if (document != null) {
                collection.deleteOne(document);
                collection.insertOne(newDoc);
            } else {
                collection.insertOne(newDoc);
            }

            if (!playerFaction.getClaims().isEmpty()) {
                for (Claim claims : playerFaction.getClaims()) {
                    ClaimManager.unsafeSaveClaim(claims);
                }
            }

            if(!playerFaction.getSubclaims().isEmpty()) {
                for (Subclaim subclaims : playerFaction.getSubclaims()) {
                    SubclaimManager.unsafeSaveSubclaim(subclaims);
                }
            }
        } else {
            ServerFaction serverFaction = (ServerFaction) faction;

            Document newDoc = new Document("factionID", faction.getFactionID().toString())
                    .append("displayName", serverFaction.getDisplayName())
                    .append("type", serverFaction.getType().toString());

            if (document != null) {
                collection.deleteOne(document);
                collection.insertOne(newDoc);
            } else {
                collection.insertOne(newDoc);
            }

            if (!serverFaction.getClaims().isEmpty()) {
                for (Claim claims : serverFaction.getClaims()) {
                    ClaimManager.unsafeSaveClaim(claims);
                }
            }
        }
    }
}
