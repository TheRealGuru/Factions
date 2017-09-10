package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FRenameForCommand extends FCommand {

    public FRenameForCommand() {
        super(
                "renamefor",
                Arrays.asList("tagfor, setname"),
                "/f renamefor <faction> <name>",
                "Rename another faction",
                Permissions.ADMIN,
                CmdCategory.STAFF,
                3,
                3,
                false
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        String changer = "Console";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            changer = player.getName();

            if (!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }
        }

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String namedFaction = args[1];
        String newName = args[2];

        if (FactionManager.getFactionByName(namedFaction) == null) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        Faction faction = FactionManager.getFactionByName(namedFaction);

        if (!StringUtils.isAlphanumeric(newName)) {
            sender.sendMessage(Messages.badFactionName());
            return;
        }

        if (newName.length() < Configuration.MIN_FAC_NAME_SIZE || newName.length() > Configuration.MAX_FAC_NAME_SIZE) {
            sender.sendMessage(Messages.badFactionName());
            return;
        }

        if (newName.contains(" ")) {
            sender.sendMessage(Messages.badFactionName());
            return;
        }

        if (FactionManager.getFactionByName(newName) != null) {
            sender.sendMessage(Messages.facNameInUse());
            return;
        }

        Bukkit.broadcastMessage(Messages.factionRenamed(faction.getDisplayName(), newName, changer));

        Logger.log(Level.INFO, changer + " renamed " + faction.getDisplayName() + " to " + newName);

        faction.setDisplayName(newName);
    }

}
