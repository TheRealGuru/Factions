package gg.revival.factions.commands;

import gg.revival.factions.FP;
import gg.revival.factions.commands.cont.*;
import gg.revival.factions.tools.Logger;
import lombok.Getter;

import java.util.HashSet;

public class CommandManager {

    @Getter public static HashSet<FCommand> factionCommands = new HashSet<>();
    @Getter public static HashSet<CCommand> chatCommands = new HashSet<>();

    public static FCommand getFactionCommandByLabel(String label) {
        for (FCommand foundCommands : factionCommands) {
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

    public static CCommand getChatCommandByLabel(String label) {
        for(CCommand foundCommands : chatCommands) {
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
        FP.getInstance().getCommand("chatchannel").setExecutor(new ChatChannelsCommandExecutor());
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
        FChatCommand chatCommand = new FChatCommand();
        FLeaderCommand leaderCommand = new FLeaderCommand();
        FPromoteCommand promoteCommand = new FPromoteCommand();
        FDemoteCommand demoteCommand = new FDemoteCommand();
        FFreezeCommand freezeCommand = new FFreezeCommand();
        FSetDTRCommand setDTRCommand = new FSetDTRCommand();
        FSaveCommand saveCommand = new FSaveCommand();

        factionCommands.add(createCommand);
        factionCommands.add(createServerFactionCommand);
        factionCommands.add(disbandCommand);
        factionCommands.add(disbandOtherCommand);
        factionCommands.add(showCommand);
        factionCommands.add(claimCommand);
        factionCommands.add(claimOtherCommand);
        factionCommands.add(inviteCommand);
        factionCommands.add(uninviteCommand);
        factionCommands.add(mapCommand);
        factionCommands.add(acceptCommand);
        factionCommands.add(subclaimCommand);
        factionCommands.add(renameCommand);
        factionCommands.add(renameForCommand);
        factionCommands.add(sethomeCommand);
        factionCommands.add(sethomeForCommand);
        factionCommands.add(thawCommand);
        factionCommands.add(unclaimCommand);
        factionCommands.add(unclaimForCommand);
        factionCommands.add(announcementCommand);
        factionCommands.add(listCommand);
        factionCommands.add(chatCommand);
        factionCommands.add(leaderCommand);
        factionCommands.add(promoteCommand);
        factionCommands.add(demoteCommand);
        factionCommands.add(freezeCommand);
        factionCommands.add(setDTRCommand);
        factionCommands.add(saveCommand);

        CCreateCommand chatCreateCommand = new CCreateCommand();
        CDeleteCommand chatDeleteCommand = new CDeleteCommand();

        chatCommands.add(chatCreateCommand);
        chatCommands.add(chatDeleteCommand);

        Logger.log("Loaded " + factionCommands.size() + " Faction Commands");
        Logger.log("Loaded " + chatCommands.size() + " Chat Commands");
    }

}
