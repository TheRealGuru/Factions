package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.claims.ClaimManager;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.pillars.Pillar;
import gg.revival.factions.pillars.PillarManager;
import gg.revival.factions.tools.Messages;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;

public class FMapCommand extends FCommand {

    public FMapCommand() {
        super(
                "map",
                Arrays.asList("territory"),
                "/f map",
                "View nearby faction claims",
                null,
                CmdCategory.INFO,
                1,
                1,
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

        if (PillarManager.getActivePillars(player.getUniqueId()) == null || PillarManager.getActivePillars(player.getUniqueId()).isEmpty()) {
            HashSet<Faction> nearbyFactions = new HashSet<>();

            for (Claim claims : ClaimManager.getActiveClaims()) {
                if (!claims.getWorld().equals(player.getWorld())) continue;

                for (int i = 1; i < 5; i++) {
                    if (claims.getCorner(i).distanceSquared(player.getLocation()) <= (100 * 100)) {
                        if (!nearbyFactions.contains(claims.getClaimOwner()))
                            nearbyFactions.add(claims.getClaimOwner());
                    }
                }
            }

            if (nearbyFactions.isEmpty()) {
                player.sendMessage(Messages.noNearbyFactions());
                return;
            }

            int byteData = 0;

            player.sendMessage(ChatColor.YELLOW + "Here is a list of all factions within 100 blocks of your current location: ");
            player.sendMessage("     ");

            for (Faction nearby : nearbyFactions) {
                if (byteData > 15) continue; //More than 15 different factions within 100 blocks apparently...

                FancyMessage message = new FancyMessage(" - ").color(ChatColor.YELLOW).then(nearby.getDisplayName()).color(ChatColor.GOLD).command("/f show " + nearby.getDisplayName() + " -f");

                switch (byteData) {
                    case 0:
                        message.then(" (").color(ChatColor.YELLOW).then("White").color(ChatColor.WHITE).then(")").color(ChatColor.YELLOW);
                        break;
                    case 1:
                        message.then(" (").color(ChatColor.YELLOW).then("Orange").color(ChatColor.GOLD).then(")").color(ChatColor.YELLOW);
                        break;
                    case 2:
                        message.then(" (").color(ChatColor.YELLOW).then("Magenta").color(ChatColor.LIGHT_PURPLE).then(")").color(ChatColor.YELLOW);
                        break;
                    case 3:
                        message.then(" (").color(ChatColor.YELLOW).then("Light Blue").color(ChatColor.AQUA).then(")").color(ChatColor.YELLOW);
                        break;
                    case 4:
                        message.then(" (").color(ChatColor.YELLOW).then("Yellow").color(ChatColor.YELLOW).then(")").color(ChatColor.YELLOW);
                        break;
                    case 5:
                        message.then(" (").color(ChatColor.YELLOW).then("Lime").color(ChatColor.GREEN).then(")").color(ChatColor.YELLOW);
                        break;
                    case 6:
                        message.then(" (").color(ChatColor.YELLOW).then("Pink").color(ChatColor.LIGHT_PURPLE).then(")").color(ChatColor.YELLOW);
                        break;
                    case 7:
                        message.then(" (").color(ChatColor.YELLOW).then("Dark Gray").color(ChatColor.DARK_GRAY).then(")").color(ChatColor.YELLOW);
                        break;
                    case 8:
                        message.then(" (").color(ChatColor.YELLOW).then("Gray").color(ChatColor.GRAY).then(")").color(ChatColor.YELLOW);
                        break;
                    case 9:
                        message.then(" (").color(ChatColor.YELLOW).then("Cyan").color(ChatColor.DARK_AQUA).then(")").color(ChatColor.YELLOW);
                        break;
                    case 10:
                        message.then(" (").color(ChatColor.YELLOW).then("Purple").color(ChatColor.DARK_PURPLE).then(")").color(ChatColor.YELLOW);
                        break;
                    case 11:
                        message.then(" (").color(ChatColor.YELLOW).then("Blue").color(ChatColor.BLUE).then(")").color(ChatColor.YELLOW);
                        break;
                    case 12:
                        message.then(" (").color(ChatColor.YELLOW).then("Brown").color(ChatColor.GRAY).then(")").color(ChatColor.YELLOW);
                        break;
                    case 13:
                        message.then(" (").color(ChatColor.YELLOW).then("Green").color(ChatColor.DARK_GREEN).then(")").color(ChatColor.YELLOW);
                        break;
                    case 14:
                        message.then(" (").color(ChatColor.YELLOW).then("Red").color(ChatColor.RED).then(")").color(ChatColor.YELLOW);
                        break;
                    case 15:
                        message.then(" (").color(ChatColor.YELLOW).then("Black").color(ChatColor.DARK_GRAY).then(")").color(ChatColor.YELLOW);
                        break;
                }

                message.send(player);

                for (Claim claims : nearby.getClaims()) {
                    for (int i = 1; i < 5; i++) {
                        Location corner = claims.getCorner(i).clone();
                        corner.setY(player.getLocation().getY() - 5.0);

                        Pillar pillar = new Pillar(player.getUniqueId(), corner, Material.WOOL, (byte) byteData);
                        pillar.build();

                        PillarManager.getActivePillars().add(pillar);
                    }
                }

                byteData++;
            }

            player.sendMessage("     ");
            player.sendMessage(ChatColor.AQUA + "You can click a faction name to view more information");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Hiding all pillars...");

            for (Pillar pillars : PillarManager.getActivePillars(player.getUniqueId())) {
                pillars.remove();
                PillarManager.getActivePillars().remove(pillars);
            }
        }
    }

}
