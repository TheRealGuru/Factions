package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FUnallyCommand extends FCommand {

    public FUnallyCommand() {
        super(
                "unally",
                null,
                "/f unally <faction>",
                "Break an alliance with another faction",
                null,
                CmdCategory.BASICS,
                2,
                2,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player) sender;

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String namedFaction = args[1];

        Faction faction = FactionManager.getFactionByPlayer(player.getUniqueId());
        Faction allyFaction = FactionManager.getFactionByName(namedFaction);

        if (!Configuration.ALLIANCES_ENABLED) {
            player.sendMessage(Messages.alliesDisabled());
            return;
        }

        if (faction == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;

        if (!playerFaction.getOfficers().contains(player.getUniqueId()) && !playerFaction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if (allyFaction == null || !(allyFaction instanceof PlayerFaction)) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerAllyFaction = (PlayerFaction) allyFaction;

        if (playerFaction.getPendingAllies().contains(playerAllyFaction.getFactionID())) {
            playerFaction.getPendingAllies().remove(playerAllyFaction.getFactionID());

            playerFaction.sendMessage(Messages.allyRequestRevoked(player.getName(), playerAllyFaction.getDisplayName()));
            playerAllyFaction.sendMessage(Messages.allyRequestRevokedOther(playerFaction.getDisplayName()));

            return;
        }

        if (!playerFaction.getAllies().contains(playerAllyFaction)) {
            player.sendMessage(Messages.notAllied());
            return;
        }

        playerFaction.getAllies().remove(playerAllyFaction.getFactionID());
        playerAllyFaction.getAllies().remove(playerFaction.getFactionID());

        Bukkit.broadcastMessage(Messages.allianceBroken(playerFaction.getDisplayName(), playerAllyFaction.getDisplayName()));
    }

}
