package gg.revival.factions.tools;

import org.bukkit.ChatColor;

public class Configuration {

    public static boolean DB_ENABLED = false;
    public static String DB_HOSTNAME = "localhost";
    public static int DB_PORT = 27017;
    public static boolean DB_USECREDS = false;
    public static String DB_USERNAME = "root";
    public static String DB_PASSWORD = "password";
    public static String DB_DATABASE = "factions";

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
    public static int STUCK_WARMUP = 60;

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

    public static boolean ALLIANCES_ENABLED = true;
    public static int ALLY_LIMIT = 1;
    public static boolean ONLY_WARN_WHEN_ALLY_VISABLE = true;

    public static String SUBCLAIM_GUI_NAME = ChatColor.BLACK + "Subclaim Editor";

    public static double PILLAR_HEIGHT = 30.0;

    public static int RENAME_COOLDOWN = 300;
    public static int DISBAND_COOLDOWN = 300;
    public static int MAP_COOLDOWN = 15;

    //TODO: Grab configuration from config.yml

}
