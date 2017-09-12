package gg.revival.factions.subclaims;

import gg.revival.factions.obj.PlayerFaction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Subclaim {

    @Getter UUID subclaimID;
    @Getter Location location;
    @Getter PlayerFaction subclaimHolder;
    @Getter @Setter List<UUID> playerAccess = new ArrayList<UUID>();
    @Getter @Setter boolean officerAccess;

    public Subclaim(UUID subclaimID, PlayerFaction subclaimHolder, Location location, List<UUID> playerAccess, boolean officerAccess) {
        this.subclaimID = subclaimID;
        this.subclaimHolder = subclaimHolder;
        this.location = location;
        this.playerAccess = playerAccess;
        this.officerAccess = officerAccess;
    }

}
