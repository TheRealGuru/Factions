package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.core.PlayerManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;

public class FWithdrawCommand extends FCommand {

    public FWithdrawCommand() {
        super(
                "withdraw",
                Arrays.asList("withdrawl", "w"),
                "/f withdraw <amt>",
                "Withdraw money from your faction balance",
                null,
                CmdCategory.ECONOMY,
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
        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if(faction == null && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        String namedAmount = args[1];
        double bal = faction.getBalance();
        double amt = 0.0;

        try {
            amt = Math.abs(Double.valueOf(namedAmount));
        } catch (NumberFormatException e) {
            player.sendMessage(Messages.invalidAmount());
            return;
        }

        if(amt > bal) {
            player.sendMessage(Messages.notEnoughMoney());
            return;
        }

        faction.setBalance(faction.getBalance() - amt);
        PlayerManager.getPlayer(player.getUniqueId()).setBalance(PlayerManager.getPlayer(player.getUniqueId()).getBalance() + amt);

        DecimalFormat format = new DecimalFormat("#,###.00");
        faction.sendMessage(Messages.factionWithdrawl(player.getName(), format.format(amt)));
    }

}
