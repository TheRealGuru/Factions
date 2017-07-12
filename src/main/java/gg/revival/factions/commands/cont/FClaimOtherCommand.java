package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.PendingClaim;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FClaimOtherCommand extends FCommand {

    public FClaimOtherCommand() {
        super(
                "claimfor",
                null,
                "/f claimfor <faction>",
                "Claim for another faction",
                Permissions.ADMIN,
                2,
                2,
                true);
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

        if(ClaimManager.getPendingClaim(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyClaimingLand());
            return;
        }

        Faction faction = FactionManager.getFactionByName(factionName);

        if(player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Messages.inventoryFull());
            return;
        }

        PendingClaim pendingClaim = new PendingClaim(player.getUniqueId(), faction);
        ClaimManager.getClaimEditors().put(player.getUniqueId(), pendingClaim);

        player.getInventory().addItem(ToolBox.getClaimingStick());

        new BukkitRunnable() {
            public void run() {
                if(ClaimManager.getPendingClaim(player.getUniqueId()) == pendingClaim) {
                    ClaimManager.getClaimEditors().remove(player.getUniqueId());
                }
            }
        }.runTaskLater(FP.getInstance(), 300 * 20L);
    }

}
