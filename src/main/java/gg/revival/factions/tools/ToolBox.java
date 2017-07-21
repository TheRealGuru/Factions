package gg.revival.factions.tools;

import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.util.*;

public class ToolBox {

    @Getter public static final List<BlockFace> flatDirections = Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);

    public static String getFormattedCooldown(boolean showDecimal, long duration) {
        if(showDecimal) {
            double seconds = Math.abs(duration / 1000.0f);
            return String.format("%.1f", seconds);
        }

        else {
            return String.valueOf((int)duration / 1000L);
        }
    }

    public static ItemStack getClaimingStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();

        stickMeta.setDisplayName(ChatColor.GREEN + "Faction Claiming Stick");
        //TODO: Get stick displayname from config
        //TODO: Get stick lore from config

        stick.setItemMeta(stickMeta);

        return stick;
    }

    public static WorldLocations getLocationEnum(Location location) {
        if (isWilderness(location)) {
            return WorldLocations.WILDERNESS;
        } else if (isWarzone(location)) {
            return WorldLocations.WARZONE;
        } else if (isNetherWarzone(location)) {
            return WorldLocations.NETHER_WARZONE;
        } else if (isNether(location)) {
            return WorldLocations.NETHER;
        } else if (isEnd(location)) {
            return WorldLocations.END;
        }

        return null;
    }

    public static boolean isNether(Location location) {
        if (location.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            return true;
        }

        return false;
    }

    public static boolean isEnd(Location location) {
        if (location.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            return true;
        }

        return false;
    }

    public static boolean isOverworld(Location location) {
        if (!isNether(location) && !isEnd(location)) {
            return true;
        }

        return false;
    }

    public static boolean isWarzone(Location location) {
        if (isOverworld(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.WARZONE_RADIUS && z <= Configuration.WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNonBuildableWarzone(Location location) {
        if(isWarzone(location)) {
            double x = location.getX();
            double z = location.getZ();

            if(x <= Configuration.BUILDABLE_WARZONE_RADIUS && z <= Configuration.BUILDABLE_WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isWilderness(Location location) {
        if (!isWarzone(location) && isOverworld(location)) {
            return true;
        }

        return false;
    }

    public static boolean isNetherWarzone(Location location) {
        if (isNether(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.NETHER_WARZONE_RADIUS && z <= Configuration.NETHER_WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInSafezone(Player mcPlayer) {
        FPlayer player = PlayerManager.getPlayer(mcPlayer.getUniqueId());

        if(player.getLocation().getCurrentClaim() != null &&
                player.getLocation().getCurrentClaim().getClaimOwner() instanceof ServerFaction) {
            ServerFaction serverFaction = (ServerFaction)player.getLocation().getCurrentClaim().getClaimOwner();

            if(serverFaction.getType().equals(ServerClaimType.SAFEZONE)) {
                return true;
            }
        }

        return false;
    }

    public static boolean overlapsWarzone(double x1, double x2, double z1, double z2) {
        double[] vals = new double[2];

        double min = -Configuration.WARZONE_RADIUS;
        double max = Configuration.WARZONE_RADIUS;

        vals[0] = x1;
        vals[1] = x2;

        Arrays.sort(vals);

        if(min > vals[1] || max < vals[0])
            return false;

        vals[0] = z1;
        vals[1] = z2;

        Arrays.sort(vals);

        if(min > vals[1] || max < vals[0])
            return false;

        return true;
    }

    public static Block getTargetBlock(Player player, int dist) {
        BlockIterator iterator = new BlockIterator(player, dist);
        Block lastBlock = iterator.next();

        while(iterator.hasNext()) {
            lastBlock = iterator.next();

            if(lastBlock.getType().equals(Material.AIR)) continue;

            break;
        }

        return lastBlock;
    }

    public static Map<PlayerFaction, Integer> sortByValue(Map<PlayerFaction, Integer> unsortMap) {
        List<Map.Entry<PlayerFaction, Integer>> list =
                new LinkedList<Map.Entry<PlayerFaction, Integer>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<PlayerFaction, Integer>>() {
            public int compare(Map.Entry<PlayerFaction, Integer> o1,
                               Map.Entry<PlayerFaction, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Collections.reverse(list);

        Map<PlayerFaction, Integer> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<PlayerFaction, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public enum WorldLocations {
        WILDERNESS, WARZONE, NETHER, NETHER_WARZONE, END
    }
}
