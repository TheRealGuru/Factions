package gg.revival.factions.tools;

import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.util.*;

public class ToolBox {

    @Getter
    public static final List<BlockFace> flatDirections = Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);

    public static String getFormattedCooldown(boolean showDecimal, long duration) {
        if (showDecimal) {
            double seconds = Math.abs(duration / 1000.0f);
            return String.format("%.1f", seconds);
        } else {
            return String.valueOf((int) duration / 1000L);
        }
    }

    public static ItemStack getClaimingStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();

        stickMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("custom-items.faction-claiming-stick.display-name")));

        List<String> lore = new ArrayList<>();

        for (String unformattedLore : FileManager.getConfig().getStringList("custom-items.faction-claiming-stick.description")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', unformattedLore));
        }

        stickMeta.setLore(lore);

        stick.setItemMeta(stickMeta);

        return stick;
    }

    public static void sendMessageWithPermission(String message, String permissions) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.hasPermission(permissions)) continue;

            players.sendMessage(message);
        }
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
        return location.getWorld().getEnvironment().equals(World.Environment.NETHER);
    }

    public static boolean isEnd(Location location) {
        return location.getWorld().getEnvironment().equals(World.Environment.THE_END);
    }

    public static boolean isOverworld(Location location) {
        return !isNether(location) && !isEnd(location);
    }

    public static boolean isWarzone(Location location) {
        if (isOverworld(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.WARZONE_RADIUS && z <= Configuration.WARZONE_RADIUS)
                return true;
        }

        return false;
    }

    public static boolean isNonBuildableWarzone(Location location) {
        if (isWarzone(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.BUILDABLE_WARZONE_RADIUS && z <= Configuration.BUILDABLE_WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isWilderness(Location location) {
        return !isWarzone(location) && isOverworld(location);
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

        if (player.getLocation().getCurrentClaim() != null &&
                player.getLocation().getCurrentClaim().getClaimOwner() instanceof ServerFaction) {
            ServerFaction serverFaction = (ServerFaction) player.getLocation().getCurrentClaim().getClaimOwner();

            if (serverFaction.getType().equals(ServerClaimType.SAFEZONE)) {
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

        if (min > vals[1] || max < vals[0])
            return false;

        vals[0] = z1;
        vals[1] = z2;

        Arrays.sort(vals);

        return !(min > vals[1]) && !(max < vals[0]);
    }

    public static Block getTargetBlock(Player player, int dist) {
        BlockIterator iterator = new BlockIterator(player, dist);
        Block lastBlock = iterator.next();

        while (iterator.hasNext()) {
            lastBlock = iterator.next();
            if (lastBlock.getType().equals(Material.AIR)) continue;
            break;
        }

        return lastBlock;
    }

    public static Map<PlayerFaction, Integer> sortByValue(Map<PlayerFaction, Integer> unsortMap) {
        List<Map.Entry<PlayerFaction, Integer>> list =
                new LinkedList<Map.Entry<PlayerFaction, Integer>>(unsortMap.entrySet());

        Collections.sort(list, Comparator.comparing(o -> (o.getValue())));

        Collections.reverse(list);

        Map<PlayerFaction, Integer> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<PlayerFaction, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static int getTime(String string) {
        int time = 0;

        if (string.contains("m")) {
            String timeStr = strip(string);

            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 60;
            }

        } else if (string.contains("h")) {
            String timeStr = strip(string);

            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 3600;
            }

        } else if (string.contains("s")) {
            String timeStr = strip(string);

            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr);
            }

        } else if (string.contains("d")) {
            String timeStr = strip(string);

            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 86400;
            }
        } else if (string.contains("y")) {
            String timeStr = strip(string);

            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 31536000;
            }
        }

        return time;
    }

    private static String strip(String src) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);

            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public enum WorldLocations {
        WILDERNESS, WARZONE, NETHER, NETHER_WARZONE, END
    }
}
