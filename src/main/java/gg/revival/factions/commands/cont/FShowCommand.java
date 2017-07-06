package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.FCommand;

import java.util.Arrays;

public class FShowCommand extends FCommand {

    public FShowCommand() {
        super(
                "show",
                Arrays.asList("who", "info"),
                "/f show <name>",
                "View a factions information",
                null,
                1,
                3,
                true
        );
    }
}
