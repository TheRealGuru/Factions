package gg.revival.factions.threads.cont;

import gg.revival.factions.core.FC;
import gg.revival.factions.locations.LocationManager;
import gg.revival.factions.tasks.HomeTask;
import gg.revival.factions.tasks.StuckTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LocationThread {

    public static void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;

        for (Player player : Bukkit.getOnlinePlayers())
            LocationManager.updateLocation(player);

        HomeTask.checkLocations();
        StuckTask.checkLocations();
        FC.getFactionsCore().getBastion().getLogoutTask().checkLocations();
    }

}
