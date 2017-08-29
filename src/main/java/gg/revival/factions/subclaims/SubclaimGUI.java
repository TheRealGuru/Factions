package gg.revival.factions.subclaims;

import gg.revival.factions.FP;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.NameFetcher;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubclaimGUI {

    @Getter UUID editor;
    @Getter Subclaim subclaim;

    public SubclaimGUI(UUID editor, Subclaim subclaim) {
        this.editor = editor;
        this.subclaim = subclaim;
    }

    public void open() {
        Inventory gui = Bukkit.createInventory(null, 54, Configuration.SUBCLAIM_GUI_NAME);
        PlayerFaction holder = subclaim.getSubclaimHolder();

        new BukkitRunnable() {
            public void run() {
                try {
                    Map<UUID, String> officerNames = new NameFetcher(holder.getOfficers()).call();
                    Map<UUID, String> memberNames = new NameFetcher(holder.getMembers()).call();

                    for (UUID officers : officerNames.keySet()) {
                        String name = officerNames.get(officers);

                        new BukkitRunnable() {
                            public void run() {
                                ItemStack head = new ItemStack(Material.SKULL, 1, (short) 3);
                                ItemMeta meta = head.getItemMeta();
                                List<String> lore = new ArrayList<String>();

                                if (subclaim.getPlayerAccess().contains(officers)) {
                                    meta.setDisplayName(ChatColor.GREEN + name);
                                    lore.add(ChatColor.GOLD + "Access" + ChatColor.YELLOW + ": " + ChatColor.GREEN + "Yes");
                                } else {
                                    meta.setDisplayName(ChatColor.RED + name);
                                    lore.add(ChatColor.GOLD + "Access" + ChatColor.YELLOW + ": " + ChatColor.RED + "No");
                                }

                                meta.setLore(lore);
                                head.setItemMeta(meta);
                                gui.addItem(head);
                            }
                        }.runTask(FP.getInstance());
                    }

                    for (UUID members : memberNames.keySet()) {
                        String name = memberNames.get(members);

                        new BukkitRunnable() {
                            public void run() {
                                ItemStack head = new ItemStack(Material.SKULL, 1, (short) 3);
                                ItemMeta meta = head.getItemMeta();
                                List<String> lore = new ArrayList<String>();

                                if (subclaim.getPlayerAccess().contains(members)) {
                                    meta.setDisplayName(ChatColor.GREEN + name);
                                    lore.add(ChatColor.GOLD + "Access" + ChatColor.YELLOW + ": " + ChatColor.GREEN + "Yes");
                                } else {
                                    meta.setDisplayName(ChatColor.RED + name);
                                    lore.add(ChatColor.GOLD + "Access" + ChatColor.YELLOW + ": " + ChatColor.RED + "No");
                                }

                                meta.setLore(lore);
                                head.setItemMeta(meta);
                                gui.addItem(head);
                            }
                        }.runTask(FP.getInstance());
                    }

                    new BukkitRunnable() {
                        public void run() {
                            ItemStack officerItem = null;
                            ItemStack delete = new ItemStack(Material.ANVIL);

                            ItemMeta officerMeta = null;
                            ItemMeta deleteMeta = delete.getItemMeta();

                            if (subclaim.isOfficerAccess()) {
                                officerItem = new ItemStack(Material.EMERALD_BLOCK);
                                officerMeta = officerItem.getItemMeta();

                                officerMeta.setDisplayName(ChatColor.GOLD + "Officer Access" + ChatColor.YELLOW + ": " + ChatColor.GREEN + "Yes");
                            } else {
                                officerItem = new ItemStack(Material.REDSTONE_BLOCK);
                                officerMeta = officerItem.getItemMeta();

                                officerMeta.setDisplayName(ChatColor.GOLD + "Officer Access" + ChatColor.YELLOW + ": " + ChatColor.RED + "No");
                            }

                            deleteMeta.setDisplayName(ChatColor.DARK_RED + "Delete Subclaim");

                            officerItem.setItemMeta(officerMeta);
                            delete.setItemMeta(deleteMeta);

                            gui.setItem(53, officerItem);
                            gui.setItem(45, delete);

                            if (Bukkit.getPlayer(editor) != null && Bukkit.getPlayer(editor).isOnline()) {
                                Bukkit.getPlayer(editor).openInventory(gui);
                            }
                        }
                    }.runTask(FP.getInstance());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
