package gg.revival.factions.listeners;

import gg.revival.factions.FP;
import gg.revival.factions.listeners.cont.*;
import gg.revival.factions.tools.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerEventsListener(), FP.getInstance());
        pluginManager.registerEvents(new ClaimEventsListener(), FP.getInstance());
        pluginManager.registerEvents(new SubclaimEventsListener(), FP.getInstance());
        pluginManager.registerEvents(new ChatEventsListener(), FP.getInstance());
        pluginManager.registerEvents(new WorldEventsListener(), FP.getInstance());
        pluginManager.registerEvents(new TaskEventsListener(), FP.getInstance());

        Logger.log("Loaded Listeners");
    }

}
