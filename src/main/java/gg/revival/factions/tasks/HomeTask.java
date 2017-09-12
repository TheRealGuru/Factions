package gg.revival.factions.tasks;

import gg.revival.factions.FP;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class HomeTask {

    /**
     * Contains every player who is performing a /f home and the location they started the warp at
     */
    @Getter
    static Map<UUID, Location> startingLocations = new HashMap<>();

    /**
     * Get a players starting location
     *
     * @param uuid The player UUID
     * @return Location the player started the /f home from
     */
    public static Location getStartingLocation(UUID uuid) {
        if (startingLocations.containsKey(uuid))
            return startingLocations.get(uuid);

        return null;
    }

    /**
     * Checks to make sure every play warping home has not moved too far
     */
    public static void checkLocations() {
        List<UUID> userCache = new CopyOnWriteArrayList<>(startingLocations.keySet());

        for (UUID uuid : userCache) {
            if (Bukkit.getPlayer(uuid) == null) {
                startingLocations.remove(uuid);
                continue;
            }

            Player player = Bukkit.getPlayer(uuid);
            FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());
            Location current = player.getLocation();
            Location expected = getStartingLocation(player.getUniqueId());

            if (expected.distance(current) >= 1.0 || expected.getWorld() != current.getWorld()) {
                if (facPlayer.isBeingTimed(TimerType.HOME)) {
                    facPlayer.removeTimer(TimerType.HOME);
                    player.sendMessage(Messages.homeWarpCancelled());
                }
            }
        }
    }

    /**
     * Sends the player to their home location
     *
     * @param uuid The player UUID
     */
    public static void sendHome(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            startingLocations.remove(uuid);
            return;
        }

        Faction faction = FactionManager.getFactionByPlayer(player.getUniqueId());
        FPlayer facPlayer = PlayerManager.getPlayer(uuid);

        if (faction == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        Location homeLocation = playerFaction.getHomeLocation();

        if (homeLocation == null) {
            player.sendMessage(Messages.homeNotSet());
            return;
        }

        if (homeLocation.getBlockY() >= Configuration.MAX_FAC_HOME_HEIGHT) {
            if (playerFaction.getBalance() < Configuration.HOME_TOO_HIGH_PRICE) {
                player.sendMessage(Messages.cantAffordHomeTooHigh());
                return;
            }

            playerFaction.setBalance(playerFaction.getBalance() - Configuration.HOME_TOO_HIGH_PRICE);
        }

        if(facPlayer.isBeingTimed(TimerType.PVPPROT)) {
            TimerManager.finishTimer(facPlayer, TimerType.PVPPROT);
        }

        if (player.getVehicle() != null) {
            Entity entity = player.getVehicle();
            player.getVehicle().eject();

            new BukkitRunnable() {
                public void run() {
                    entity.teleport(homeLocation);
                }
            }.runTaskLater(FP.getInstance(), 1L);
        }

        player.teleport(homeLocation);
        player.sendMessage(Messages.returnedHome());

        startingLocations.remove(player.getUniqueId());
    }

}
