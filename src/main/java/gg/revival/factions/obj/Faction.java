package gg.revival.factions.obj;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.subclaims.Subclaim;
import gg.revival.factions.tools.ToolBox;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.UUID;

public class Faction {

    @Getter UUID factionID;
    @Getter @Setter String displayName;
    @Getter @Setter HashSet<Claim> claims;
    @Getter @Setter HashSet<Subclaim> subclaims;

    public Faction(UUID factionID, String displayName) {
        this.factionID = factionID;
        this.displayName = displayName;
        this.claims = new HashSet<>();
        this.subclaims = new HashSet<>();
    }

    /**
     * Mainly used to check if a block is touching the side of a claim
     *
     * @param location
     * @return
     */
    public boolean isTouching(Location location) {
        for (Claim claims : getClaims()) {
            if (!claims.getWorld().equals(location.getWorld())) continue;

            for (BlockFace directions : ToolBox.getFlatDirections()) {
                Block block = location.getBlock().getRelative(directions);
                Location locations = block.getLocation();

                if (claims.inside(locations, false))
                    return true;
            }
        }

        return false;
    }

}
