package gg.revival.factions.core;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;

public class FactionManager {

    private static HashSet<Faction> activeFactions = new HashSet<Faction>();

    public static HashSet<Faction> getFactions() {
        return activeFactions;
    }

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

        PlayerFaction faction = new PlayerFaction(factionID, displayName, leader,
                officers, members, allies, pendingInvites, pendingAllies,
                null, 0.0, BigDecimal.valueOf(0.1), System.currentTimeMillis());

        activeFactions.add(faction);

        Bukkit.broadcastMessage(Messages.factionCreated(faction.getDisplayName(), Bukkit.getPlayer(leader).getName()));
    }

    public static void disbandFaction(String disbander, Faction faction) {
        if (Configuration.DB_ENABLED && MongoAPI.isConnected()) {
            new BukkitRunnable() {
                public void run() {
                    MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "factions");
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

        activeFactions.remove(faction);

        Bukkit.broadcastMessage(Messages.factionDisbanded(faction.getDisplayName(), disbander));
    }

    public static void loadFactions() {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "factions");
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

                        activeFactions.add(faction);

                        ClaimManager.loadClaims(faction);
                    } else {
                        UUID factionID = UUID.fromString(current.getString("factionID"));
                        String displayName = current.getString("displayName");
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

                        PlayerFaction faction = new PlayerFaction(factionID, displayName, leader,
                                officers, members, allies, pendingInvites, pendingAllies, announcement,
                                balance, BigDecimal.valueOf(dtr), unfreezeTime);

                        activeFactions.add(faction);

                        ClaimManager.loadClaims(faction);
                    }
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void saveFaction(Faction faction, boolean unloadOnCompletion) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "factions");
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

                    if (document != null) {
                        collection.updateOne(document, newDoc);
                    } else {
                        collection.insertOne(newDoc);
                    }

                    if (!playerFaction.getClaims().isEmpty()) {
                        for (Claim claims : playerFaction.getClaims()) {
                            ClaimManager.saveClaim(claims);
                        }
                    }
                } else {
                    ServerFaction serverFaction = (ServerFaction) faction;

                    Document newDoc = new Document("factionID", faction.getFactionID().toString())
                            .append("displayName", serverFaction.getDisplayName())
                            .append("type", serverFaction.getType().toString());

                    if (document != null) {
                        collection.updateOne(document, newDoc);
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
}
