package gg.revival.factions.listeners.cont;

import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.core.bastion.logout.tasks.LogoutTask;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.tasks.HomeTask;
import gg.revival.factions.tasks.StuckTask;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Messages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class TaskEventsListener implements Listener
{

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event)
    {
        if(event.isCancelled()) return;

        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player)event.getEntity();
        FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());

        if(facPlayer.isBeingTimed(TimerType.HOME))
        {
            facPlayer.removeTimer(TimerType.HOME);
            HomeTask.getStartingLocations().remove(player.getUniqueId());

            player.sendMessage(Messages.homeWarpCancelled());
        }

        if(facPlayer.isBeingTimed(TimerType.STUCK))
        {
            facPlayer.removeTimer(TimerType.STUCK);
            StuckTask.getStartingLocations().remove(player.getUniqueId());

            player.sendMessage(Messages.homeWarpCancelled());
        }

        if(facPlayer.isBeingTimed(TimerType.LOGOUT))
        {
            facPlayer.removeTimer(TimerType.LOGOUT);
            LogoutTask.getSafeloggers().remove(player.getUniqueId());
            LogoutTask.getStartingLocations().remove(player.getUniqueId());

            player.sendMessage(Messages.logoutCancelled());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(event.isCancelled()) return;

        if(event.getDamager() instanceof Player)
        {
            Player player = (Player)event.getDamager();
            FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());

            if(facPlayer.isBeingTimed(TimerType.HOME))
            {
                facPlayer.removeTimer(TimerType.HOME);
                HomeTask.getStartingLocations().remove(player.getUniqueId());

                player.sendMessage(Messages.homeWarpCancelled());
            }

            if(facPlayer.isBeingTimed(TimerType.STUCK))
            {
                facPlayer.removeTimer(TimerType.STUCK);
                StuckTask.getStartingLocations().remove(player.getUniqueId());

                player.sendMessage(Messages.homeWarpCancelled());
            }

            if(facPlayer.isBeingTimed(TimerType.LOGOUT))
            {
                facPlayer.removeTimer(TimerType.LOGOUT);
                LogoutTask.getSafeloggers().remove(player.getUniqueId());
                LogoutTask.getStartingLocations().remove(player.getUniqueId());

                player.sendMessage(Messages.logoutCancelled());
            }
        }

        if(event.getDamager() instanceof Projectile)
        {
            ProjectileSource src = ((Projectile) event.getDamager()).getShooter();

            if(src instanceof Player)
            {
                Player player = (Player)src;
                FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());

                if(facPlayer.isBeingTimed(TimerType.HOME))
                {
                    facPlayer.removeTimer(TimerType.HOME);
                    HomeTask.getStartingLocations().remove(player.getUniqueId());

                    player.sendMessage(Messages.homeWarpCancelled());
                }

                if(facPlayer.isBeingTimed(TimerType.STUCK))
                {
                    facPlayer.removeTimer(TimerType.STUCK);
                    StuckTask.getStartingLocations().remove(player.getUniqueId());

                    player.sendMessage(Messages.homeWarpCancelled());
                }

                if(facPlayer.isBeingTimed(TimerType.LOGOUT))
                {
                    facPlayer.removeTimer(TimerType.LOGOUT);
                    LogoutTask.getSafeloggers().remove(player.getUniqueId());
                    LogoutTask.getStartingLocations().remove(player.getUniqueId());

                    player.sendMessage(Messages.logoutCancelled());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());

        if(facPlayer == null) return;

        facPlayer.getTimers().clear();
    }

}
