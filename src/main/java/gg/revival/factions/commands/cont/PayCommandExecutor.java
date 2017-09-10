package gg.revival.factions.commands.cont;

import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.FPlayer;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PayCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[]) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.noConsole());
                return false;
            }

            Player player = (Player) sender;

            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "/pay <player> <amount>");
                return false;
            }

            String paidPlayerName = args[0];
            String amountName = args[1];

            if (Bukkit.getPlayer(paidPlayerName) == null || !Bukkit.getPlayer(paidPlayerName).isOnline()) {
                player.sendMessage(Messages.playerNotFound());
                return false;
            }

            if (!NumberUtils.isNumber(amountName)) {
                player.sendMessage(Messages.invalidAmount());
                return false;
            }

            Player paidPlayer = Bukkit.getPlayer(paidPlayerName);
            double amount = Double.valueOf(amountName);

            if (paidPlayer.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Messages.cantPaySelf());
                return false;
            }

            FPlayer factionPlayer = PlayerManager.getPlayer(player.getUniqueId());
            FPlayer factionPaidPlayer = PlayerManager.getPlayer(paidPlayer.getUniqueId());

            if (factionPlayer.getBalance() < amount) {
                player.sendMessage(Messages.notEnoughMoney());
                return false;
            }

            factionPlayer.setBalance(factionPlayer.getBalance() - amount);
            factionPaidPlayer.setBalance(factionPaidPlayer.getBalance() + amount);

            player.sendMessage(Messages.paidPlayer(amount, paidPlayer.getName()));
            paidPlayer.sendMessage(Messages.paidPlayerOther(amount, player.getName()));

            Logger.log(Level.INFO, player.getName() + " sent $" + amountName + " to " + paidPlayer.getName());
        }

        return false;
    }
}
