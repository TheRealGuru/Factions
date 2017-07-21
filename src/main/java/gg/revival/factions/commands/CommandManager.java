package gg.revival.factions.commands;

import gg.revival.factions.FP;
import gg.revival.factions.commands.cont.*;
import gg.revival.factions.tools.Logger;

import java.util.HashSet;

public class CommandManager {

    private static HashSet<FCommand> commands = new HashSet<FCommand>();

    public static HashSet<FCommand> getCommands() {
        return commands;
    }

    public static FCommand getCommandByLabel(String label) {
        for (FCommand foundCommands : commands) {
            if (foundCommands.getLabel().equalsIgnoreCase(label)) {
                return foundCommands;
            } else if (foundCommands.getAliases() != null && !foundCommands.getAliases().isEmpty()) {
                for (String aliases : foundCommands.getAliases()) {
                    if (aliases.equalsIgnoreCase(label)) {
                        return foundCommands;
                    }
                }
            }
        }

        return null;
    }

    public static void loadCommands() {
        FP.getInstance().getCommand("faction").setExecutor(new FactionsCommandExecutor());
        FP.getInstance().getCommand("balance").setExecutor(new BalanceCommandExecutor());
        FP.getInstance().getCommand("pay").setExecutor(new PayCommandExecutor());

        FCreateCommand createCommand = new FCreateCommand();
        FCreateServerFactionCommand createServerFactionCommand = new FCreateServerFactionCommand();

        FDisbandCommand disbandCommand = new FDisbandCommand();
        FDisbandOtherCommand disbandOtherCommand = new FDisbandOtherCommand();

        FShowCommand showCommand = new FShowCommand();

        FClaimCommand claimCommand = new FClaimCommand();
        FClaimOtherCommand claimOtherCommand = new FClaimOtherCommand();

        FSubclaimCommand subclaimCommand = new FSubclaimCommand();

        FInviteCommand inviteCommand = new FInviteCommand();
        FUninviteCommand uninviteCommand = new FUninviteCommand();
        FAcceptCommand acceptCommand = new FAcceptCommand();

        FMapCommand mapCommand = new FMapCommand();

        FRenameCommand renameCommand = new FRenameCommand();
        FRenameForCommand renameForCommand = new FRenameForCommand();

        FSethomeCommand sethomeCommand = new FSethomeCommand();
        FSethomeForCommand sethomeForCommand = new FSethomeForCommand();

        FThawCommand thawCommand = new FThawCommand();

        FUnclaimCommand unclaimCommand = new FUnclaimCommand();
        FUnclaimForCommand unclaimForCommand = new FUnclaimForCommand();

        FAnnouncementCommand announcementCommand = new FAnnouncementCommand();

        FListCommand listCommand = new FListCommand();

        commands.add(createCommand);
        commands.add(createServerFactionCommand);
        commands.add(disbandCommand);
        commands.add(disbandOtherCommand);
        commands.add(showCommand);
        commands.add(claimCommand);
        commands.add(claimOtherCommand);
        commands.add(inviteCommand);
        commands.add(uninviteCommand);
        commands.add(mapCommand);
        commands.add(acceptCommand);
        commands.add(subclaimCommand);
        commands.add(renameCommand);
        commands.add(renameForCommand);
        commands.add(sethomeCommand);
        commands.add(sethomeForCommand);
        commands.add(thawCommand);
        commands.add(unclaimCommand);
        commands.add(unclaimForCommand);
        commands.add(announcementCommand);
        commands.add(listCommand);

        Logger.log("Loaded " + commands.size() + " Commands");
    }

}
