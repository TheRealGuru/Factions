package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FUnclaimForCommand extends FCommand {

    public FUnclaimForCommand() {
        super(
                "unclaimfor",
                null,
                "/f unclaimfor <faction> [all]",
                "Unclaim land for another faction",
                Permissions.ADMIN,
                2,
                3,
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

        if(!player.hasPermission(getPermission())) {
            player.sendMessage(Messages.noPermission());
            return;
        }

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        String factionName = args[1];

        if(FactionManager.getFactionByName(factionName) == null) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        Faction faction = FactionManager.getFactionByName(factionName);

        if(faction.getClaims().isEmpty()) {
            player.sendMessage(Messages.noClaims());
            return;
        }

        if(args.length == 2) {
            Claim claim = null;

            for(Claim claims : faction.getClaims()) {
                if(!claims.inside(player.getLocation(), true)) continue;

                claim = claims;
            }

            if(claim == null) {
                player.sendMessage(Messages.notStandingInClaims());
                return;
            }

            if(faction.getClaims().size() > 2) {
                for(Claim claimSetOne : faction.getClaims()) {
                    boolean isTouching = false;

                    if(claimSetOne.getClaimID().equals(claim.getClaimID())) continue;

                    for(Claim claimSetTwo : faction.getClaims()) {
                        if(claimSetTwo.getClaimID().equals(claim.getClaimID()) || claimSetOne.getClaimID().equals(claimSetTwo.getClaimID())) continue;

                        for(Location perimeter : claimSetTwo.getPerimeter(claimSetTwo.getWorldName(), 64)) {

                            if(claimSetOne.isTouching(perimeter)) {
                                isTouching = true;
                            }
                        }
                    }

                    if(!isTouching) {
                        player.sendMessage(Messages.unclaimNotConnected());
                        return;
                    }
                }
            }

            faction.getClaims().remove(claim);

            if(faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;

                playerFaction.setBalance(playerFaction.getBalance() + claim.getClaimValue());
                playerFaction.sendMessage(Messages.landUnclaimedOther(player.getName()));
            }

            ClaimManager.deleteClaim(claim);

            player.sendMessage(Messages.landUnclaimed(String.valueOf(claim.getClaimValue())));

            Logger.log(Level.INFO, player.getName() + " unclaimed land for " + faction.getDisplayName());

            return;
        }

        if(args.length == 3) {
            if(!args[2].equalsIgnoreCase("all")) {
                player.sendMessage(ChatColor.RED + getSyntax());
                return;
            }

            double totalReturned = 0.0;

            for(Claim claims : faction.getClaims()) {
                totalReturned += claims.getClaimValue();

                ClaimManager.deleteClaim(claims);
            }

            if(faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction)faction;

                playerFaction.setBalance(playerFaction.getBalance() + totalReturned);
                playerFaction.sendMessage(Messages.landUnclaimedOther(player.getName()));
            }

            faction.getClaims().clear();

            player.sendMessage(Messages.landUnclaimed(String.valueOf(totalReturned)));

            return;
        }
    }

}
