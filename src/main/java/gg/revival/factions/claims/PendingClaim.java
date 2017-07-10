package gg.revival.factions.claims;

import gg.revival.factions.obj.Faction;
import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PendingClaim {

    @Getter UUID claimer;
    @Getter @Setter Location posA, posB;
    @Getter Faction claimingFor;

    public PendingClaim(UUID claimer, Faction faction) {
        this.claimer = claimer;
        this.posA = null;
        this.posB = null;
        this.claimingFor = faction;
    }

    public int getX1() {
        if(posA == null || posB == null)
            return 0;

        return Math.min(posA.getBlockX(), posB.getBlockX());
    }

    public int getX2() {
        if(posA == null || posB == null)
            return 0;

        return Math.max(posA.getBlockX(), posB.getBlockX());
    }

    public int getY1() {
        if(posA == null || posB == null)
            return 0;

        return Math.min(posA.getBlockY(), posB.getBlockY());
    }

    public int getY2() {
        if(posA == null || posB == null)
            return 0;

        return Math.max(posA.getBlockY(), posB.getBlockY());
    }

    public int getZ1() {
        if(posA == null || posB == null)
            return 0;

        return Math.min(posA.getBlockZ(), posB.getBlockZ());
    }

    public int getZ2() {
        if(posA == null || posB == null)
            return 0;

        return Math.max(posA.getBlockZ(), posB.getBlockZ());
    }

    public List<Location> getBlockPeremeter(String worldName, int yLevel) {
        if(posA == null || posB == null)
            return null;

        List<Location> locations = new ArrayList<>();

        for(int x = getX1(); x <= getX2(); x++) {
            for(int z = getZ1(); z <= getZ1(); z++) {
                Location location = new Location(Bukkit.getWorld(worldName), x, yLevel, z);
                locations.add(location);
            }
        }

        return locations;
    }

    public double calculateCost() {
        int area = 0;

        int x1 = getX1();
        int x2 = getX2();
        int z1 = getZ1();
        int z2 = getZ2();

        for(int x = x1; x <= x2; x++) {
            for(int z = z1; z <= z2; z++) {
                area++;
            }
        }

        return area * 4;
    }

    public boolean isTooSmall() {
        double xAxis = Math.abs(getX1() - getX2());
        double zAxis = Math.abs(getZ1() - getZ2());

        if(xAxis < Configuration.MIN_CLAIM_SIZE || zAxis < Configuration.MIN_CLAIM_SIZE)
            return true;

        return false;
    }

}
