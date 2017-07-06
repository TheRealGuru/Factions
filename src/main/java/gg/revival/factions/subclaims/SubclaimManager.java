package gg.revival.factions.subclaims;

import gg.revival.factions.FP;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SubclaimManager {

    @Getter @Setter private static HashSet<Subclaim> activeSubclaims = new HashSet<Subclaim>();
    @Getter @Setter private static HashMap<UUID, SubclaimGUI> subclaimEditor = new HashMap<UUID, SubclaimGUI>();

    public static Subclaim getSubclaimAt(Location location) {
        for (Subclaim subclaims : activeSubclaims) {
            if (!subclaims.getLocation().equals(location)) continue;

            return subclaims;
        }

        return null;
    }

    public static boolean isEditingSubclaim(UUID uuid) {
        if(getEditedSubclaim(uuid) != null) return true;

        return false;
    }

    public static SubclaimGUI getEditedSubclaim(UUID uuid) {
        if(subclaimEditor.containsKey(uuid)) {
            return subclaimEditor.get(uuid);
        }

        return null;
    }

    public static boolean subclaimExists(Subclaim subclaim) {
        for(Subclaim active : activeSubclaims) {
            if(active.getSubclaimID().equals(subclaim.getSubclaimID())) {
                return true;
            }
        }

        return false;
    }

    public static void addSubclaim(Subclaim subclaim) {
        if(subclaimExists(subclaim)) return;

        activeSubclaims.add(subclaim);
        subclaim.getSubclaimHolder().getSubclaims().add(subclaim);
    }

    public static void removeSubclaim(Subclaim subclaim) {
        if(!subclaimExists(subclaim)) return;

        List<Subclaim> serverCache = new ArrayList<Subclaim>();
        List<Subclaim> factionCache = new ArrayList<Subclaim>();

        for(Subclaim subclaims : activeSubclaims) {
            serverCache.add(subclaims);
        }

        for(Subclaim subclaims : subclaim.getSubclaimHolder().getSubclaims()) {
            factionCache.add(subclaims);
        }

        for(Subclaim subclaims : serverCache) {
            if(subclaims.getSubclaimID().equals(subclaim.getSubclaimID())) {
                activeSubclaims.remove(subclaims);
            }
        }

        for(Subclaim subclaims : factionCache) {
            if(subclaims.getSubclaimID().equals(subclaim.getSubclaimID())) {
                subclaim.getSubclaimHolder().getSubclaims().remove(subclaims);
            }
        }
    }

    public static void performUpdate(Player player, Subclaim subclaim) {
        new BukkitRunnable() {
            public void run() {
                if(player.getOpenInventory() != null) {
                    player.closeInventory();
                }

                removeSubclaim(subclaim);
                addSubclaim(subclaim);

                SubclaimGUI newGUI = new SubclaimGUI(player.getUniqueId(), subclaim);

                newGUI.open();
            }
        }.runTaskLater(FP.getInstance(), 1L);
    }

}
