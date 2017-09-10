package gg.revival.factions;

import gg.revival.factions.commands.CommandManager;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.listeners.ListenerManager;
import gg.revival.factions.threads.ThreadManager;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class FP extends JavaPlugin {

    @Getter
    private static FP instance;

    public void onEnable() {
        instance = this;

        FileManager.createFiles();

        Configuration.loadConfiguration();
        ListenerManager.loadListeners();
        CommandManager.loadCommands();
        FactionManager.loadFactions();
        ThreadManager.startThreads();
    }

    public void onDisable() {
        FactionManager.saveAllFactions(true, true);
        PlayerManager.saveAllProfiles(true, true);
    }
}
