package gg.revival.factions;

import gg.revival.driver.MongoAPI;
import gg.revival.factions.commands.CommandManager;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.db.DatabaseManager;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.listeners.ListenerManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.threads.ThreadManager;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FP extends JavaPlugin {

    @Getter
    private static FP instance;

    public void onEnable() {
        instance = this;

        FileManager.createFiles();

        DatabaseManager.setupConnection();
        ListenerManager.loadListeners();
        CommandManager.loadCommands();
        FactionManager.loadFactions();
        ThreadManager.startThreads();
    }

    public void onDisable() {
        if (Configuration.DB_ENABLED && MongoAPI.isConnected()) {

            for (Faction factions : FactionManager.getFactions()) {
                FactionManager.saveFaction(factions, true);
            }

            for (Player players : Bukkit.getOnlinePlayers()) {
                PlayerManager.saveProfile(PlayerManager.getPlayer(players.getUniqueId()), true);
            }

            MongoAPI.disconnect();
        }
    }

}
