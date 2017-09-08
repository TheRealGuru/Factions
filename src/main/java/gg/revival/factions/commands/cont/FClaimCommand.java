package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.claims.PendingClaim;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FClaimCommand extends FCommand {

    public FClaimCommand() {
        super(
                "claim",
                null,
                "/f claim",
                "Claim faction land",
                null,
                CmdCategory.CLAIM,
                1,
                1,
                true);
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

        if(ClaimManager.getPendingClaim(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyClaimingLand());
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
