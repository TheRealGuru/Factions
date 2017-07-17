package gg.revival.factions.listeners.cont;

import gg.revival.factions.FP;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.locations.FLocation;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.subclaims.SubclaimManager;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerEventsListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerManager.loadProfile(player.getUniqueId());

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) != null) {
            PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

            faction.sendMessage(Messages.memberOnline(player.getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ClaimManager.removeFromClaimEditor(player.getUniqueId());
        SubclaimManager.getSubclaimEditor().remove(player.getUniqueId());

        PlayerManager.saveProfile(PlayerManager.getPlayer(player.getUniqueId()), true);

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) != null) {
            PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

            faction.sendMessage(Messages.memberOffline(player.getName()));
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(!(entity instanceof Player))
            return;

        Player player = (Player)entity;
        FLocation location = PlayerManager.getPlayer(player.getUniqueId()).getLocation();

        if(location.getCurrentClaim() != null &&
                location.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                ((ServerFaction) location.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE)) {

            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled())
            return;

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        if(damaged instanceof Player && damager instanceof Player) {
            Player pDamaged = (Player)damaged;
            Player pDamager = (Player)damager;

            FLocation damagedLocation = PlayerManager.getPlayer(pDamaged.getUniqueId()).getLocation();
            FLocation damagerLocation = PlayerManager.getPlayer(pDamager.getUniqueId()).getLocation();

            if(damagedLocation.getCurrentClaim() != null &&
                    damagedLocation.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                    ((ServerFaction) damagedLocation.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE) &&
                    !pDamaged.getUniqueId().equals(pDamager.getUniqueId())) {

                pDamager.sendMessage(Messages.combatDisabledSafezone());
                event.setCancelled(true);

                return;
            }

            if(damagerLocation.getCurrentClaim() != null &&
                    damagerLocation.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                    ((ServerFaction) damagerLocation.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE) &&
                    !pDamaged.getUniqueId().equals(pDamager.getUniqueId())) {

                pDamager.sendMessage(Messages.combatDisabledSafezone());
                event.setCancelled(true);

                return;
            }

            if(FactionManager.isFactionMember(pDamaged.getUniqueId(), pDamager.getUniqueId()) &&
                    !pDamaged.getUniqueId().equals(pDamager.getUniqueId())) {

                pDamager.sendMessage(Messages.factionDamageDisabled());
                event.setCancelled(true);

                return;
            }

            if(FactionManager.isAllyMember(pDamaged.getUniqueId(), pDamager.getUniqueId())) {
                if(pDamaged.hasPotionEffect(PotionEffectType.INVISIBILITY) && Configuration.ONLY_WARN_WHEN_ALLY_VISABLE) {
                    return;
                }

                pDamager.sendMessage(Messages.allyDamage());

                return;
            }

            return;
        }

        if(damaged instanceof Player && damager instanceof Arrow) {
            Player pDamaged = (Player)damaged;
            FLocation damagedLocation = PlayerManager.getPlayer(pDamaged.getUniqueId()).getLocation();

            Arrow arrow = (Arrow)damager;
            ProjectileSource shooter = arrow.getShooter();

            if(damagedLocation.getCurrentClaim() != null &&
                    damagedLocation.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                    ((ServerFaction) damagedLocation.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE)) {

                if(shooter instanceof Player) {
                    Player pDamager = (Player)shooter;

                    pDamager.sendMessage(Messages.combatDisabledSafezone());
                }

                event.setCancelled(true);

                return;
            }

            if(!(shooter instanceof Player))
                return;

            Player pDamager = (Player)shooter;

            if(FactionManager.isFactionMember(pDamaged.getUniqueId(), pDamager.getUniqueId())) {
                pDamager.sendMessage(Messages.factionDamageDisabled());
                event.setCancelled(true);

                return;
            }

            if(FactionManager.isAllyMember(pDamaged.getUniqueId(), pDamager.getUniqueId())) {
                if(pDamaged.hasPotionEffect(PotionEffectType.INVISIBILITY) && Configuration.ONLY_WARN_WHEN_ALLY_VISABLE) {
                    return;
                }

                pDamager.sendMessage(Messages.allyDamage());

                return;
            }

            return;
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if(event.getAffectedEntities().isEmpty())
            return;

        if(event.getPotion().getShooter() instanceof Player) {
            Player player = (Player)event.getPotion().getShooter();
            FLocation location = PlayerManager.getPlayer(player.getUniqueId()).getLocation();

            if(location.getCurrentClaim() != null &&
                    location.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                    ((ServerFaction) location.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE)) {

                event.setCancelled(true);
                return;
            }
        }

        List<UUID> protectedEntities = new ArrayList<>();

        for(Entity entities : event.getAffectedEntities()) {
            if(!(entities instanceof Player)) continue;

            Player players = (Player)entities;
            FLocation location = PlayerManager.getPlayer(players.getUniqueId()).getLocation();

            if(location.getCurrentClaim() != null &&
                    location.getCurrentClaim().getClaimOwner() instanceof ServerFaction &&
                    ((ServerFaction) location.getCurrentClaim().getClaimOwner()).getType().equals(ServerClaimType.SAFEZONE)) {

                if(!protectedEntities.contains(players.getUniqueId())) {
                    protectedEntities.add(players.getUniqueId());
                }
            }
        }

        if(protectedEntities.isEmpty())
            return;

        new BukkitRunnable() {
            public void run() {

                for(UUID playerID : protectedEntities) {
                    Player players = Bukkit.getPlayer(playerID);

                    for(PotionEffect effects : event.getPotion().getEffects()) {
                        players.removePotionEffect(effects.getType());
                    }
                }

            }
        }.runTaskLater(FP.getInstance(), 1L);
    }
}
