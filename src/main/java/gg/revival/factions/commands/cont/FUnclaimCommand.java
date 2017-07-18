package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FUnclaimCommand extends FCommand {

    public FUnclaimCommand() {
        super(
                "unclaim",
                null,
                "/f unclaim [all]",
                "Unclaim faction land",
                null,
                1,
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

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if(faction.isFrozen()) {
            //TODO: Send cant perform while frozen
            return;
        }

        if(faction.getClaims().isEmpty()) {
            //TODO: Send no claims error
            return;
        }

        if(args.length == 0) {
            Claim claim = null;

            for(Claim claims : faction.getClaims()) {
                if(!claims.inside(player.getLocation(), true)) continue;

                claim = claims;
            }

            if(claim == null) {
                //TODO: Send not standing inside claim error
                return;
            }

            if(faction.getClaims().size() > 2) { //They have multiple claims, we need to check to make sure they're all still going to be connected
                for(Claim claims : faction.getClaims()) {
                    boolean isTouching = false;

                    if(claims.getClaimID().equals(claim.getClaimID())) continue;

                    for(Location perimeter : claims.getPerimeter(claims.getWorldName(), 64)) {
                        if(!faction.isTouching(perimeter) || claim.inside(perimeter, false)) continue;

                        isTouching = true;
                    }

                    if(!isTouching) {
                        //TODO: Send claims would not be touching if unclaimed error
                        return;
                    }
                }
            }

            faction.getClaims().remove(claim);
            faction.setBalance(faction.getBalance() + claim.getClaimValue());
            ClaimManager.deleteClaim(claim);

            //TODO: Send unclaim messages

            Logger.log(Level.INFO, player.getName() + " unclaimed land for " + faction.getDisplayName());

            return;
        }

        if(args.length == 1) {
            if(!args[1].equalsIgnoreCase("all")) {
                player.sendMessage(ChatColor.RED + getSyntax());
                return;
            }

            if(faction.getClaims().isEmpty()) {
                //TODO: Send no claims error
                return;
            }

            double totalReturned = 0.0;

            for(Claim claims : faction.getClaims()) {
                totalReturned += claims.getClaimValue();

                ClaimManager.deleteClaim(claims);
            }

            faction.getClaims().clear();
            faction.setBalance(faction.getBalance() + totalReturned);

            //TODO: Send unclaim all message

            return;
        }
    }

}
