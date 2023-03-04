package com.eternalcode.check.config.implementation;

import com.eternalcode.check.config.ReloadableConfig;
import com.eternalcode.check.shared.position.Position;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class PluginConfig implements ReloadableConfig {

    @Description({ "# ",
            "# EternalCheck - A simple plugin for checking suspicious players",
            "# ",
            "# Discord: https://dc.eternalcode.pl/",
            "# Website: https://discord.gg/NvZ6apa5sJ/", " " })

    @Description("# Settings")
    public Settings settings = new Settings();

    @Description({ "", "# Commands" })
    public Commands commands = new Commands();

    @Contextual
    public static class Settings {

        @Description("# Whether the player after entering the server should receive information about the new version of the plugin?")
        public boolean receiveUpdates = true;

        @Description(" ")
        public Runnable runnable = new Runnable();

        @Description(" ")
        public Title title = new Title();

        @Contextual
        public static class Runnable {
            @Description("# Whether the task should be enabled?")
            public boolean enabled = true;

            @Description({ " ", "# Task interval (in secounds)" })
            public int interval = 20;
        }

        @Contextual
        public static class Title {

            @Description("# Title values")
            public Duration stay = Duration.ofSeconds(1);
            public Duration fadeIn = Duration.ofSeconds(2);
            public Duration fadeOut = Duration.ofSeconds(1);
        }

        @Description({ " ", "# Whether the checked player is to see only the messages of the checking admin?" })
        public boolean onlyMessageFromAdmin = true;

        @Description({ " ", "# Whether the player is allowed to move while checking?" })
        public boolean canMove = true;

        @Description({ " ", "# Whether the player can use commands when checking?" })
        public boolean canUseCommand = true;

        @Description({ " ", "# Available commands when checking,",
                "# if canUseCommand is set to false" })
        public List<String> availableCommands = Arrays.asList("/admit", "/helpop", "/przyznajsie");
    }

    @Contextual
    public static class Commands {

        @Description("# Command executed with /admit")
        public String admit = "tempban {PLAYER} 3d Cheat admit!";

        @Description({ "", "# Command executed with Logout" })
        public String logout = "ban {PLAYER} Logout when checking!";

        @Description({ "", "# Command executed with /check ban" })
        public String ban = "tempban {PLAYER} 14d Having cheats!";
    }

    @Description({ " ", "# Check location - use \"/check setlocation\" in game!" })
    public Position checkLocation = new Position(0, 100, 0, 0.f, 0.f, "world");

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "config.yml");
    }
}
