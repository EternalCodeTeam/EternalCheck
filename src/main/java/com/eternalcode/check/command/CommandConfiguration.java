package com.eternalcode.check.command;

import com.eternalcode.check.command.configurer.Command;
import com.eternalcode.check.command.configurer.SubCommand;
import com.eternalcode.check.config.ReloadableConfig;
import com.google.common.collect.ImmutableMap;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
}
