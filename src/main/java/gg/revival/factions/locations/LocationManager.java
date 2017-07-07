package gg.revival.factions.locations;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LocationManager {

    public static FLocation getLocation(UUID uuid) {
        if (!PlayerManager.isLoaded(uuid)) {
            return null;
        }

        FPlayer player = PlayerManager.getPlayer(uuid);

        return player.getLocation();
    }

    public static void performClaimChange(Player mcPlayer, Claim oldClaim, Claim newClaim) {
        if (!PlayerManager.isLoaded(mcPlayer.getUniqueId())) return;

        FPlayer player = PlayerManager.getPlayer(mcPlayer.getUniqueId());

        player.getLocation().setLastSeen(oldClaim);
        player.getLocation().setCurrentClaim(newClaim);

        StringBuilder leaving = new StringBuilder();
        StringBuilder entering = new StringBuilder();

        if (oldClaim == null && newClaim != null) {

            if (ToolBox.isOverworld(mcPlayer.getLocation())) {

                if (ToolBox.isWarzone(mcPlayer.getLocation())) {
                    leaving.append(Configuration.WARZONE_NAME);
                } else {
                    leaving.append(Configuration.WILDERNESS_NAME);
                }
            } else if (ToolBox.isNether(mcPlayer.getLocation())) {

                if (ToolBox.isNetherWarzone(mcPlayer.getLocation())) {
                    leaving.append(Configuration.NETHER_WARZONE_NAME);
                } else {
                    leaving.append(Configuration.NETHER_NAME);
                }
            } else if (ToolBox.isEnd(mcPlayer.getLocation())) {
                leaving.append(Configuration.END_NAME);
            }

            if (newClaim.getClaimOwner() instanceof PlayerFaction) {
                PlayerFaction claimFaction = (PlayerFaction) newClaim.getClaimOwner();
                PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(mcPlayer.getUniqueId());

                if (playerFaction != null && playerFaction.getFactionID().equals(claimFaction.getFactionID())) {
                    entering.append(ChatColor.DARK_GREEN + claimFaction.getDisplayName());
                } else {
                    entering.append(ChatColor.RED + claimFaction.getDisplayName());
                }
            } else {
                ServerFaction claimFaction = (ServerFaction) newClaim.getClaimOwner();

                switch (claimFaction.getType()) {
                    case EVENT:
                        entering.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Event" + ChatColor.GOLD + ")");
                        break;
                    case ROAD:
                        entering.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        entering.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GOLD + ")");
                        break;
                }
            }
        } else if (oldClaim != null && newClaim == null) {

            if (ToolBox.isOverworld(mcPlayer.getLocation())) {

                if (ToolBox.isWarzone(mcPlayer.getLocation())) {
                    entering.append(Configuration.WARZONE_NAME);
                } else {
                    entering.append(Configuration.WILDERNESS_NAME);
                }
            } else if (ToolBox.isNether(mcPlayer.getLocation())) {

                if (ToolBox.isNetherWarzone(mcPlayer.getLocation())) {
                    entering.append(Configuration.NETHER_WARZONE_NAME);
                } else {
                    entering.append(Configuration.NETHER_NAME);
                }
            } else if (ToolBox.isEnd(mcPlayer.getLocation())) {
                entering.append(Configuration.END_NAME);
            }

            if (oldClaim.getClaimOwner() instanceof PlayerFaction) {
                PlayerFaction claimFaction = (PlayerFaction) oldClaim.getClaimOwner();
                PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(mcPlayer.getUniqueId());

                if (playerFaction != null && playerFaction.getFactionID().equals(claimFaction.getFactionID())) {
                    leaving.append(ChatColor.DARK_GREEN + claimFaction.getDisplayName());
                } else {
                    leaving.append(ChatColor.RED + claimFaction.getDisplayName());
                }
            } else {
                ServerFaction claimFaction = (ServerFaction) oldClaim.getClaimOwner();

                switch (claimFaction.getType()) {
                    case EVENT:
                        leaving.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Event" + ChatColor.GOLD + ")");
                        break;
                    case ROAD:
                        leaving.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        leaving.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GOLD + ")");
                        break;
                }
            }
        } else if (oldClaim != null && newClaim != null && !oldClaim.getClaimOwner().getFactionID().equals(newClaim.getClaimOwner().getFactionID())) {

            PlayerFaction playerFaction = (PlayerFaction) FactionManager.getFactionByPlayer(mcPlayer.getUniqueId());
            Faction oldClaimFaction = oldClaim.getClaimOwner();
            Faction newClaimFaction = newClaim.getClaimOwner();

            if (oldClaimFaction.getFactionID().equals(newClaimFaction.getFactionID())) {
                return;
            }

            if (oldClaimFaction instanceof PlayerFaction) {
                PlayerFaction playerOldClaimFaction = (PlayerFaction) oldClaimFaction;

                if (playerFaction != null && playerFaction.getFactionID().equals(playerOldClaimFaction.getFactionID())) {
                    leaving.append(ChatColor.DARK_GREEN + playerOldClaimFaction.getDisplayName());
                } else {
                    leaving.append(ChatColor.RED + playerOldClaimFaction.getDisplayName());
                }
            } else {
                ServerFaction serverOldClaimFaction = (ServerFaction) oldClaimFaction;

                switch (serverOldClaimFaction.getType()) {
                    case EVENT:
                        leaving.append(ChatColor.YELLOW + serverOldClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Event" + ChatColor.GOLD + ")");
                        break;
                    case ROAD:
                        leaving.append(ChatColor.YELLOW + serverOldClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        leaving.append(ChatColor.YELLOW + serverOldClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GOLD + ")");
                        break;
                }
            }

            if (newClaimFaction instanceof PlayerFaction) {
                PlayerFaction playerNewClaimFaction = (PlayerFaction) newClaimFaction;

                if (playerFaction != null && playerFaction.getFactionID().equals(playerNewClaimFaction.getFactionID())) {
                    entering.append(ChatColor.DARK_GREEN + playerNewClaimFaction.getDisplayName());
                } else {
                    entering.append(ChatColor.RED + playerNewClaimFaction.getDisplayName());
                }
            } else {
                ServerFaction serverNewClaimFaction = (ServerFaction) newClaimFaction;

                switch (serverNewClaimFaction.getType()) {
                    case EVENT:
                        entering.append(ChatColor.YELLOW + serverNewClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Event" + ChatColor.GOLD + ")");
                        break;
                    case ROAD:
                        entering.append(ChatColor.YELLOW + serverNewClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.DARK_AQUA + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        entering.append(ChatColor.YELLOW + serverNewClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "Safezone" + ChatColor.GOLD + ")");
                        break;
                }
            }
        }

        mcPlayer.sendMessage(Messages.enteringClaim(entering.toString()));
        mcPlayer.sendMessage(Messages.leavingClaim(leaving.toString()));
    }

    public static void performZoneChange(Player mcPlayer, ToolBox.WorldLocations oldLoc, ToolBox.WorldLocations newLoc) {
        StringBuilder leaving = new StringBuilder();
        StringBuilder entering = new StringBuilder();

        switch (oldLoc) {
            case WILDERNESS:
                leaving.append(Configuration.WILDERNESS_NAME);
                break;
            case WARZONE:
                leaving.append(Configuration.WARZONE_NAME);
                break;
            case NETHER:
                leaving.append(Configuration.NETHER_NAME);
                break;
            case NETHER_WARZONE:
                leaving.append(Configuration.NETHER_WARZONE_NAME);
                break;
            case END:
                leaving.append(Configuration.END_NAME);
                break;
        }

        switch (newLoc) {
            case WILDERNESS:
                entering.append(Configuration.WILDERNESS_NAME);
                break;
            case WARZONE:
                entering.append(Configuration.WARZONE_NAME);
                break;
            case NETHER:
                entering.append(Configuration.NETHER_NAME);
                break;
            case NETHER_WARZONE:
                entering.append(Configuration.NETHER_WARZONE_NAME);
                break;
            case END:
                entering.append(Configuration.END_NAME);
                break;
        }

        mcPlayer.sendMessage(Messages.enteringClaim(entering.toString()));
        mcPlayer.sendMessage(Messages.leavingClaim(leaving.toString()));
    }

    public static void updateLocation(Player mcPlayer) {
        if (!PlayerManager.isLoaded(mcPlayer.getUniqueId()))
            return;

        FPlayer player = PlayerManager.getPlayer(mcPlayer.getUniqueId());
        FLocation location = player.getLocation();

        Claim expected = location.getCurrentClaim();
        Claim found = ClaimManager.getClaimAt(mcPlayer.getLocation(), true);

        Location oldLocation = null;

        if (player.getLocation().getLastLocation() != null) {
            oldLocation = player.getLocation().getLastLocation();
        }

        Location currentLocation = mcPlayer.getLocation();

        if (expected == null && found != null) {
            performClaimChange(mcPlayer, expected, found);
            return;
        }

        if (expected != null && found == null) {
            performClaimChange(mcPlayer, expected, found);
            return;
        }

        if (expected != null && found != null && !expected.getClaimID().equals(found.getClaimID())) {
            performClaimChange(mcPlayer, expected, found);
            return;
        }

        if (found == null && oldLocation != null) {
            ToolBox.WorldLocations oldLocEnum = ToolBox.getLocationEnum(oldLocation);
            ToolBox.WorldLocations currentLocEnum = ToolBox.getLocationEnum(currentLocation);

            if (oldLocEnum != currentLocEnum) {
                performZoneChange(mcPlayer, oldLocEnum, currentLocEnum);
            }
        }

        player.getLocation().setLastLocation(currentLocation);

        PlayerManager.updatePlayer(player);
    }

}
