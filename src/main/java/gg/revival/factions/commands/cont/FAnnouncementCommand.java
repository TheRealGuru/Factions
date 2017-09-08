package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FAnnouncementCommand extends FCommand {

    public FAnnouncementCommand() {
        super(
                "announcement",
                null,
                "/f announcement [announcement]",
                "Send an announcement to your faction",
                null,
                CmdCategory.MANAGE,
                1,
                Integer.MAX_VALUE,
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

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if(args.length == 1) {
            if(faction.getAnnouncement() == null || faction.getAnnouncement().length() <= 0) {
                player.sendMessage(Messages.noAnnouncement());
                return;
            }

            player.sendMessage(Messages.factionAnnouncement(faction.getAnnouncement()));

            return;
        }

        StringBuilder messageBuilder = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            if(messageBuilder.toString().length() > 0) {
                messageBuilder.append(" ");
            }

            messageBuilder.append(args[i]);
        }

        String message = ChatColor.stripColor(messageBuilder.toString());

        if(!StringUtils.isAlphanumericSpace(message)) {
            player.sendMessage(Messages.badAnnouncement());
            return;
        }

        faction.setAnnouncement(message);
        faction.sendMessage(Messages.factionAnnouncement(message));

        Logger.log(Level.INFO, player.getName() + " set " + faction.getDisplayName() + "'s announcement to: " + message.toString());
    }

}
