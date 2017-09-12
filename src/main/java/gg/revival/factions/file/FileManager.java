package gg.revival.factions.file;

import gg.revival.factions.FP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    static File configFile;
    static File messageFile;
    static FileConfiguration configConfig;
    static FileConfiguration messageConfig;

    public static void createFiles() {
        try {
            if (!FP.getInstance().getDataFolder().exists()) {
                FP.getInstance().getDataFolder().mkdirs();
            }

            configFile = new File(FP.getInstance().getDataFolder(), "config.yml");
            messageFile = new File(FP.getInstance().getDataFolder(), "messages.yml");

            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                FP.getInstance().saveResource("config.yml", true);
            }

            if (!messageFile.exists()) {
                messageFile.getParentFile().mkdirs();
                FP.getInstance().saveResource("messages.yml", true);
            }

            configConfig = new YamlConfiguration();
            messageConfig = new YamlConfiguration();

            try {
                configConfig.load(configFile);
                messageConfig.load(messageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig() {
        return configConfig;
    }

    public static FileConfiguration getMessages() {
        return messageConfig;
    }

    public static void saveConfig() {
        try {
            configConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveMessages() {
        try {
            messageConfig.save(messageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadFiles() {
        try {
            if (!FP.getInstance().getDataFolder().exists()) {
                FP.getInstance().getDataFolder().mkdirs();
            }

            configFile = new File(FP.getInstance().getDataFolder(), "config.yml");
            messageFile = new File(FP.getInstance().getDataFolder(), "messages.yml");

            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                FP.getInstance().saveResource("config.yml", true);
            }

            if (!messageFile.exists()) {
                messageFile.getParentFile().mkdirs();
                FP.getInstance().saveResource("messages.yml", true);
            }

            configConfig = new YamlConfiguration();
            messageConfig = new YamlConfiguration();

            try {
                configConfig.load(configFile);
                messageConfig.load(messageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}