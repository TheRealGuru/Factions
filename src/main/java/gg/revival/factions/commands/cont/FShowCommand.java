package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FShowCommand extends FCommand {

    public FShowCommand() {
        super(
                "show",
                Arrays.asList("who", "info"),
                "/f show <name>",
                "View a factions information",
                null,
                1,
                3,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player) sender;

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (args.length == 1) {
            if (FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
                player.sendMessage(Messages.notInFaction());
                return;
            }

            PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

            new BukkitRunnable() {
                public void run() {
                    String message = Messages.factionInfo(faction, player);

                    new BukkitRunnable() {
                        public void run() {
                            player.sendMessage(message);
                        }
                    }.runTask(FP.getInstance());
                }
            }.runTaskAsynchronously(FP.getInstance());
        }

        if (args.length == 2) {

        }

        if (args.length == 3) {
            String query = args[1];
            String queryType = args[2];

            if (queryType.equalsIgnoreCase("-p")) {
                //TODO: Perform player lookup
            }

            if (query.equalsIgnoreCase("-f")) {
                //TODO: Perform faction lookup
            }
        }
    }
}
