package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FThawCommand extends FCommand {

    public FThawCommand() {
        super(
                "thaw",
                Arrays.asList("unfreeze"),
                "/f thaw <faction>",
                "Thaw a factions power",
                Permissions.ADMIN,
                CmdCategory.STAFF,
                2,
                2,
                false
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        String unfreezer = "CONSOLE";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }

            unfreezer = player.getName();
        }

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String factionName = args[1];

        if (FactionManager.getFactionByName(factionName) == null) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        Faction faction = FactionManager.getFactionByName(factionName);

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;

        if (!playerFaction.isFrozen()) {
            sender.sendMessage(Messages.powerNotFrozen());
            return;
        }

        sender.sendMessage(Messages.powerThawed(playerFaction.getDisplayName()));

        playerFaction.setUnfreezeTime(0);
        playerFaction.sendMessage(Messages.powerThawedOther());

        Logger.log(Level.INFO, unfreezer + " has thawed " + playerFaction.getDisplayName() + "'s power");
    }

}
