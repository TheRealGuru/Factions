package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class FFreezeCommand extends FCommand {

    public FFreezeCommand() {
        super(
                "freeze",
                null,
                "/f freeze <faction> <duration>",
                "Freeze a factions power",
                Permissions.ADMIN,
                CmdCategory.STAFF,
                3,
                3,
                false
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        String freezer = "Console";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }

            freezer = player.getName();
        }

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String namedFaction = args[1];
        String namedDuration = args[2];
        Faction faction = FactionManager.getFactionByName(namedFaction);

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        int time = ToolBox.getTime(namedDuration);

        if (time == 0) {
            sender.sendMessage(Messages.badTime());
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, time);

        playerFaction.setUnfreezeTime(calendar.getTimeInMillis());
        playerFaction.sendMessage(Messages.powerFrozenByStaff(freezer));

        sender.sendMessage(Messages.powerFrozenByStaffOther(playerFaction.getDisplayName()));
    }

}
