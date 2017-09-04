package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.tasks.StuckTask;
import gg.revival.factions.timers.TimerManager;
import gg.revival.factions.timers.TimerType;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FStuckCommand extends FCommand {

    public FStuckCommand() {
        super(
                "stuck",
                Arrays.asList("unstuck"),
                "/f stuck",
                "Teleport to a safer location",
                null,
                1,
                1,
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

        if(ClaimManager.getClaimAt(player.getLocation(), true) == null) {
            player.sendMessage(Messages.notInsideClaim());
            return;
        }

        PlayerManager.getPlayer(player.getUniqueId()).addTimer(TimerManager.createTimer(TimerType.STUCK, Configuration.STUCK_WARMUP));
        StuckTask.getStartingLocations().put(player.getUniqueId(), player.getLocation());
        player.sendMessage(Messages.stuckWarpStarted(Configuration.STUCK_WARMUP));
    }

}
