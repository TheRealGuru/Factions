package gg.revival.factions.locations;

import gg.revival.factions.claims.Claim;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

public class FLocation {

    @Getter
    UUID uuid;
    @Getter
    @Setter
    Claim lastSeen, currentClaim;
    @Getter
    @Setter
    Location lastLocation;

    public FLocation(UUID uuid) {
        this.uuid = uuid;
    }

}
