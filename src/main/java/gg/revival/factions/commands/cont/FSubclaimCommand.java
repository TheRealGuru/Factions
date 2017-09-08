package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.subclaims.Subclaim;
import gg.revival.factions.subclaims.SubclaimGUI;
import gg.revival.factions.subclaims.SubclaimManager;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class FSubclaimCommand extends FCommand {

    public FSubclaimCommand() {
        super(
                "subclaim",
                null,
                "/f subclaim",
                "Subclaim a chest",
                null,
                CmdCategory.MANAGE,
                1,
                1,
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

        Block block = ToolBox.getTargetBlock(player, 4);

        if(block == null) {
            player.sendMessage(Messages.notLookingAtChest());
            return;
        }

        if(!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) {
            player.sendMessage(Messages.notLookingAtChest());
            return;
        }

        Chest chest = (Chest)block.getState();
        DoubleChest doubleChest = null;

        if(chest.getInventory().getHolder() instanceof DoubleChest) {
            doubleChest = (DoubleChest)chest.getInventory().getHolder();
        }

        if(chest != null && doubleChest == null) {
            Subclaim subclaim = null;

            if(SubclaimManager.getSubclaimAt(chest.getLocation()) != null) {
                subclaim = SubclaimManager.getSubclaimAt(chest.getLocation());

                if(faction.getLeader().equals(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                if(subclaim.isOfficerAccess() && faction.getOfficers().contains(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                player.sendMessage(Messages.noSubclaimAccess());
                return;
            }

            for(Claim claims : faction.getClaims()) {
                if(!claims.inside(chest.getLocation(), false)) continue;

                List<UUID> accessPlayers = new ArrayList<>();
                accessPlayers.add(player.getUniqueId());

                subclaim = new Subclaim(UUID.randomUUID(), faction, chest.getLocation(), accessPlayers, true);

                SubclaimManager.addSubclaim(subclaim);

                SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                subclaimGUI.open();

                SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                player.sendMessage(Messages.subclaimCreated());
                faction.sendMessage(Messages.subclaimCreatedFaction(player.getName()));
                return;
            }

            player.sendMessage(Messages.subclaimOutsideClaim());
            return;
        }

        if(doubleChest != null) {
            Subclaim subclaim = null;

            if(SubclaimManager.getSubclaimAt(chest.getBlock().getLocation()) != null) {
                subclaim = SubclaimManager.getSubclaimAt(chest.getBlock().getLocation());

                if(faction.getLeader().equals(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                if(subclaim.isOfficerAccess() && faction.getOfficers().contains(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                if(subclaim.getPlayerAccess().contains(player.getUniqueId()) && subclaim.getSubclaimHolder().getRoster(false).contains(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                player.sendMessage(Messages.noSubclaimAccess());
                return;
            }

            for(BlockFace directions : ToolBox.getFlatDirections()) {
                if(SubclaimManager.getSubclaimAt(chest.getBlock().getRelative(directions).getLocation()) == null) continue;

                subclaim = SubclaimManager.getSubclaimAt(chest.getBlock().getRelative(directions).getLocation());

                if(faction.getLeader().equals(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                if(subclaim.isOfficerAccess() && faction.getOfficers().contains(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                if(subclaim.getPlayerAccess().contains(player.getUniqueId()) && subclaim.getSubclaimHolder().getRoster(false).contains(player.getUniqueId())) {
                    SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                    subclaimGUI.open();

                    SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                    return;
                }

                player.sendMessage(Messages.noSubclaimAccess());
                return;
            }

            for(Claim claims : faction.getClaims()) {
                if(!claims.inside(chest.getLocation(), false)) continue;

                List<UUID> accessPlayers = new ArrayList<>();
                accessPlayers.add(player.getUniqueId());

                subclaim = new Subclaim(UUID.randomUUID(), faction, chest.getLocation(), accessPlayers, true);

                SubclaimManager.addSubclaim(subclaim);

                SubclaimGUI subclaimGUI = new SubclaimGUI(player.getUniqueId(), subclaim);
                subclaimGUI.open();

                SubclaimManager.getSubclaimEditor().put(player.getUniqueId(), subclaimGUI);

                player.sendMessage(Messages.subclaimCreated());
                faction.sendMessage(Messages.subclaimCreatedFaction(player.getName()));

                Logger.log(Level.INFO, player.getName() + " created a new subclaim for " + faction.getDisplayName());

                return;
            }

            player.sendMessage(Messages.subclaimOutsideClaim());
            return;
        }
    }

}
