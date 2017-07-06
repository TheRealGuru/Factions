package gg.revival.factions.threads.cont;

import gg.revival.factions.locations.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LocationThread {

    public static void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            LocationManager.updateLocation(player);
        }
    }

}
