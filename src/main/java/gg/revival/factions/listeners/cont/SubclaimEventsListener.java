package gg.revival.factions.listeners.cont;

import gg.revival.factions.FP;
import gg.revival.factions.claims.Claim;
import gg.revival.factions.subclaims.Subclaim;
import gg.revival.factions.subclaims.SubclaimManager;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SubclaimEventsListener implements Listener {

    private final List<BlockFace> flatDirections = Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().equals(Material.CHEST) && !event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))
            return;

        Block block = event.getClickedBlock();

        if (SubclaimManager.getSubclaimAt(block.getLocation()) != null) {
            Subclaim subclaim = SubclaimManager.getSubclaimAt(block.getLocation());

            if (subclaim.getSubclaimHolder().isRaidable()) return;

            for (Claim claims : subclaim.getSubclaimHolder().getClaims()) {
                if (!claims.inside(block.getLocation(), false)) continue;

                if (subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) return;

                else if (subclaim.getSubclaimHolder().getOfficers().contains(player.getUniqueId()) && subclaim.isOfficerAccess())
                    return;

                else if (subclaim.getPlayerAccess().contains(player.getUniqueId()) && subclaim.getSubclaimHolder().getRoster(false).contains(player.getUniqueId()))
                    return;

                else if (player.hasPermission(Permissions.ADMIN)) return;

                player.sendMessage(Messages.noSubclaimAccess());
                event.setCancelled(true);
                return;
            }
        }

        for (BlockFace directions : flatDirections) {
            Block relative = block.getRelative(directions);

            if (relative == null || SubclaimManager.getSubclaimAt(relative.getLocation()) == null) continue;

            Subclaim subclaim = SubclaimManager.getSubclaimAt(relative.getLocation());

            if (subclaim.getSubclaimHolder().isRaidable()) continue;

            for (Claim claims : subclaim.getSubclaimHolder().getClaims()) {
                if (!claims.inside(block.getLocation(), false)) continue;

                if (subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) return;

                else if (subclaim.getSubclaimHolder().getOfficers().contains(player.getUniqueId()) && subclaim.isOfficerAccess())
                    return;

                else if (subclaim.getPlayerAccess().contains(player.getUniqueId()) && subclaim.getSubclaimHolder().getRoster(false).contains(player.getUniqueId()))
                    return;

                else if (player.hasPermission(Permissions.ADMIN)) return;

                player.sendMessage(Messages.noSubclaimAccess());
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) return;

        if (SubclaimManager.getSubclaimAt(block.getLocation()) != null && !SubclaimManager.getSubclaimAt(block.getLocation()).getSubclaimHolder().isRaidable()) {
            Subclaim subclaim = SubclaimManager.getSubclaimAt(block.getLocation());

            if (player.hasPermission(Permissions.ADMIN)) {
                SubclaimManager.removeSubclaim(subclaim);

                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
                return;
            }

            if (subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) {
                SubclaimManager.removeSubclaim(subclaim);

                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
                return;
            }

            if (subclaim.getSubclaimHolder().getOfficers().contains(player.getUniqueId()) && subclaim.isOfficerAccess()) {
                SubclaimManager.removeSubclaim(subclaim);

                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
                return;
            }

            player.sendMessage(Messages.noSubclaimAccess());
            event.setCancelled(true);
            return;
        }

        for (BlockFace directions : flatDirections) {
            Block relative = block.getRelative(directions);

            if (relative == null || SubclaimManager.getSubclaimAt(relative.getLocation()) == null) continue;

            Subclaim subclaim = SubclaimManager.getSubclaimAt(relative.getLocation());

            if (player.hasPermission(Permissions.ADMIN)) {
                SubclaimManager.removeSubclaim(subclaim);

                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
                return;
            }

            if (subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) {
                SubclaimManager.removeSubclaim(subclaim);

                //TODO: Send subclaim deleted message to leader
                //TODO: Send subclaim deleted message to faction
                return;
            }

            if (subclaim.getSubclaimHolder().getOfficers().contains(player.getUniqueId()) && subclaim.isOfficerAccess()) {
                SubclaimManager.removeSubclaim(subclaim);

                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
                return;
            }

            player.sendMessage(Messages.noSubclaimAccess());
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) return;

        for (BlockFace directions : flatDirections) {
            Block relative = block.getRelative(directions);

            if (relative == null || !relative.getType().equals(Material.CHEST) || !relative.getType().equals(Material.TRAPPED_CHEST))
                continue;
            if (SubclaimManager.getSubclaimAt(relative.getLocation()) == null) continue;

            if (!player.hasPermission(Permissions.ADMIN)) {
                player.sendMessage(Messages.subclaimTooClose());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory() == null) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        if (inv.getName().equals(Configuration.SUBCLAIM_GUI_NAME) && SubclaimManager.isEditingSubclaim(player.getUniqueId())) {
            Subclaim subclaim = SubclaimManager.getEditedSubclaim(player.getUniqueId()).getSubclaim();
            ItemStack currentItem = event.getCurrentItem();

            if (currentItem == null) return;

            ItemMeta currentItemMeta = currentItem.getItemMeta();
            String title = currentItemMeta.getDisplayName();
            String stripped = ChatColor.stripColor(title);

            if (currentItem.getType().equals(Material.SKULL_ITEM)) {
                new BukkitRunnable() {
                    public void run() {
                        try {
                            UUID clickedUUID = UUIDFetcher.getUUIDOf(stripped);

                            new BukkitRunnable() {
                                public void run() {
                                    if (subclaim.getPlayerAccess().contains(clickedUUID)) {
                                        subclaim.getPlayerAccess().remove(clickedUUID);
                                    } else {
                                        subclaim.getPlayerAccess().add(clickedUUID);
                                    }

                                    SubclaimManager.performUpdate(player, subclaim);
                                }
                            }.runTask(FP.getInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskAsynchronously(FP.getInstance());
            }

            if (currentItem.getType().equals(Material.EMERALD_BLOCK)) {
                if (!subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) {
                    new BukkitRunnable() {
                        public void run() {
                            player.closeInventory();
                            player.sendMessage(Messages.leaderRequired());
                        }
                    }.runTaskLater(FP.getInstance(), 1L);

                    return;
                }

                subclaim.setOfficerAccess(false);

                SubclaimManager.performUpdate(player, subclaim);
            }

            if (currentItem.getType().equals(Material.REDSTONE_BLOCK)) {
                if (!subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) {
                    new BukkitRunnable() {
                        public void run() {
                            player.closeInventory();
                            player.sendMessage(Messages.leaderRequired());
                        }
                    }.runTaskLater(FP.getInstance(), 1L);

                    return;
                }

                subclaim.setOfficerAccess(true);

                SubclaimManager.performUpdate(player, subclaim);
            }

            if (currentItem.getType().equals(Material.FIRE)) {
                if (!subclaim.getSubclaimHolder().getLeader().equals(player.getUniqueId())) {
                    if ((subclaim.isOfficerAccess() && !subclaim.getSubclaimHolder().getOfficers().contains(player.getUniqueId())) || !subclaim.isOfficerAccess()) {
                        new BukkitRunnable() {
                            public void run() {
                                player.closeInventory();
                                player.sendMessage(Messages.leaderRequired());
                            }
                        }.runTaskLater(FP.getInstance(), 1L);
                    }

                    return;
                }

                SubclaimManager.removeSubclaim(subclaim);
                player.sendMessage(Messages.subclaimDeleted());
                subclaim.getSubclaimHolder().sendMessage(Messages.subclaimDeletedFaction());
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        InventoryHolder src = event.getSource().getHolder();
        InventoryHolder dest = event.getDestination().getHolder();

        if (src instanceof Chest || src instanceof Hopper) {
            Location location;

            if (src instanceof Chest) {
                location = ((Chest) src).getLocation();
            } else {
                location = ((Hopper) src).getLocation();
            }

            if (SubclaimManager.getSubclaimAt(location) != null) {
                event.setCancelled(true);
                return;
            }
        }

        if (dest instanceof Chest || dest instanceof Hopper) {
            Location location;

            if (src instanceof Chest) {
                location = ((Chest) dest).getLocation();
            } else {
                location = ((Hopper) dest).getLocation();
            }

            if (SubclaimManager.getSubclaimAt(location) != null) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
