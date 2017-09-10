package gg.revival.factions.commands.cont;

import gg.revival.factions.chat.ChatChannel;
import gg.revival.factions.chat.ChatChannelManager;
import gg.revival.factions.commands.CCommand;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CDeleteCommand extends CCommand {

    public CDeleteCommand() {
        super(
                "delete",
                null,
                "/cc delete",
                "Delete your chat channel",
                null,
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

        if (args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (ChatChannelManager.getChannel(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInChatChannel());
            return;
        }

        ChatChannel channel = ChatChannelManager.getChannel(player.getUniqueId());

        if (!channel.getOwner().equals(player.getUniqueId()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.noPermission());
            return;
        }

        channel.sendMessage(Messages.channelDeleted());
        ChatChannelManager.deleteChannel(channel);
    }

}
