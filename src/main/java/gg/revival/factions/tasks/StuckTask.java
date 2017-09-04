package gg.revival.factions.tasks;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Messages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class StuckTask
{

    /**
     * Contains every player currently performing a /f stuck and the location they started at
     */
    @Getter static Map<UUID, Location> startingLocations = new HashMap<>();

    /**
     * Returns the location the player started their /f stuck at
     * @param uuid The player UUID
     * @return The players starting location
     */
    public static Location getStartingLocation(UUID uuid)
    {
        if(startingLocations.containsKey(uuid))
            return startingLocations.get(uuid);

        return null;
    }

    /**
     * Checks to make sure every player currently unstucking has not moved too far
     */
    public static void checkLocations()
    {
        List<UUID> userCache = new CopyOnWriteArrayList<>(startingLocations.keySet());

        for(UUID uuid : userCache)
        {
            if(Bukkit.getPlayer(uuid) == null)
            {
                startingLocations.remove(uuid);
                continue;
            }

            Player player = Bukkit.getPlayer(uuid);
            FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());
            Location current = player.getLocation();
            Location expected = getStartingLocation(player.getUniqueId());

            if(expected.distance(current) >= 1.0 || expected.getWorld() != current.getWorld())
            {
                if(facPlayer.isBeingTimed(TimerType.STUCK))
                {
                    facPlayer.removeTimer(TimerType.STUCK);
                    player.sendMessage(Messages.stuckWarpCancelled());
                }
            }
        }
    }

    /**
     * Teleports the player outside of all claims to the highest Y level block
     * @param uuid
     */
    public static void unstuck(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null)
        {
            startingLocations.remove(uuid);
            return;
        }

        Claim inside = ClaimManager.getClaimAt(player.getLocation(), true);

        if(inside == null)
        {
            player.sendMessage(Messages.notInsideClaim());
            return;
        }

        int x = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();

        while(ClaimManager.getClaimAt(new Location(player.getWorld(), x, player.getLocation().getBlockY(), z), true) != null)
        {
            x += 5;
            z += 5;
        }

        Location safeBlock = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z);

        player.sendMessage(Messages.unstuck());
        player.teleport(safeBlock.add(0, 2, 0));

        startingLocations.remove(player.getUniqueId());
    }

}
