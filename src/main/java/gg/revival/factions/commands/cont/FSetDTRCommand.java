package gg.revival.factions.commands.cont;

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

import java.math.BigDecimal;

public class FSetDTRCommand extends FCommand {

    public FSetDTRCommand() {
        super(
                "setdtr",
                null,
                "/f setdtr <faction> <dtr>",
                "Set the DTR of a faction",
                Permissions.ADMIN,
                3,
                3,
                false
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        String changer = "Console";

        if(sender instanceof Player) {
            Player player = (Player)sender;

            changer = player.getName();

            if(!player.hasPermission(getPermission())) {
                player.sendMessage(Messages.noPermission());
                return;
            }
        }

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            sender.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String namedFaction = args[1];
        String namedDTR = args[2];
        double dtr;

        try {
            dtr = Double.valueOf(namedDTR);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.invalidAmount());
            return;
        }

        Faction faction = FactionManager.getFactionByName(namedFaction);

        if(faction == null || !(faction instanceof PlayerFaction)) {
            sender.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction)faction;

        if(playerFaction.getMaxDTR() < dtr) {
            dtr = playerFaction.getMaxDTR();
        }

        playerFaction.setDtr(BigDecimal.valueOf(dtr));
        playerFaction.sendMessage(Messages.dtrChanged(changer, dtr));

        sender.sendMessage(Messages.dtrChangedOther(playerFaction.getDisplayName(), dtr));

        Logger.log(changer + " updated " + playerFaction.getDisplayName() + "'s DTR to " + dtr);
    }

}
