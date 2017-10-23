package gg.revival.factions.tools;

import gg.revival.factions.file.FileManager;
import org.bukkit.ChatColor;

public class Configuration {

    public static boolean DB_ENABLED = false;
    public static String DB_NAME = "factions";

    public static int CHANNEL_MIN_NAME_SIZE = 2;
    public static int CHANNEL_MAX_NAME_SIZE = 10;

    public static int DTR_REGEN_TIME = 60;
    public static int DTR_FREEZE_TIME = 3600;
    public static double DTR_PLAYER_VALUE = 0.6;
    public static double DTR_MAX = 5.5;

    public static int MIN_FAC_NAME_SIZE = 3;
    public static int MAX_FAC_NAME_SIZE = 16;

    public static int MAX_FAC_SIZE = 30;
    public static int MAX_FAC_HOME_HEIGHT = 150;
    public static int HOME_TOO_HIGH_PRICE = 100;
    public static int HOME_WARMUP = 5;
    public static int HOME_WARMUP_OTHERWORLD = 60;
    public static int STUCK_WARMUP = 120;

    public static int WARZONE_RADIUS = 1000;
    public static int NETHER_WARZONE_RADIUS = 200;
    public static int BUILDABLE_WARZONE_RADIUS = 150;
    public static String WARZONE_NAME = ChatColor.RED + "Warzone";
    public static String WILDERNESS_NAME = ChatColor.DARK_GREEN + "Wilderness";
    public static String NETHER_NAME = ChatColor.DARK_RED + "The Nether";
    public static String NETHER_WARZONE_NAME = ChatColor.DARK_RED + "Nether Warzone";
    public static String END_NAME = ChatColor.DARK_PURPLE + "The End";

    public static int MIN_CLAIM_SIZE = 10;
    public static int CLAIM_BUFFER = 5;
    public static double PILLAR_HEIGHT = 30.0;

    public static boolean ALLIANCES_ENABLED = true;
    public static int ALLY_LIMIT = 1;
    public static boolean ONLY_WARN_WHEN_ALLY_VISABLE = true;

    public static String SUBCLAIM_GUI_NAME = ChatColor.BLACK + "Subclaim Editor";

    public static int RENAME_COOLDOWN = 300;
    public static int DISBAND_COOLDOWN = 300;
    public static int MAP_COOLDOWN = 15;

    public static double STARTING_BALANCE = 300.0;

    public static void loadConfiguration() {
        DB_ENABLED = FileManager.getConfig().getBoolean("database.enabled");
        DB_NAME = FileManager.getConfig().getString("database.db");

        CHANNEL_MIN_NAME_SIZE = FileManager.getConfig().getInt("chat-channels.create.min-name-length");
        CHANNEL_MAX_NAME_SIZE = FileManager.getConfig().getInt("chat-channels.create.max-name-length");

        MIN_FAC_NAME_SIZE = FileManager.getConfig().getInt("factions.create.min-name-length");
        MAX_FAC_NAME_SIZE = FileManager.getConfig().getInt("factions.create.max-name-length");

        DTR_REGEN_TIME = FileManager.getConfig().getInt("factions.dtr.regen-time");
        DTR_FREEZE_TIME = FileManager.getConfig().getInt("factions.dtr.freeze-time");
        DTR_MAX = FileManager.getConfig().getDouble("factions.dtr.max-dtr");
        DTR_PLAYER_VALUE = FileManager.getConfig().getDouble("factions.dtr.player-value");

        MAX_FAC_SIZE = FileManager.getConfig().getInt("factions.limits.max-faction-size");
        MAX_FAC_HOME_HEIGHT = FileManager.getConfig().getInt("factions.limits.max-home-height");
        HOME_TOO_HIGH_PRICE = FileManager.getConfig().getInt("factions.limits.home-too-high-price");

        HOME_WARMUP = FileManager.getConfig().getInt("timers.home-warmup");
        HOME_WARMUP_OTHERWORLD = FileManager.getConfig().getInt("timers.home-warmup-other");
        STUCK_WARMUP = FileManager.getConfig().getInt("timers.stuck-warmup");

        WARZONE_RADIUS = FileManager.getConfig().getInt("factions.map-setup.warzone-radius");
        NETHER_WARZONE_RADIUS = FileManager.getConfig().getInt("factions.map-setup.nether-warzone-radius");
        BUILDABLE_WARZONE_RADIUS = FileManager.getConfig().getInt("factions.map-setup.buildable-warzone-radius");
        WARZONE_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.map-setup.warzone-name"));
        WILDERNESS_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.map-setup.wilderness-name"));
        NETHER_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.map-setup.nether-name"));
        NETHER_WARZONE_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.map-setup.nether-warzone-name"));
        END_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.map-setup.end-name"));

        MIN_CLAIM_SIZE = FileManager.getConfig().getInt("factions.claiming.minimum-claim-size");
        CLAIM_BUFFER = FileManager.getConfig().getInt("factions.claiming.claim-buffer");
        PILLAR_HEIGHT = FileManager.getConfig().getDouble("factions.claiming.pillar-height");

        ALLIANCES_ENABLED = FileManager.getConfig().getBoolean("factions.alliances.enabled");
        ALLY_LIMIT = FileManager.getConfig().getInt("factions.alliances.ally-limit");
        ONLY_WARN_WHEN_ALLY_VISABLE = FileManager.getConfig().getBoolean("factions.alliances.only-warn-when-visable");

        SUBCLAIM_GUI_NAME = ChatColor.translateAlternateColorCodes('&', FileManager.getConfig().getString("factions.subclaims.gui-title"));

        RENAME_COOLDOWN = FileManager.getConfig().getInt("timers.rename-cooldown");
        DISBAND_COOLDOWN = FileManager.getConfig().getInt("timers.disband-cooldown");
        MAP_COOLDOWN = FileManager.getConfig().getInt("timers.map-cooldown");

        STARTING_BALANCE = FileManager.getConfig().getDouble("economy.starting-balance");

        Logger.log("Loaded configuration from config.yml");
    }

}
