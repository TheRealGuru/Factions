package gg.revival.factions.listeners.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;
import java.util.UUID;

public class ClaimEventsListener implements Listener {

    /**
     * This is used to prevent block-glitching & mounting entities to get inside claims
     * @param event
     */
    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        Entity mounted = event.getEntity();
        Entity mounter = event.getMount();

        if(!(mounter instanceof Player))
            return;

        Player player = (Player)mounter;

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(!claims.inside(mounted.getLocation(), true)) continue;

            if(claims.getClaimOwner() instanceof PlayerFaction) {
                PlayerFaction faction = (PlayerFaction)claims.getClaimOwner();

                if(!faction.getRoster(false).contains(player.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    /**
     * For whatever stupid reason, placing water/lava buckets can not be cancelled in the PlayerInteractEvent
     * @param event
     */
    @EventHandler
    public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(!claims.inside(block.getLocation(), false)) continue;

            if(claims.getClaimOwner() instanceof ServerFaction && !player.hasPermission(Permissions.ADMIN)) {
                player.sendMessage(Messages.landClaimedBy(ChatColor.YELLOW + claims.getClaimOwner().getDisplayName()));
                event.setCancelled(true);
                return;
            }

            if(claims.getClaimOwner() instanceof PlayerFaction) {
                PlayerFaction faction = (PlayerFaction)claims.getClaimOwner();

                if(!faction.getRoster(false).contains(player.getUniqueId()) && !faction.isRaidable() && !player.hasPermission(Permissions.ADMIN)) {
                    player.sendMessage(Messages.landClaimedBy(ChatColor.RED + faction.getDisplayName()));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if(ToolBox.isNonBuildableWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isNetherWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.NETHER_WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isEnd(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.END_NAME));
            event.setCancelled(true);
            return;
        }
    }

    /**
     * This event is used to stop Water/Lava flow in to other claims.
     * It also prevents things such as cobblestone generators entering the claim
     * @param event
     */
    @EventHandler
    public void onBlockChange(BlockFromToEvent event) {
        Block from = event.getBlock();
        Block to = event.getToBlock();

        if(from == null || to == null)
            return;

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(!claims.inside(to.getLocation(), false)) continue;

            if(!claims.inside(from.getLocation(), false)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(claims.inside(block.getLocation(), false)) {
                if(claims.getClaimOwner() instanceof PlayerFaction) {
                    PlayerFaction faction = (PlayerFaction)claims.getClaimOwner();
                    List<UUID> roster = faction.getRoster(false);

                    if(!roster.contains(player.getUniqueId()) && !faction.isRaidable() && !player.hasPermission(Permissions.ADMIN)) {
                        player.sendMessage(Messages.landClaimedBy(ChatColor.RED + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }

                else {
                    ServerFaction faction = (ServerFaction)claims.getClaimOwner();

                    if(!player.hasPermission(Permissions.ADMIN)) {
                        player.sendMessage(Messages.landClaimedBy(ChatColor.YELLOW + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if(claims.nearby(block.getLocation(), Configuration.CLAIM_BUFFER)) {
                if(claims.getClaimOwner() instanceof ServerFaction) {
                    ServerFaction faction = (ServerFaction)claims.getClaimOwner();

                    if(!faction.getType().equals(ServerClaimType.ROAD)) {
                        player.sendMessage(Messages.nearbyLandClaimedBy(ChatColor.YELLOW + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if(ToolBox.isNonBuildableWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isNetherWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.NETHER_WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isEnd(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.END_NAME));
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(claims.inside(block.getLocation(), false)) {
                if(claims.getClaimOwner() instanceof PlayerFaction) {
                    PlayerFaction faction = (PlayerFaction)claims.getClaimOwner();
                    List<UUID> roster = faction.getRoster(false);

                    if(!roster.contains(player.getUniqueId()) && !faction.isRaidable() && !player.hasPermission(Permissions.ADMIN)) {
                        player.sendMessage(Messages.landClaimedBy(ChatColor.RED + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }

                else {
                    ServerFaction faction = (ServerFaction)claims.getClaimOwner();

                    if(!player.hasPermission(Permissions.ADMIN)) {
                        player.sendMessage(Messages.landClaimedBy(ChatColor.YELLOW + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if(claims.nearby(block.getLocation(), Configuration.CLAIM_BUFFER)) {
                if(claims.getClaimOwner() instanceof ServerFaction) {
                    ServerFaction faction = (ServerFaction)claims.getClaimOwner();

                    if(!faction.getType().equals(ServerClaimType.ROAD)) {
                        player.sendMessage(Messages.nearbyLandClaimedBy(ChatColor.YELLOW + faction.getDisplayName()));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if(ToolBox.isNonBuildableWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isNetherWarzone(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.NETHER_WARZONE_NAME));
            event.setCancelled(true);
            return;
        }

        if(ToolBox.isEnd(block.getLocation()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.landClaimedBy(Configuration.END_NAME));
            event.setCancelled(true);
            return;
        }
    }
}
