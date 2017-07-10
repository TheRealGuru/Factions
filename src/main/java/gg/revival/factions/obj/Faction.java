package gg.revival.factions.obj;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.subclaims.Subclaim;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.UUID;

public class Faction {

    @Getter
    UUID factionID;
    @Getter
    @Setter
    String displayName;
    @Getter
    @Setter
    HashSet<Claim> claims;
    @Getter
    @Setter
    HashSet<Subclaim> subclaims;
    @Getter
    @Setter
    Location home;

    public Faction(UUID factionID, String displayName) {
        this.factionID = factionID;
        this.displayName = displayName;
        this.claims = new HashSet<Claim>();
        this.subclaims = new HashSet<Subclaim>();
    }

    /**
     * Mainly used to check if a block is touching the side of a claim
     * @param location
     * @return
     */
    public boolean isTouching(Location location) {
        for(Claim claims : getClaims()) {
            if(!claims.getWorld().equals(location.getWorld())) continue;

            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(0.0D, 0.0D, 1.0D), false)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(1.0D, 0.0D, 0.0D), false)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(0.0D, 0.0D, -1.0D), true)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(-1.0D, 0.0D, 0.0D), true)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(-1.0D, 0.0D, 1.0D), false)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(-1.0D, 0.0D, -1.0D), false)) return true;
            if (claims.inside(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(1.0D, 0.0D, 1.0D), false)) return true;
        }

        return false;
    }

}
