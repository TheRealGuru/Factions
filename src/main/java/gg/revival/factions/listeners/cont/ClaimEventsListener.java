package gg.revival.factions.listeners.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.locations.FLocation;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClaimEventsListener implements Listener {

    private final List<Material> clickables = Arrays.asList(Material.ANVIL, Material.BEACON, Material.BED_BLOCK, Material.BREWING_STAND, Material.BURNING_FURNACE, Material.CAULDRON,
            Material.CHEST, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.FENCE_GATE,
            Material.FURNACE, Material.HOPPER, Material.ITEM_FRAME, Material.JUKEBOX, Material.LEVER, Material.NOTE_BLOCK, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON, Material.TRAP_DOOR, Material.WOODEN_DOOR, Material.WORKBENCH, Material.STONE_BUTTON, Material.WOOD_BUTTON);

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if(item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) {
            event.setCurrentItem(null);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtension(BlockPistonExtendEvent event) {
        Block piston = event.getBlock();

        if(event.getBlocks().isEmpty())
            return;

        for(Claim claims : ClaimManager.getActiveClaims()) {
            for(Block blocks : event.getBlocks()) {
                if(claims.inside(blocks.getLocation(), false) && !claims.inside(piston.getLocation(), false)) {
                    event.setCancelled(true);
                }

                if(claims.nearby(blocks.getLocation(), Configuration.CLAIM_BUFFER) && !claims.inside(piston.getLocation(), false)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        Block piston = event.getBlock();
        Location retractLocation = event.getRetractLocation();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(claims.inside(retractLocation, false) && !claims.inside(piston.getLocation(), false)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();

        if(!(target instanceof Player))
            return;

        Player player = (Player)target;
        FLocation location = PlayerManager.getPlayer(player.getUniqueId()).getLocation();

        if(location.getCurrentClaim() != null && location.getCurrentClaim().getClaimOwner() instanceof ServerFaction) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Location location = event.getLocation();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(!claims.inside(location, true)) continue;

            if(claims.getClaimOwner() instanceof ServerFaction) {
                event.setCancelled(true);
            }
        }
    }

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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if(!action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.PHYSICAL))
            return;

        Block block = event.getClickedBlock();

        for(Claim claims : ClaimManager.getActiveClaims()) {
            if(!claims.inside(block.getLocation(), false)) continue;

            if(claims.getClaimOwner() instanceof ServerFaction && !player.hasPermission(Permissions.ADMIN)) {
                event.setCancelled(true);

                if(clickables.contains(block.getType())) {
                    player.sendMessage(ChatColor.YELLOW + claims.getClaimOwner().getDisplayName());
                }
            }

            if(claims.getClaimOwner() instanceof PlayerFaction && !player.hasPermission(Permissions.ADMIN)) {
                if(!((PlayerFaction) claims.getClaimOwner()).getRoster(false).contains(player.getUniqueId())) {
                    event.setCancelled(true);

                    if(clickables.contains(block.getType())) {
                        player.sendMessage(ChatColor.RED + claims.getClaimOwner().getDisplayName());
                    }
                }
            }
        }
    }
}
