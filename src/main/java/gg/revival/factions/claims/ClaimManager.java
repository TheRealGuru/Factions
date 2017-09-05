package gg.revival.factions.claims;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.revival.driver.MongoAPI;
import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.db.DatabaseManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.pillars.Pillar;
import gg.revival.factions.pillars.PillarManager;
import gg.revival.factions.tools.*;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

public class ClaimManager {

    @Getter static Set<Claim> activeClaims = new HashSet<>();
    @Getter static Map<UUID, PendingClaim> claimEditors = new HashMap<>();

    public static void removeFromClaimEditor(UUID uuid) {
        if(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
            Player player = Bukkit.getPlayer(uuid);

            for(ItemStack contents : player.getInventory().getContents()) {
                if(contents == null) continue;
                if(!contents.hasItemMeta()) continue;
                if(!contents.getItemMeta().getDisplayName().equals(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) continue;

                player.getInventory().remove(contents);
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
        if (Configuration.DB_ENABLED && MongoAPI.isConnected()) {
            new BukkitRunnable() {
                public void run() {
                    if(DatabaseManager.getClaimsCollection() == null)
                        DatabaseManager.setClaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "claims"));

                    MongoCollection<Document> collection = DatabaseManager.getClaimsCollection();
                    FindIterable<Document> query = collection.find(Filters.eq("claimID", claim.getClaimID().toString()));
                    Document document = query.first();

                    if (document != null) {
                        collection.findOneAndDelete(document);
                    }
                }
            }.runTaskAsynchronously(FP.getInstance());
        }

        activeClaims.remove(claim);
    }

    public static void loadClaims(Faction faction) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                if(DatabaseManager.getClaimsCollection() == null)
                    DatabaseManager.setClaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "claims"));

                MongoCollection<Document> collection = DatabaseManager.getClaimsCollection();
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
                    double claimValue = document.getDouble("claimValue");

                    Claim claim = new Claim(claimID, claimOwner, x1, x2, y1, y2, z1, z2, worldName, claimValue);

                    faction.getClaims().add(claim);
                    activeClaims.add(claim);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void saveClaim(Claim claim) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        new BukkitRunnable() {
            public void run() {
                if(DatabaseManager.getClaimsCollection() == null)
                    DatabaseManager.setClaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "claims"));

                MongoCollection<Document> collection = DatabaseManager.getClaimsCollection();
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
                    collection.replaceOne(document, newDoc);
                } else {
                    collection.insertOne(newDoc);
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    /**
     * Used to save a claim on the main thread, should only be used in the onDisable method
     * @param claim
     */
    public static void unsafeSaveClaim(Claim claim) {
        if (!Configuration.DB_ENABLED || !MongoAPI.isConnected())
            return;

        if(DatabaseManager.getClaimsCollection() == null)
            DatabaseManager.setClaimsCollection(MongoAPI.getCollection(Configuration.DB_NAME, "claims"));

        MongoCollection<Document> collection = DatabaseManager.getClaimsCollection();
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
            collection.replaceOne(document, newDoc);
        } else {
            collection.insertOne(newDoc);
        }
    }

    public static void performClaimAction(Action action, Player player, Location clickedLocation) {
        if(getPendingClaim(player.getUniqueId()) == null)
            return;

        PendingClaim pendingClaim = getPendingClaim(player.getUniqueId());
        Faction faction = pendingClaim.getClaimingFor();

        if(action.equals(Action.LEFT_CLICK_BLOCK)) {
            pendingClaim.setPosA(clickedLocation);
            player.sendMessage(Messages.claimPointSet(1));

            Pillar pillar = new Pillar(player.getUniqueId(), pendingClaim.getPosA(), Material.EMERALD_BLOCK, (byte)0);

            for(Pillar pillars : PillarManager.getActivePillars(player.getUniqueId())) {
                if(!pillars.getLocation().equals(pendingClaim.getPosA()) && !pillars.getLocation().equals(pendingClaim.getPosB())) {
                    pillars.remove();
                    PillarManager.getActivePillars().remove(pillars);
                }
            }

            pillar.build();
            PillarManager.getActivePillars().add(pillar);

            if(pendingClaim.getPosA() != null && pendingClaim.getPosB() != null && faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;
                double totalValue = pendingClaim.calculateCost();

                for(Claim claims : playerFaction.getClaims()) {
                    totalValue = totalValue + claims.getClaimValue();
                }

                player.sendMessage(Messages.claimCost(totalValue));
            }
        }

        if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            pendingClaim.setPosB(clickedLocation);
            player.sendMessage(Messages.claimPointSet(2));

            Pillar pillar = new Pillar(player.getUniqueId(), pendingClaim.getPosB(), Material.EMERALD_BLOCK, (byte)0);

            for(Pillar pillars : PillarManager.getActivePillars(player.getUniqueId())) {
                if(!pillars.getLocation().equals(pendingClaim.getPosA()) && !pillars.getLocation().equals(pendingClaim.getPosB())) {
                    pillars.remove();
                    PillarManager.getActivePillars().remove(pillars);
                }
            }

            // Run 1 tick later so the bottom block isn't clicked
            new BukkitRunnable() {
                public void run() {
                    pillar.build();
                }
            }.runTask(FP.getInstance());

            PillarManager.getActivePillars().add(pillar);

            if(pendingClaim.getPosA() != null && pendingClaim.getPosB() != null && faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;
                double totalValue = pendingClaim.calculateCost();

                for(Claim claims : playerFaction.getClaims()) {
                    totalValue = totalValue + claims.getClaimValue();
                }

                player.sendMessage(Messages.claimCost(totalValue));
            }
        }

        if(action.equals(Action.LEFT_CLICK_AIR) && player.isSneaking()) {

            if(pendingClaim.getPosA() == null || pendingClaim.getPosB() == null) {
                player.sendMessage(Messages.claimUnfinished());
                return;
            }

            double claimValue = pendingClaim.calculateCost();
            double totalValue = claimValue;
            int x1 = pendingClaim.getX1();
            int x2 = pendingClaim.getX2();
            int y1 = pendingClaim.getY1();
            int y2 = pendingClaim.getY2();
            int z1 = pendingClaim.getZ1();
            int z2 = pendingClaim.getZ2();
            String worldName = pendingClaim.getPosA().getWorld().getName();

            if(faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;

                if(!playerFaction.getClaims().isEmpty()) {
                    if(!playerFaction.isTouching(pendingClaim.getPosA()) && !playerFaction.isTouching(pendingClaim.getPosB())) {
                        player.sendMessage(Messages.claimNotConnected());
                        return;
                    }
                }

                if(pendingClaim.isTooSmall()) {
                    player.sendMessage(Messages.claimTooSmall());
                    return;
                }

                for(Claim claims : playerFaction.getClaims()) {
                    totalValue = totalValue + claims.getClaimValue();
                }

                for(Claim claims : ClaimManager.getActiveClaims()) {
                    if(claims.overlaps(x1, x2, z1, z2)) {
                        player.sendMessage(Messages.claimOverlapping());
                        return;
                    }

                    for(Location blocks : pendingClaim.getPerimeter(worldName, 64)) {
                        if(claims.nearby(blocks, Configuration.CLAIM_BUFFER) && !claims.getClaimOwner().getFactionID().equals(faction.getFactionID())) {
                            player.sendMessage(Messages.claimTooClose());
                            return;
                        }
                    }
                }

                if(ToolBox.overlapsWarzone(x1, x2, z1, z2)) {
                    player.sendMessage(Messages.cantClaimWarzone());
                    return;
                }

                if(playerFaction.getBalance() < totalValue && !player.hasPermission(Permissions.ADMIN)) {
                    player.sendMessage(Messages.claimTooExpensive());
                    return;
                }

                if(!player.hasPermission(Permissions.ADMIN)) {
                    playerFaction.setBalance(playerFaction.getBalance() - totalValue);
                }

                playerFaction.sendMessage(Messages.landClaimSuccessOther(player.getName(), totalValue));

                y1 = 0; y2 = 256; //Setting the claims to max height
            }

            if(faction instanceof ServerFaction) {
                ServerFaction serverFaction = (ServerFaction)faction;

                for(Claim claims : ClaimManager.getActiveClaims()) {
                    if(claims.overlaps(x1, x2, z1, z2)) {
                        player.sendMessage(Messages.claimOverlapping());
                        return;
                    }
                }

                if(!serverFaction.getType().equals(ServerClaimType.ROAD)) {
                    y1 = 0; y2 = 256; //Setting the claims to max height UNLESS its a road claim where we want players to go under/over it
                }
            }

            for(Pillar pillars : PillarManager.getActivePillars(player.getUniqueId())) {
                pillars.remove();
                PillarManager.getActivePillars().remove(pillars);
            }

            for(ItemStack contents : player.getInventory().getContents()) {
                if(contents == null ||
                        !contents.hasItemMeta() ||
                        !contents.getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) continue;

                player.getInventory().remove(contents);
            }

            player.sendMessage(Messages.landClaimSuccess());

            Claim claim = new Claim(UUID.randomUUID(), faction, x1, x2, y1, y2, z1, z2, worldName, claimValue);

            faction.getClaims().add(claim);
            ClaimManager.getActiveClaims().add(claim);
            ClaimManager.getClaimEditors().remove(player.getUniqueId());

            ClaimManager.saveClaim(claim);

            Logger.log(Level.INFO, player.getName() + " has created a new claim for " + faction.getDisplayName());
        }

        if(action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
            if(pendingClaim.getPosA() != null || pendingClaim.getPosB() != null) {
                pendingClaim.setPosA(null);
                pendingClaim.setPosB(null);

                for(Pillar pillars : PillarManager.getActivePillars(player.getUniqueId())) {
                    pillars.remove();
                    PillarManager.getActivePillars().remove(pillars);
                }

                player.sendMessage(Messages.claimReset());
            }
        }
    }
}
