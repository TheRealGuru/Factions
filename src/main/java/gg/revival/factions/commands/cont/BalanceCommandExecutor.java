package gg.revival.factions.commands.cont;

import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.noConsole());
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 2 || args.length > 3) {
            player.sendMessage(ChatColor.RED + "/balance");
            player.sendMessage(ChatColor.RED + "/balance [player]");

            if (player.hasPermission(Permissions.ADMIN)) {
                player.sendMessage(ChatColor.RED + "/balance give <player> <amount>");
                player.sendMessage(ChatColor.RED + "/balance take <player> <amount>");
                player.sendMessage(ChatColor.RED + "/balance set <player> <amount>");
            }

            return false;
        }

        if (args.length == 0) {
            player.sendMessage(Messages.currentBalance(PlayerManager.getPlayer(player.getUniqueId()).getBalance()));

            return true;
        }

        if (args.length == 1) {
            String query = args[0];

            if (Bukkit.getPlayer(query) == null || !Bukkit.getPlayer(query).isOnline()) {
                player.sendMessage(Messages.playerNotFound());
                return false;
            }

            Player lookupPlayer = Bukkit.getPlayer(query);

            player.sendMessage(Messages.currentBalanceOther(lookupPlayer.getName(), PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance()));

            return true;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                String query = args[1];
                String amount = args[2];

                if (Bukkit.getPlayer(query) == null || !Bukkit.getPlayer(query).isOnline()) {
                    player.sendMessage(Messages.playerNotFound());
                    return false;
                }

                Player lookupPlayer = Bukkit.getPlayer(query);

                if (!NumberUtils.isNumber(amount)) {
                    player.sendMessage(Messages.invalidAmount());

                    return false;
                }

                double originalBalance = PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance();
                double newBalance = originalBalance + Double.valueOf(amount);

                PlayerManager.getPlayer(lookupPlayer.getUniqueId()).setBalance(newBalance);

                player.sendMessage(Messages.balanceModified(newBalance));
                lookupPlayer.sendMessage(Messages.balanceModifiedOther(player.getName(), PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance()));

                return false;
            }

            if (args[0].equalsIgnoreCase("take")) {
                String query = args[1];
                String amount = args[2];

                if (Bukkit.getPlayer(query) == null || !Bukkit.getPlayer(query).isOnline()) {
                    player.sendMessage(Messages.playerNotFound());
                    return false;
                }

                Player lookupPlayer = Bukkit.getPlayer(query);

                if (!NumberUtils.isNumber(amount)) {
                    player.sendMessage(Messages.invalidAmount());

                    return false;
                }

                double originalBalance = PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance();
                double newBalance = originalBalance - Double.valueOf(amount);

                PlayerManager.getPlayer(lookupPlayer.getUniqueId()).setBalance(newBalance);

                player.sendMessage(Messages.balanceModified(newBalance));
                lookupPlayer.sendMessage(Messages.balanceModifiedOther(player.getName(), PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance()));

                return false;
            }

            if (args[0].equalsIgnoreCase("set")) {
                String query = args[1];
                String amount = args[2];

                if (Bukkit.getPlayer(query) == null || !Bukkit.getPlayer(query).isOnline()) {
                    player.sendMessage(Messages.playerNotFound());
                    return false;
                }

                Player lookupPlayer = Bukkit.getPlayer(query);

                if (!NumberUtils.isNumber(amount)) {
                    player.sendMessage(Messages.invalidAmount());

                    return false;
                }

                PlayerManager.getPlayer(lookupPlayer.getUniqueId()).setBalance(Double.valueOf(amount));

                player.sendMessage(Messages.balanceModified(PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance()));
                lookupPlayer.sendMessage(Messages.balanceModifiedOther(player.getName(), PlayerManager.getPlayer(lookupPlayer.getUniqueId()).getBalance()));

                return false;
            }
        }

        return false;
    }

}
