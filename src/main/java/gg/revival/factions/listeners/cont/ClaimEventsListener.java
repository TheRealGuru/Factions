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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClaimEventsListener implements Listener {

    private final List<Material> clickables = Arrays.asList(Material.ANVIL, Material.BEACON, Material.BED_BLOCK, Material.BED, Material.BREWING_STAND, Material.BURNING_FURNACE, Material.CAULDRON,
            Material.CHEST, Material.TRAPPED_CHEST, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.FENCE_GATE,
            Material.FURNACE, Material.HOPPER, Material.ITEM_FRAME, Material.JUKEBOX, Material.LEVER, Material.NOTE_BLOCK, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON, Material.TRAP_DOOR, Material.WOODEN_DOOR, Material.WORKBENCH, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.WOOD_DOOR, Material.STONE_PLATE,
            Material.WOOD_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR,
            Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE);

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if(
                item != null &&
                item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null &&
                item.getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) {

            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(
                event.getCurrentItem() != null &&
                event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null &&
                event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) {

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

        if(PlayerManager.getPlayer(player.getUniqueId()) == null || PlayerManager.getPlayer(player.getUniqueId()).getLocation() == null) return;

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

                    if(!faction.getType().equals(ServerClaimType.ROAD) && !player.hasPermission(Permissions.ADMIN)) {
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

                    if(!faction.getType().equals(ServerClaimType.ROAD) && !player.hasPermission(Permissions.ADMIN)) {
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
                if(clickables.contains(block.getType())) {
                    if(!block.getType().name().contains("PLATE"))
                    {
                        player.sendMessage(Messages.landClaimedBy(ChatColor.YELLOW + claims.getClaimOwner().getDisplayName()));
                    }

                    event.setCancelled(true);
                }
            }

            if(claims.getClaimOwner() instanceof PlayerFaction && !player.hasPermission(Permissions.ADMIN)) {
                if(!((PlayerFaction) claims.getClaimOwner()).getRoster(false).contains(player.getUniqueId())) {
                    if(clickables.contains(block.getType())) {
                        if(!block.getType().name().contains("PLATE"))
                        {
                            player.sendMessage(Messages.landClaimedBy(ChatColor.YELLOW + claims.getClaimOwner().getDisplayName()));
                        }

                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClaimAttempt(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Location clickedLocation = null;

        if(player.getItemInHand() == null) return;
        if(!player.getItemInHand().hasItemMeta() || player.getItemInHand().getItemMeta() == null || player.getItemInHand().getItemMeta().getDisplayName() == null) return;
        if(!player.getItemInHand().getItemMeta().getDisplayName().equals(ToolBox.getClaimingStick().getItemMeta().getDisplayName())) return;

        if(event.getClickedBlock() != null) {
            clickedLocation = event.getClickedBlock().getLocation();
        }

        ClaimManager.performClaimAction(action, player, clickedLocation);

        if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = new CopyOnWriteArrayList<>(event.blockList());

        for(Block block : blocks) {
            if(ClaimManager.getClaimAt(block.getLocation(), false) == null) continue;

            blocks.remove(block);
        }

        event.blockList().clear();
        event.blockList().addAll(blocks);
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        if(ClaimManager.getClaimAt(event.getBlock().getLocation(), false) == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (ClaimManager.getClaimAt(event.getBlock().getLocation(), false) == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(entity.getType().equals(EntityType.HORSE) ||
                entity.getType().equals(EntityType.IRON_GOLEM) ||
                entity.getType().equals(EntityType.ITEM_FRAME) ||
                entity.getType().equals(EntityType.VILLAGER) ||
                entity.getType().equals(EntityType.WOLF)) {

            Claim claim = ClaimManager.getClaimAt(entity.getLocation(), true);

            if(claim == null)
                return;

            if(claim.getClaimOwner() instanceof ServerFaction) {
                ServerFaction serverFaction = (ServerFaction)claim.getClaimOwner();

                if(serverFaction.getType().equals(ServerClaimType.SAFEZONE)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        if(!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL))
            return;

        Entity entity = event.getEntity();

        Claim claim = ClaimManager.getClaimAt(entity.getLocation(), true);

        if(claim == null)
            return;

        if(claim.getClaimOwner() instanceof ServerFaction) {
            ServerFaction serverFaction = (ServerFaction)claim.getClaimOwner();

            if(serverFaction.getType().equals(ServerClaimType.ROAD))
                return;

            event.setCancelled(true);
        }
    }
}
