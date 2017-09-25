package gg.revival.factions.locations;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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
                        entering.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.YELLOW + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        entering.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "SafeZone" + ChatColor.GOLD + ")");
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
                        leaving.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.YELLOW + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        leaving.append(ChatColor.YELLOW + claimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "SafeZone" + ChatColor.GOLD + ")");
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
                        leaving.append(ChatColor.YELLOW + serverOldClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.YELLOW + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        leaving.append(ChatColor.YELLOW + serverOldClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "SafeZone" + ChatColor.GOLD + ")");
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
                        entering.append(ChatColor.YELLOW + serverNewClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.YELLOW + "Road" + ChatColor.GOLD + ")");
                        break;
                    case SAFEZONE:
                        entering.append(ChatColor.YELLOW + serverNewClaimFaction.getDisplayName() + ChatColor.RESET + " " + ChatColor.GOLD + "(" + ChatColor.GREEN + "SafeZone" + ChatColor.GOLD + ")");
                        break;
                }
            }
        }

        if (entering != null && entering.length() > 0 && leaving != null && leaving.length() > 0) {
            mcPlayer.sendMessage(Messages.leavingClaim(leaving.toString()));
            mcPlayer.sendMessage(Messages.enteringClaim(entering.toString()));
        }
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

    public static boolean checkForInvalidLocation(Player mcPlayer) {
        FPlayer player = PlayerManager.getPlayer(mcPlayer.getUniqueId());

        if(player == null) return false;

        Location storedLocation = player.getLocation().getLastLocation();
        Location currentLocation = mcPlayer.getLocation();

        Claim claimAtCurrentLocation = ClaimManager.getClaimAt(currentLocation, true);

        if(claimAtCurrentLocation == null) return false;

        if(claimAtCurrentLocation.getClaimOwner() instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction)claimAtCurrentLocation.getClaimOwner();

            if(player.isBeingTimed(TimerType.PVPPROT)) {
                Vector knockback = storedLocation.toVector().subtract(mcPlayer.getLocation().toVector()).multiply(0.2);
                mcPlayer.getLocation().setDirection(knockback);
                mcPlayer.setVelocity(knockback);
                mcPlayer.teleport(storedLocation);
                mcPlayer.sendMessage(ChatColor.RED + "You are not allowed to enter this claim while you have PvP protection");
                return true;
            }

            if(player.isBeingTimed(TimerType.PROGRESSION) && !playerFaction.getRoster(true).contains(mcPlayer.getUniqueId())) {
                Vector knockback = storedLocation.toVector().subtract(mcPlayer.getLocation().toVector()).multiply(0.2);
                mcPlayer.getLocation().setDirection(knockback);
                mcPlayer.setVelocity(knockback);
                mcPlayer.teleport(storedLocation);
                mcPlayer.sendMessage(ChatColor.RED + "You are not allowed to enter this claim while you have unfinished progression");
                return true;
            }
        }

        if(claimAtCurrentLocation.getClaimOwner() instanceof ServerFaction) {
            ServerFaction serverFaction = (ServerFaction)claimAtCurrentLocation.getClaimOwner();

            if(serverFaction.getType().equals(ServerClaimType.EVENT) && player.isBeingTimed(TimerType.PROGRESSION)) {
                Vector knockback = storedLocation.toVector().subtract(mcPlayer.getLocation().toVector()).multiply(0.2);
                mcPlayer.getLocation().setDirection(knockback);
                mcPlayer.setVelocity(knockback);
                mcPlayer.teleport(storedLocation);
                mcPlayer.sendMessage(ChatColor.RED + "You are not allowed to enter this claim while you have unfinished progression");
                return true;
            }

            if(serverFaction.getType().equals(ServerClaimType.EVENT) && player.isBeingTimed(TimerType.PVPPROT)) {
                Vector knockback = storedLocation.toVector().subtract(mcPlayer.getLocation().toVector()).multiply(0.2);
                mcPlayer.getLocation().setDirection(knockback);
                mcPlayer.setVelocity(knockback);
                mcPlayer.teleport(storedLocation);
                mcPlayer.sendMessage(ChatColor.RED + "You are not allowed to enter this claim while you have PvP protection");
                return true;
            }

            if(serverFaction.getType().equals(ServerClaimType.SAFEZONE) && player.isBeingTimed(TimerType.TAG)) {
                Vector knockback = storedLocation.toVector().subtract(mcPlayer.getLocation().toVector()).multiply(0.2);
                mcPlayer.getLocation().setDirection(knockback);
                mcPlayer.setVelocity(knockback);
                mcPlayer.teleport(storedLocation);
                mcPlayer.sendMessage(ChatColor.RED + "You are not allowed to enter this claim while you are combat-tagged");
                return true;
            }
        }

        return false;
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

        if (found != null && found.getClaimOwner() instanceof ServerFaction && ((ServerFaction)found.getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE)) {
            if (player.isBeingTimed(TimerType.PVPPROT) && !player.getTimer(TimerType.PVPPROT).isPaused()) {
                player.getTimer(TimerType.PVPPROT).setPaused(true);
                player.getTimer(TimerType.PVPPROT).setPauseDiff(player.getTimer(TimerType.PVPPROT).getExpire() - System.currentTimeMillis());
            }

            if (player.isBeingTimed(TimerType.PROGRESSION) && !player.getTimer(TimerType.PROGRESSION).isPaused()) {
                player.getTimer(TimerType.PROGRESSION).setPaused(true);
                player.getTimer(TimerType.PROGRESSION).setPauseDiff(player.getTimer(TimerType.PROGRESSION).getExpire() - System.currentTimeMillis());
            }
        } else if (player.isBeingTimed(TimerType.PVPPROT) || player.isBeingTimed(TimerType.PROGRESSION)) {
            if (player.isBeingTimed(TimerType.PVPPROT) && player.getTimer(TimerType.PVPPROT).isPaused()) {
                player.getTimer(TimerType.PVPPROT).setPaused(false);
            }

            if (player.isBeingTimed(TimerType.PROGRESSION) && player.getTimer(TimerType.PROGRESSION).isPaused()) {
                player.getTimer(TimerType.PROGRESSION).setPaused(false);
            }
        }

        if(!checkForInvalidLocation(mcPlayer)) {
            player.getLocation().setLastLocation(currentLocation);
        }

        PlayerManager.updatePlayer(player);
    }

}
