package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FSaveCommand extends FCommand {

    public FSaveCommand() {
        super(
                "save",
                null,
                "/f save",
                "Save all faction information to DB",
                Permissions.ADMIN,
                CmdCategory.STAFF,
                1,
                1,
                false
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if(sender instanceof Player) {
            Player player = (Player)sender;

            if(!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }
        }

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        Bukkit.broadcastMessage("[" + ChatColor.RED + "" + ChatColor.BOLD + " ! " + ChatColor.WHITE + "] " + ChatColor.BOLD + "Now saving all Faction & Player information... This may take a second.");

        FactionManager.saveAllFactions(false, false);
        PlayerManager.saveAllProfiles(false, false);
    }

}
