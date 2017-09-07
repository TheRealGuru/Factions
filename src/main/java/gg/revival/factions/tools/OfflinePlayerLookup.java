package gg.revival.factions.tools;

import gg.revival.factions.FP;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class OfflinePlayerLookup
{

    public static void getManyOfflinePlayersByName(List<String> queries, ManyOfflinePlayerCallback callback)
    {
        List<String> found = new ArrayList<>();
        Map<UUID, String> result = new HashMap<>();

        for(String toLookup : queries)
        {
            if(Bukkit.getPlayer(toLookup) != null)
            {
                result.put(Bukkit.getPlayer(toLookup).getUniqueId(), Bukkit.getPlayer(toLookup).getName());
                found.add(toLookup);
                continue;
            }
        }

        if(found.size() == queries.size())
            callback.onQueryDone(result);

        new BukkitRunnable() {
            public void run() {
                for(String toLookup : queries)
                {
                    if(found.contains(toLookup)) continue;

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(toLookup);

                    if(offlinePlayer != null)
                    {
                        result.put(offlinePlayer.getUniqueId(), offlinePlayer.getName());
                        found.add(toLookup);
                    }
                }

                new BukkitRunnable() {
                    public void run() {
                        callback.onQueryDone(result);
                    }
                }.runTask(FP.getInstance());
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void getOfflinePlayerByName(String query, OfflinePlayerCallback callback)
    {
        if(Bukkit.getPlayer(query) != null)
        {
            Player player = Bukkit.getPlayer(query);
            UUID uuid = player.getUniqueId();
            String username = player.getName();

            callback.onQueryDone(uuid, username);

            return;
        }

        new BukkitRunnable() {
            public void run() {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(query);

                if(offlinePlayer != null)
                {
                    UUID uuid = offlinePlayer.getUniqueId();
                    String username = offlinePlayer.getName();

                    new BukkitRunnable() {
                        public void run() {
                            callback.onQueryDone(uuid, username);
                        }
                    }.runTask(FP.getInstance());
                }

                else
                {
                    new BukkitRunnable() {
                        public void run() {
                            callback.onQueryDone(null, null);
                        }
                    }.runTask(FP.getInstance());
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void getManyOfflinePlayersByUUID(List<UUID> queries, ManyOfflinePlayerCallback callback)
    {
        List<UUID> found = new ArrayList<>();
        Map<UUID, String> result = new HashMap<>();

        for(UUID toLookup : queries)
        {
            if(Bukkit.getPlayer(toLookup) != null)
            {
                result.put(Bukkit.getPlayer(toLookup).getUniqueId(), Bukkit.getPlayer(toLookup).getName());
                found.add(toLookup);
                continue;
            }
        }

        if(found.size() == queries.size())
            callback.onQueryDone(result);

        new BukkitRunnable() {
            public void run() {
                for(UUID toLookup : queries)
                {
                    if(found.contains(toLookup)) continue;

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(toLookup);

                    if(offlinePlayer != null)
                    {
                        result.put(offlinePlayer.getUniqueId(), offlinePlayer.getName());
                        found.add(toLookup);
                    }
                }

                new BukkitRunnable() {
                    public void run() {
                        callback.onQueryDone(result);
                    }
                }.runTask(FP.getInstance());
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

    public static void getOfflinePlayerByUUID(UUID query, OfflinePlayerCallback callback)
    {
        if(Bukkit.getPlayer(query) != null)
        {
            Player player = Bukkit.getPlayer(query);
            UUID uuid = player.getUniqueId();
            String username = player.getName();

            callback.onQueryDone(uuid, username);

            return;
        }

        new BukkitRunnable() {
            public void run() {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(query);

                if(offlinePlayer != null)
                {
                    UUID uuid = offlinePlayer.getUniqueId();
                    String username = offlinePlayer.getName();

                    new BukkitRunnable() {
                        public void run() {
                            callback.onQueryDone(uuid, username);
                        }
                    }.runTask(FP.getInstance());
                }

                else
                {
                    new BukkitRunnable() {
                        public void run() {
                            callback.onQueryDone(null, null);
                        }
                    }.runTask(FP.getInstance());
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}
