package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.OfflinePlayerLookup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FLeaderCommand extends FCommand {

    public FLeaderCommand() {
        super(
                "leader",
                null,
                "/f leader <new leader>",
                "Assign a new faction leader",
                null,
                CmdCategory.MANAGE,
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

        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Messages.leaderRequired());
            return;
        }

        String namedPlayer = args[1];

        OfflinePlayerLookup.getOfflinePlayerByName(namedPlayer, (uuid, username) -> {
            if (uuid == null || username == null) {
                player.sendMessage(Messages.playerNotFound());
                return;
            }

            if (!faction.getRoster(false).contains(uuid)) {
                player.sendMessage(Messages.playerNotInFaction());
                return;
            }

            faction.setLeader(uuid);
            faction.getOfficers().add(player.getUniqueId());

            faction.sendMessage(Messages.newLeader(player.getName(), username));
        });
    }

}
