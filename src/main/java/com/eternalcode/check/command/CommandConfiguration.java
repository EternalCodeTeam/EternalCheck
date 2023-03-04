package com.eternalcode.check.command;

import com.eternalcode.check.config.ReloadableConfig;
import com.google.common.collect.ImmutableMap;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandConfiguration implements ReloadableConfig {

    @Description("# Set name of command, argument names and permissions:")
    public Map<String, Command> commands = ImmutableMap.of(
            "admit",
            new Command(
                    "admit",
                    Arrays.asList("przyznajsie", "ff"),
                    new ArrayList<>()
            ),

            "check",
            new Command(
                    "check",

                    Collections.singletonList("sprawdz"),

                    Collections.singletonList("eternalcheck.check"),

                    Arrays.asList(
                            new SubCommand("start", "rozpocznij", Collections.singletonList("eternalcheck.check")),
                            new SubCommand("end", "koniec", Collections.singletonList("eternalcheck.check")),
                            new SubCommand("ban", "zbanuj", Collections.singletonList("eternalcheck.check")),
                            new SubCommand("setlocation", "ustawmiejsce", Collections.singletonList("eternalcheck.setlocation")),
                            new SubCommand("reload", "przeladuj", Collections.singletonList("eternalcheck.reload"))
                    )
            )
    );

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "commands.yml");
    }

    @Contextual
    public static class Command {

        public String name;
        public List<String> aliases;
        public List<String> permissions;
        public List<SubCommand> subCommands = new ArrayList<>();

        public Command(String name, List<String> aliases, List<String> permissions, List<SubCommand> subCommands) {
            this.name = name;
            this.aliases = aliases;
            this.permissions = permissions;
            this.subCommands = subCommands;
        }

        public Command(String name, List<String> aliases, List<String> permissions) {
            this.name = name;
            this.aliases = aliases;
            this.permissions = permissions;
        }

        public Command() {

        }
    }

    @Contextual
    public static class SubCommand {

        public String name;
        public String alias;
        public List<String> permissions;

        public SubCommand(String name, String alias) {
            this.name = name;
            this.alias = alias;
        }

        public SubCommand(String name, String alias, List<String> permissions) {
            this.name = name;
            this.alias = alias;
            this.permissions = permissions;
        }

        public SubCommand() {

        }
    }
}
