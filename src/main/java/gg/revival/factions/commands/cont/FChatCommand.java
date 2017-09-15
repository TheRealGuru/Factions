package gg.revival.factions.commands.cont;

import gg.revival.factions.chat.FactionChatChannelType;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FChatCommand extends FCommand {

    public FChatCommand() {
        super(
                "chat",
                Arrays.asList("c"),
                "/f chat [public/faction/ally]",
                "Change between faction chat channels",
                null,
                CmdCategory.BASICS,
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

        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByPlayer(player.getUniqueId());
        FactionChatChannelType type = FactionChatChannelType.PUBLIC;

        if (faction != null && faction.getFactionChat().contains(player.getUniqueId()))
            type = FactionChatChannelType.FACTION;

        if (faction != null && faction.getAllyChat().contains(player.getUniqueId()))
            type = FactionChatChannelType.ALLY;

        if (args.length == 1) {
            if (type.equals(FactionChatChannelType.PUBLIC)) {
                if (faction != null) {
                    type = FactionChatChannelType.FACTION;
                    faction.getFactionChat().add(player.getUniqueId());
                    player.sendMessage(Messages.joinedFactionChannel(ChatColor.DARK_GREEN + StringUtils.capitalize(type.toString().toLowerCase())));
                    return;
                }

                type = FactionChatChannelType.FACTION;
            }

            if (type.equals(FactionChatChannelType.FACTION)) {
                if (faction == null) {
                    type = FactionChatChannelType.PUBLIC;
                    player.sendMessage(Messages.joinedFactionChannel(ChatColor.GOLD + StringUtils.capitalize(type.toString().toLowerCase())));
                    return;
                }

                faction.getFactionChat().remove(player.getUniqueId());

                if (faction.getAllies().isEmpty()) {
                    type = FactionChatChannelType.PUBLIC;
                    player.sendMessage(Messages.joinedFactionChannel(ChatColor.GOLD + StringUtils.capitalize(type.toString().toLowerCase())));
                    return;
                }

                type = FactionChatChannelType.ALLY;
                faction.getAllyChat().add(player.getUniqueId());
                player.sendMessage(Messages.joinedFactionChannel(ChatColor.LIGHT_PURPLE + StringUtils.capitalize(type.toString().toLowerCase())));
                return;
            }

            if (type.equals(FactionChatChannelType.ALLY)) {
                faction.getAllyChat().remove(player.getUniqueId());
                player.sendMessage(Messages.joinedFactionChannel(ChatColor.GOLD + StringUtils.capitalize(type.toString().toLowerCase())));
                return;
            }

            return;
        }

        if (args[1].equalsIgnoreCase("p") || args[1].equalsIgnoreCase("public") || args[1].equalsIgnoreCase("g") || args[1].equalsIgnoreCase("global")) {
            type = FactionChatChannelType.PUBLIC;
            faction.getFactionChat().remove(player.getUniqueId());
            faction.getAllyChat().remove(player.getUniqueId());
            player.sendMessage(Messages.joinedFactionChannel(ChatColor.GOLD + StringUtils.capitalize(type.toString().toLowerCase())));
            return;
        }

        if (args[1].equalsIgnoreCase("f") || args[1].equalsIgnoreCase("faction")) {
            if (faction == null) {
                player.sendMessage(Messages.notInFaction());
                return;
            }

            type = FactionChatChannelType.FACTION;

            if (faction.getFactionChat().contains(player.getUniqueId())) {
                player.sendMessage(Messages.alreadySpeakingInChannel());

                return;
            }

            faction.getAllyChat().remove(player.getUniqueId());
            faction.getFactionChat().add(player.getUniqueId());
            player.sendMessage(Messages.joinedFactionChannel(ChatColor.DARK_GREEN + StringUtils.capitalize(type.toString().toLowerCase())));
            return;
        }

        if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("ally")) {
            if (faction == null) {
                player.sendMessage(Messages.notInFaction());
                return;
            }

            type = FactionChatChannelType.ALLY;

            if (faction.getAllies().isEmpty()) {
                player.sendMessage(Messages.noAlliesInAllyChat());

                return;
            }

            if (faction.getAllyChat().contains(player.getUniqueId())) {
                player.sendMessage(Messages.alreadySpeakingInChannel());

                return;
            }

            faction.getFactionChat().remove(player.getUniqueId());
            faction.getAllyChat().add(player.getUniqueId());
            player.sendMessage(Messages.joinedFactionChannel(ChatColor.LIGHT_PURPLE + StringUtils.capitalize(type.toString().toLowerCase())));
            return;
        }

        player.sendMessage(ChatColor.RED + getSyntax());
    }

}
