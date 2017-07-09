package gg.revival.factions.claims;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class ClaimManager {

    @Getter public static HashSet<Claim> activeClaims = new HashSet<Claim>();
    @Getter public static HashMap<UUID, PendingClaim> claimEditors = new HashMap<UUID, PendingClaim>();

    public static void removeFromClaimEditor(UUID uuid) {
        if(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
            Player player = Bukkit.getPlayer(uuid);

            for(ItemStack contents : player.getInventory().getContents()) {
                if(contents != null && contents.hasItemMeta() && contents.getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) {
                    player.getInventory().remove(contents);
                }
            }
        }

        if(claimEditors.containsKey(uuid)) {
            claimEditors.remove(uuid);
        }
    }

    public static PendingClaim getPendingClaim(UUID uuid) {
        if(claimEditors.containsKey(uuid)) {
            return claimEditors.get(uuid);
        }

        return null;
    }

    public static Claim getClaimAt(Location location, boolean isEntity) {
        for (Claim claims : activeClaims) {
            if (!claims.inside(location, isEntity)) continue;

            return claims;
        }

        return null;
    }

    public static void deleteClaim(Claim claim) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find(Filters.eq("claimID", claim.getClaimID().toString()));
                Document document = query.first();

                if (document != null) {
                    collection.deleteOne(document);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void loadClaims(Faction faction) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                MongoCollection<Document> collection = MongoAPI.getCollection(Configuration.DB_DATABASE, "claims");
                FindIterable<Document> query = collection.find();
                Iterator<Document> iterator = query.iterator();

                while (iterator.hasNext()) {
                    Document document = iterator.next();

                    if (!UUID.fromString(document.getString("factionID")).equals(faction.getFactionID())) continue;

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
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
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

                if (document != null) {
                    collection.updateOne(document, newDoc);
                } else {
                    collection.insertOne(newDoc);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void performClaimAction(Action action, Player player, Location clickedLocation) {
        if(getPendingClaim(player.getUniqueId()) == null)
            return;

        PendingClaim pendingClaim = getPendingClaim(player.getUniqueId());
        Faction faction = pendingClaim.getClaimingFor();

        if(action.equals(Action.LEFT_CLICK_BLOCK)) {
            pendingClaim.setPosA(clickedLocation);

            //TODO: Draw pillar at clickedLocation
        }

        if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            pendingClaim.setPosB(clickedLocation);

            //TODO: Draw pillar at clickedLocation
        }

        if(action.equals(Action.LEFT_CLICK_AIR) && player.isSneaking()) {

            if(pendingClaim.getPosA() == null || pendingClaim.getPosB() == null) {
                //TODO: Send claim not finished error
                return;
            }

            double claimValue = pendingClaim.calculateCost();
            int x1 = pendingClaim.getX1();
            int x2 = pendingClaim.getX2();
            int y1 = pendingClaim.getY1();
            int y2 = pendingClaim.getY2();
            int z1 = pendingClaim.getZ1();
            int z2 = pendingClaim.getZ2();
            String worldName = pendingClaim.getPosA().getWorld().getName();

            if(faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;

                for(Claim claims : ClaimManager.getActiveClaims()) {
                    if(claims.overlaps(x1, x2, z1, z2)) {
                        //TODO: Send claim overlapping
                        return;
                    }

                    if(ToolBox.overlapsWarzone(x1, x2, z1, z2)) {
                        //TODO: Send cant claim in warzone
                        return;
                    }

                    for(Location blocks : pendingClaim.getBlockPeremeter(worldName, 64)) {
                        if(claims.nearby(blocks, Configuration.CLAIM_BUFFER) && !claims.getClaimOwner().getFactionID().equals(faction.getFactionID())) {
                            //TODO: Send claim too close to other factions
                            return;
                        }
                    }
                }

                if(playerFaction.getBalance() < claimValue && !player.hasPermission(Permissions.ADMIN)) {
                    //TODO: Send claim too expensive
                    return;
                }

                if(!player.hasPermission(Permissions.ADMIN)) {
                    playerFaction.setBalance(playerFaction.getBalance() - claimValue);
                }

                y1 = 0; y2 = 256; //Setting the claims to max height
            }

            if(faction instanceof ServerFaction) {
                ServerFaction serverFaction = (ServerFaction)faction;

                for(Claim claims : ClaimManager.getActiveClaims()) {
                    if(claims.overlaps(x1, x2, z1, z2)) {
                        //TODO: Send claim overlapping
                        return;
                    }
                }

                if(!serverFaction.getType().equals(ServerClaimType.ROAD)) {
                    y1 = 0; y2 = 256; //Setting the claims to max height UNLESS its a road claim where we want players to go under/over it
                }
            }

            Claim claim = new Claim(UUID.randomUUID(), faction, x1, x2, y1, y2, z1, z2, worldName, claimValue);

            faction.getClaims().add(claim);
            ClaimManager.getActiveClaims().add(claim);

            ClaimManager.saveClaim(claim);
        }

        if(action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
            pendingClaim.setPosA(null);
            pendingClaim.setPosB(null);
            //TODO: Send claim reset
        }

    }
}
