package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FAllyCommand extends FCommand {

    public FAllyCommand()
    {
        super(
                "ally",
                null,
                "/f ally <faction>",
                "Form an alliance with another faction",
                null,
                2,
                2,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[])
    {
        if(!(sender instanceof Player) && isPlayerOnly())
        {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player)sender;

        if(args.length < getMinArgs() || args.length > getMaxArgs())
        {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String namedFaction = args[1];

        Faction faction = FactionManager.getFactionByPlayer(player.getUniqueId());
        Faction allyFaction = FactionManager.getFactionByName(namedFaction);

        if(!Configuration.ALLIANCES_ENABLED)
        {
            player.sendMessage(Messages.alliesDisabled());
            return;
        }

        if(faction == null)
        {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction)faction;

        if(!playerFaction.getOfficers().contains(player.getUniqueId()) && !playerFaction.getLeader().equals(player.getUniqueId()))
        {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if(allyFaction == null || !(allyFaction instanceof PlayerFaction))
        {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerAllyFaction = (PlayerFaction)allyFaction;

        if(playerFaction.getAllies().size() >= Configuration.ALLY_LIMIT)
        {
            player.sendMessage(Messages.selfMaxAllies());
            return;
        }

        if(playerAllyFaction.getAllies().size() >= Configuration.ALLY_LIMIT)
        {
            player.sendMessage(Messages.otherMaxAllies());
            return;
        }

        if(playerAllyFaction.getPendingAllies().contains(playerFaction.getFactionID()))
        {
            playerFaction.getAllies().add(playerAllyFaction.getFactionID());
            playerFaction.getPendingAllies().remove(playerAllyFaction.getFactionID());
            playerAllyFaction.getAllies().add(playerFaction.getFactionID());
            playerAllyFaction.getPendingAllies().remove(playerFaction.getFactionID());

            Bukkit.broadcastMessage(Messages.allianceFormed(playerFaction.getDisplayName(), playerAllyFaction.getDisplayName()));
            return;
        }

        if(playerFaction.getAllies().contains(playerAllyFaction.getFactionID()))
        {
            player.sendMessage(Messages.allyRequestPending());
            return;
        }

        if(playerFaction.getTimer(TimerType.ALLY) != null)
        {
            int dur = (int)((playerFaction.getTimer(TimerType.ALLY).getExpire() - System.currentTimeMillis()) / 1000L);
            player.sendMessage(Messages.cooldownMessage(String.valueOf(dur)));
            return;
        }

        playerFaction.getPendingAllies().add(playerAllyFaction.getFactionID());

        playerFaction.sendMessage(Messages.allyRequestSent(player.getName(), playerAllyFaction.getDisplayName()));
        playerAllyFaction.sendMessage(Messages.allyRequest(playerFaction.getDisplayName()));

        Timer timer = TimerManager.createTimer(TimerType.ALLY, 300);

        playerFaction.getTimers().add(timer);
    }

}
