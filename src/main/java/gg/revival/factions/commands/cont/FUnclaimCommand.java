package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.CmdCategory;
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
                CmdCategory.CLAIM,
                1,
                2,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player) sender;

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());

        if (!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        if (faction.isFrozen()) {
            player.sendMessage(Messages.unfrozenRequired());
            return;
        }

        if (faction.getClaims().isEmpty()) {
            player.sendMessage(Messages.noClaims());
            return;
        }

        if (args.length == 1) {
            Claim claim = null;

            for (Claim claims : faction.getClaims()) {
                if (!claims.inside(player.getLocation(), true)) continue;

                claim = claims;
            }

            if (claim == null) {
                player.sendMessage(Messages.notStandingInClaims());
                return;
            }

            if (faction.getClaims().size() > 2) {
                for (Claim claimSetOne : faction.getClaims()) {
                    boolean isTouching = false;

                    if (claimSetOne.getClaimID().equals(claim.getClaimID())) continue;

                    for (Claim claimSetTwo : faction.getClaims()) {
                        if (claimSetTwo.getClaimID().equals(claim.getClaimID()) || claimSetOne.getClaimID().equals(claimSetTwo.getClaimID()))
                            continue;

                        for (Location perimeter : claimSetTwo.getPerimeter(claimSetTwo.getWorldName(), 64)) {

                            if (claimSetOne.isTouching(perimeter)) {
                                isTouching = true;
                            }
                        }
                    }

                    if (!isTouching) {
                        player.sendMessage(Messages.unclaimNotConnected());
                        return;
                    }
                }
            }

            faction.getClaims().remove(claim);
            faction.setBalance(faction.getBalance() + claim.getClaimValue());
            ClaimManager.deleteClaim(claim);

            faction.sendMessage(Messages.landUnclaimedOther(player.getName()));
            player.sendMessage(Messages.landUnclaimed(String.valueOf(claim.getClaimValue())));

            Logger.log(Level.INFO, player.getName() + " unclaimed land for " + faction.getDisplayName());

            return;
        }

        if (args.length == 2) {
            if (!args[1].equalsIgnoreCase("all")) {
                player.sendMessage(ChatColor.RED + getSyntax());
                return;
            }

            if (faction.getClaims().isEmpty()) {
                player.sendMessage(Messages.noClaims());
                return;
            }

            double totalReturned = 0.0;

            for (Claim claims : faction.getClaims()) {
                totalReturned += claims.getClaimValue();

                ClaimManager.deleteClaim(claims);
            }

            faction.getClaims().clear();
            faction.setBalance(faction.getBalance() + totalReturned);

            faction.sendMessage(Messages.landUnclaimedOther(player.getName()));
            player.sendMessage(Messages.landUnclaimed(String.valueOf(totalReturned)));

            return;
        }
    }

}
