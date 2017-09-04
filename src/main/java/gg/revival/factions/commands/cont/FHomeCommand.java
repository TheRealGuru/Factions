package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.ServerClaimType;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import gg.revival.factions.tasks.HomeTask;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FHomeCommand extends FCommand {

    public FHomeCommand() {
        super(
                "home",
                Arrays.asList("base", "h"),
                "/f home",
                "Warp to your faction home",
                null,
                1,
                1,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if(!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player)sender;
        FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());
        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());
        Location homeLocation = faction.getHomeLocation();

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if(faction == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        if(homeLocation == null) {
            player.sendMessage(Messages.homeNotSet());
            return;
        }

        if(facPlayer.isBeingTimed(TimerType.HOME))
        {
            int inSeconds = (int)((PlayerManager.getPlayer(player.getUniqueId()).getTimer(TimerType.HOME).getExpire() - System.currentTimeMillis()) / 1000L);
            player.sendMessage(Messages.homeWarpStarted(inSeconds));
            return;
        }

        // TODO: Check if the player is combat tagged and prevent home if so

        if(homeLocation.getBlockY() >= Configuration.MAX_FAC_HOME_HEIGHT)
        {
            if(faction.getBalance() < Configuration.HOME_TOO_HIGH_PRICE)
            {
                player.sendMessage(Messages.cantAffordHomeTooHigh());
                return;
            }
        }

        int homeDur = Configuration.HOME_WARMUP;

        if(ToolBox.isNether(player.getLocation()) || ToolBox.isEnd(player.getLocation()))
        {
            homeDur = Configuration.HOME_WARMUP_OTHERWORLD;
        }

        Claim inside = ClaimManager.getClaimAt(player.getLocation(), true);

        if(inside != null)
        {
            if(inside.getClaimOwner() instanceof ServerFaction)
            {
                ServerFaction serverFaction = (ServerFaction)inside.getClaimOwner();

                if(serverFaction.getType().equals(ServerClaimType.SAFEZONE))
                {
                    HomeTask.sendHome(player.getUniqueId());
                    return;
                }

                if(serverFaction.getType().equals(ServerClaimType.EVENT))
                {
                    player.sendMessage(Messages.cantWarpHomeInsideClaim());
                    return;
                }
            }

            else
            {
                PlayerFaction insidePlayerFaction = (PlayerFaction)inside.getClaimOwner();

                if(insidePlayerFaction.getFactionID().equals(faction.getFactionID()))
                {
                    HomeTask.sendHome(player.getUniqueId());
                    return;
                }

                player.sendMessage(Messages.cantWarpHomeInsideClaim());
                return;
            }
        }

        PlayerManager.getPlayer(player.getUniqueId()).addTimer(TimerManager.createTimer(TimerType.HOME, homeDur));
        HomeTask.getStartingLocations().put(player.getUniqueId(), player.getLocation());
        player.sendMessage(Messages.homeWarpStarted(homeDur));
    }

}
