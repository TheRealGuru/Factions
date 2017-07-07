package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FCreateCommand extends FCommand {

    public FCreateCommand() {
        super(
                "create",
                null,
                "/f create <name>",
                "Create a faction",
                null,
                2,
                2,
                true);
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

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyInFaction());
            return;
        }

        String factionName = args[1];

        if (!StringUtils.isAlphanumeric(factionName)) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (factionName.length() < Configuration.MIN_FAC_NAME_SIZE || factionName.length() > Configuration.MAX_FAC_NAME_SIZE) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (factionName.contains(" ")) {
            player.sendMessage(Messages.badFactionName());
            return;
        }

        if (FactionManager.getFactionByName(factionName) != null) {
            player.sendMessage(Messages.facNameInUse());
            return;
        }

        FactionManager.createPlayerFaction(player.getUniqueId(), factionName);
    }

}
