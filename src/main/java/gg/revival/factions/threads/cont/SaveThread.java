package gg.revival.factions.threads.cont;

import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaveThread {

    public static void run() {
        List<FPlayer> toRemove = new ArrayList<FPlayer>();

        for (FPlayer players : PlayerManager.getActivePlayersSnapshot()) {
            UUID uuid = players.getUuid();

            if (Bukkit.getPlayer(uuid) == null) {
                toRemove.add(players);
            }
        }

        if (toRemove.isEmpty()) return;

        for (FPlayer removed : toRemove) {
            PlayerManager.saveProfile(removed, true);
        }
    }

}
