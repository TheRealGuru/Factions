package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.timers.Timer;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FRenameCommand extends FCommand {

    public FRenameCommand() {
        super(
                "rename",
                Arrays.asList("changename", "tag"),
                "/f rename <name>",
                "Rename your faction",
                null,
                CmdCategory.MANAGE,
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

        String factionName = args[1];

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Messages.leaderRequired());
            return;
        }

        if(faction.isBeingTimed(TimerType.RENAME) && faction.getTimer(TimerType.RENAME).getExpire() > System.currentTimeMillis()) {
            player.sendMessage(Messages.cooldownMessage(ToolBox.getFormattedCooldown(false, faction.getTimer(TimerType.RENAME).getExpire() - System.currentTimeMillis())));
            return;
        }

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

        Bukkit.broadcastMessage(Messages.factionRenamed(faction.getDisplayName(), factionName, player.getName()));

        Logger.log(Level.INFO, player.getName() + " renamed " + faction.getDisplayName() + " to " + factionName);

        faction.setDisplayName(factionName);

        Timer timer = TimerManager.createTimer(TimerType.RENAME, Configuration.RENAME_COOLDOWN);
        faction.getTimers().add(timer);
    }

}
