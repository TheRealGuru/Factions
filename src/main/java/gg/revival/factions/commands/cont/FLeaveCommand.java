package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FLeaveCommand extends FCommand {

    public FLeaveCommand() {
        super(
                "leave",
                null,
                "/f leave",
                "Leave your faction",
                null,
                CmdCategory.BASICS,
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

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        Faction faction = FactionManager.getFactionByPlayer(player.getUniqueId());

        if(faction == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction)faction;

        if(playerFaction.isFrozen()) {
            player.sendMessage(Messages.unfrozenRequired());
            return;
        }

        FPlayer facPlayer = PlayerManager.getPlayer(player.getUniqueId());

        if(playerFaction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Messages.cantLeaveWhileLeader());
            return;
        }

        if(facPlayer.isBeingTimed(TimerType.TAG)) {
            player.sendMessage(Messages.cantWhileTagged());
            return;
        }

        playerFaction.getOfficers().remove(player.getUniqueId());
        playerFaction.getMembers().remove(player.getUniqueId());

        playerFaction.sendMessage(Messages.playerLeft(player.getName()));
        player.sendMessage(Messages.playerLeftOther());

        Logger.log(player.getName() + " left " + playerFaction.getDisplayName());
    }

}
