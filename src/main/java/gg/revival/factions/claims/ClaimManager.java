package gg.revival.factions.claims;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.tools.Configuration;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class ClaimManager {

    private static HashSet<Claim> activeClaims = new HashSet<Claim>();

    public static HashSet<Claim> getClaims() {
        return activeClaims;
    }

    public static Claim getClaimAt(Location location, boolean isEntity) {
        for (Claim claims : activeClaims) {
            if (!claims.inside(location, isEntity)) continue;

            return claims;
        }

        return null;
    }

    public static void deleteClaim(Claim claim) {
        if(!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find(Filters.eq("claimID", claim.getClaimID().toString()));
                Document document = query.first();

                if(document != null) {
                    collection.deleteOne(document);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void loadClaims(Faction faction) {
        if(!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find();
                Iterator<Document> iterator = query.iterator();

                while(iterator.hasNext()) {
                    Document document = iterator.next();

                    if(!UUID.fromString(document.getString("factionID")).equals(faction.getFactionID())) continue;

                    UUID claimID = UUID.fromString(document.getString("claimID"));
                    Faction claimOwner = FactionManager.getFactionByUUID(UUID.fromString(document.getString("factionID")));
                    double x1 = document.getDouble("x1");
                    double x2 = document.getDouble("x2");
                    double y1 = document.getDouble("y1");
                    double y2 = document.getDouble("y2");
                    double z1 = document.getDouble("z1");
                    double z2 = document.getDouble("z2");
                    String worldName = document.getString("worldName");
                    int claimValue = document.getInteger("claimValue");

                    Claim claim = new Claim(claimID, claimOwner, x1, x2, y1, y2, z1, z2, worldName, claimValue);

                    faction.getClaims().add(claim);
                    activeClaims.add(claim);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    /*public static void loadClaims() {
        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find();
                Iterator<Document> iterator = query.iterator();

                while(iterator.hasNext()) {
                    Document document = iterator.next();

                    UUID claimID = UUID.fromString(document.getString("claimID"));
                    Faction claimOwner = FactionManager.getFactionByUUID(UUID.fromString(document.getString("factionID")));
                    double x1 = document.getDouble("x1");
                    double x2 = document.getDouble("x2");
                    double y1 = document.getDouble("y1");
                    double y2 = document.getDouble("y2");
                    double z1 = document.getDouble("z1");
                    double z2 = document.getDouble("z2");
                    String worldName = document.getString("worldName");
                    int claimValue = document.getInteger("claimValue");

                    Claim claim = new Claim(claimID, claimOwner, x1, x2, y1, y2, z1, z2, worldName, claimValue);

                    claimOwner.getClaims().add(claim);
                    activeClaims.add(claim);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }*/

    public static void saveClaim(Claim claim) {
        if(!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find(Filters.eq("claimID", claim.getClaimID().toString()));
                Document document = query.first();

                Document newDoc = new Document("claimID", claim.getClaimID().toString())
                        .append("factionID", claim.getClaimOwner().getFactionID().toString())
                        .append("x1", claim.getX1())
                        .append("x2", claim.getX2())
                        .append("y1", claim.getY1())
                        .append("y2", claim.getY2())
                        .append("z1", claim.getZ1())
                        .append("z2", claim.getZ2())
                        .append("worldName", claim.getWorld().getName())
                        .append("claimValue", claim.getClaimValue());

                if(document != null) {
                    collection.updateOne(document, newDoc);
                }

                else {
                    collection.insertOne(newDoc);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
