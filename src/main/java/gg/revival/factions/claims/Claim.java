package gg.revival.factions.claims;

import gg.revival.factions.obj.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.UUID;

public class Claim {

    @Getter
    UUID claimID;

    @Getter
    @Setter
    double x1, x2, y1, y2, z1, z2, claimValue;
    @Getter
    @Setter
    String worldName;

    @Getter
    @Setter
    Faction claimOwner;

    public Claim(UUID claimID, Faction claimOwner, double x1, double x2, double y1, double y2, double z1, double z2, String worldName, int claimValue) {
        this.claimID = claimID;
        this.claimOwner = claimOwner;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.worldName = worldName;
        this.claimValue = claimValue;
    }

    public World getWorld() {
        return getCorner(1).getWorld();
    }

    public Location getCorner(int cornerID) {
        Location corner = null;

        switch (cornerID) {
            case 1:
                corner = new Location(Bukkit.getWorld(this.worldName), x1, 64.0, z1);
                break;
            case 2:
                corner = new Location(Bukkit.getWorld(this.worldName), x2, 64.0, z1);
                break;
            case 3:
                corner = new Location(Bukkit.getWorld(this.worldName), x2, 64.0, z2);
                break;
            case 4:
                corner = new Location(Bukkit.getWorld(this.worldName), x1, 64.0, z2);
                break;
        }

        return corner;
    }

    public boolean overlaps(double x1, double x2, double z1, double z2) {
        double[] vals = new double[2];

        double xMin = Math.min(this.x1, this.x2);
        double xMax = Math.max(this.x1, this.x2);
        double zMin = Math.min(this.z1, this.z2);
        double zMax = Math.max(this.z1, this.z2);

        vals[0] = x1;
        vals[1] = x2;

        Arrays.sort(vals);

        if (xMin > vals[1] || xMax < vals[0]) return false;

        vals[0] = zMin;
        vals[1] = zMax;

        Arrays.sort(vals);

        if (zMin > vals[1] || zMax < vals[0]) return false;

        return true;
    }

    public boolean inside(Location loc, boolean isEntity) {
        if (!loc.getWorld().equals(getWorld())) return false;

        double xMin = Math.min(this.x1, this.x2);
        double xMax = Math.max(this.x1, this.x2);
        double yMin = Math.min(this.y1, this.y2);
        double yMax = Math.max(this.y1, this.y2);
        double zMin = Math.min(this.z1, this.z2);
        double zMax = Math.max(this.z1, this.z2);

        if (isEntity) {
            ++xMax;
            ++zMax;
        }

        if (loc.getX() >= xMin && loc.getX() <= xMax && loc.getY() >= yMin && loc.getY() <= yMax && loc.getZ() >= zMin && loc.getZ() <= zMax)
            return true;

        return false;
    }

    public boolean nearby(Location loc, int dist) {
        if (!loc.getWorld().equals(getWorld())) return false;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(0, 0, dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(dist, 0, 0), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(0, 0, -dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(-dist, 0, dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(-dist, 0, dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(dist, 0, -dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(-dist, 0, -dist), false))
            return true;

        if (inside(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).add(dist, 0, dist), false))
            return true;

        return false;
    }
}
