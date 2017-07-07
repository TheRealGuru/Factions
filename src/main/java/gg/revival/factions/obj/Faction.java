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

}
