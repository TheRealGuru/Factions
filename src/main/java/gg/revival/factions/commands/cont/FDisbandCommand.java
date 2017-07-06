package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FDisbandCommand extends FCommand {

    public FDisbandCommand() {
        super("disband",
                null,
                "/f disband",
                "Disband your faction",
                null,
                1,
                1,
                true);
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player)sender;

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Messages.leaderRequired());
            return;
        }

        if(faction.isRaidable()) {
            player.sendMessage(Messages.unraidableRequired());
            return;
        }

        if(faction.isFrozen()) {
            player.sendMessage(Messages.unfrozenRequired());
            return;
        }

        //TODO: Make it so faction can not be disbanded if member is combat tagged

        FactionManager.disbandFaction(player.getName(), faction);
    }
}
