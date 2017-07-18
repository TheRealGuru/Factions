package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FAcceptCommand extends FCommand {

    public FAcceptCommand() {
        super(
                "accept",
                null,
                "/f accept <faction>",
                "Accept a faction invitation",
                null,
                2,
                2,
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

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyInFaction());
            return;
        }

        String namedFaction = args[1];

        if(FactionManager.getFactionByName(namedFaction) == null) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByName(namedFaction);

        if(!faction.getPendingInvites().contains(player.getUniqueId())) {
            player.sendMessage(Messages.noPendingInviteOther());
            return;
        }

        if(faction.getRoster(false).size() >= Configuration.MAX_FAC_SIZE) {
            player.sendMessage(Messages.factionFull());
            return;
        }

        if(faction.isFrozen()) {
            player.sendMessage(Messages.unfrozenRequiredOther());
            return;
        }

        faction.getMembers().add(player.getUniqueId());
        faction.sendMessage(Messages.joinedFactionOther(player.getName()));

        player.sendMessage(Messages.joinedFaction(faction.getDisplayName()));

        Logger.log(Level.INFO, player.getName() + " joined " + faction.getDisplayName());
    }

}
