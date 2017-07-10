package gg.revival.factions.pillars;

import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

public class PillarManager {

    @Getter
    public static HashSet<Pillar> activePillars = new HashSet<Pillar>();

    public static HashSet<Pillar> getActivePillars(UUID uuid) {
        HashSet<Pillar> results = new HashSet<Pillar>();

        for(Pillar pillars : activePillars) {
            if(!pillars.getDisplayedTo().equals(uuid)) continue;

            results.add(pillars);
        }

        return results;
    }

}
