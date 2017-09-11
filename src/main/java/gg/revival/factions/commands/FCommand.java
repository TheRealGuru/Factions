package gg.revival.factions.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FCommand {

    @Getter String label;
    @Getter String syntax;
    @Getter String description;
    @Getter String permission;
    @Getter CmdCategory category;

    @Getter List<String> aliases;

    @Getter int minArgs;
    @Getter int maxArgs;

    @Getter boolean playerOnly;

    public FCommand(String label, List<String> aliases, String syntax, String description, String permission, CmdCategory category, int minArgs, int maxArgs, boolean playerOnly) {
        this.label = label;
        this.aliases = aliases;
        this.syntax = syntax;
        this.description = description;
        this.permission = permission;
        this.category = category;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.playerOnly = playerOnly;
    }

    public void onCommand(CommandSender sender, String args[]) {
    }
}
