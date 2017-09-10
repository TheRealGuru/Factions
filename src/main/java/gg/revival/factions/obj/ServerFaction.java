package gg.revival.factions.obj;

import gg.revival.factions.claims.ServerClaimType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ServerFaction extends Faction {

    @Getter @Setter ServerClaimType type;

    public ServerFaction(UUID uuid, String displayName, ServerClaimType type) {
        super(uuid, displayName);

        this.type = type;
    }

}
