package gg.revival.factions.commands.cont;

import gg.revival.factions.chat.ChatChannelManager;
import gg.revival.factions.commands.CCommand;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.ToolBox;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCreateCommand extends CCommand {

    public CCreateCommand() {
        super(
                "create",
                null,
                "/cc create <name>",
                "Create a chat channel",
                null,
                2,
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

        String name = args[1];
        String password = ToolBox.getRandomPassword(4);

        if(ChatChannelManager.getChannel(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyInChannel());
            return;
        }

        if(!StringUtils.isAlphanumeric(name)) {
            player.sendMessage(Messages.badChannelName());
            return;
        }

        if(name.length() < Configuration.CHANNEL_MIN_NAME_SIZE || name.length() > Configuration.CHANNEL_MAX_NAME_SIZE) {
            player.sendMessage(Messages.badChannelName());
            return;
        }

        if(name.equalsIgnoreCase("p") ||
                name.equalsIgnoreCase("public") ||
                name.equalsIgnoreCase("g") ||
                name.equalsIgnoreCase("global") ||
                name.equalsIgnoreCase("f") ||
                name.equalsIgnoreCase("faction") ||
                name.equalsIgnoreCase("a") ||
                name.equalsIgnoreCase("ally") ||
                name.equalsIgnoreCase("cc")) {

            player.sendMessage(Messages.badChannelName());
            return;
        }

        if(ChatChannelManager.getChannelByName(name) != null) {
            player.sendMessage(Messages.channelExists());
            return;
        }

        ChatChannelManager.createChannel(name, password, player.getUniqueId());
        player.sendMessage(Messages.channelCreated(name));
    }

}
