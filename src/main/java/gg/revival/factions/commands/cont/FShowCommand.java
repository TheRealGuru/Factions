package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.OfflinePlayerLookup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FShowCommand extends FCommand {

    public FShowCommand() {
        super(
                "show",
                Arrays.asList("who", "info"),
                "/f show <name>",
                "View a factions information",
                null,
                CmdCategory.INFO,
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

            Messages.factionInfo(faction, player);
        }

        if (args.length == 2) {
            String query = args[1];
            List<Faction> results = new ArrayList<>();

            if (FactionManager.getFactionByName(query) != null && FactionManager.getFactionByName(query) instanceof PlayerFaction) {
                results.add(FactionManager.getFactionByName(query));
            }

            OfflinePlayerLookup.getOfflinePlayerByName(query, (uuid, username) -> {
                if (uuid != null && username != null) {
                    if (FactionManager.getFactionByPlayer(uuid) != null) {
                        results.add(FactionManager.getFactionByPlayer(uuid));
                    }

                    if (results.size() == 0) {
                        player.sendMessage(Messages.factionNotFound());
                    } else if (results.size() == 1) {
                        Messages.factionInfo((PlayerFaction) results.get(0), player);
                    } else if (results.size() > 1) {
                        Messages.sendMultiFactionList(results, player, query);
                    }
                }
            });
        }

        if (args.length == 3) {
            String query = args[1];
            String queryType = args[2];

            if (queryType.equalsIgnoreCase("-p")) {
                OfflinePlayerLookup.getOfflinePlayerByName(query, (uuid, username) -> {
                    if (uuid != null && username != null) {
                        Faction faction = FactionManager.getFactionByPlayer(uuid);

                        if (faction != null && faction instanceof PlayerFaction) {
                            Messages.factionInfo((PlayerFaction) faction, player);
                        } else {
                            player.sendMessage(Messages.factionNotFound());
                        }
                    }
                });
            }

            if (queryType.equalsIgnoreCase("-f")) {
                Faction faction = FactionManager.getFactionByName(query);

                if (faction != null && faction instanceof PlayerFaction) {
                    Messages.factionInfo((PlayerFaction) faction, player);
                }
            }
        }
    }
}
