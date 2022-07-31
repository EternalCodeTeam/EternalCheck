package com.eternalcode.check.config.implementation;

import com.eternalcode.check.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MessagesConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    @Description({ "# ",
            "# EternalCheck - A simple plugin for checking suspicious players.",
            "# ",
            "# Discord: https://dc.eternalcode.pl/",
            "# Website: https://eternalcode.pl/", " " })

    public Argument argument = new Argument();

    @Description(" ")
    public Check check = new Check();

    @Contextual
    public static class Argument {
        public String permission = "&cYou don't have permission! &7({PERMISSIONS})";

        @Description(" ")
        public String correctUsage = "&eCorrect usage: &7{USAGE}";

        @Description(" ")
        public String offlinePlayer = "&cThat player is offline!";

        @Description(" ")
        public String roomNotSet = "&cUse &e/check set &cto set check location!";

        @Description(" ")
        public String notChecking = "&cThis player wasn't checking";

        @Description(" ")
        public String isChecking = "&cThis player is checking now!";

        @Description(" ")
        public String youArentChecked = "&cYou aren't being checked right now!";

        @Description(" ")
        public String cantUseCommand = "&cYou are being checked! You can use only: &e/helpop, &e/admit";

        @Description(" ")
        public String bypass = "&cYou don't have permission to check this player!";

        @Description(" ")
        public List<String> help = Arrays.asList(
                "&8• &7/check start <player> - start of check",
                "&8• &7/check end <player> - end of check",
                "&8• &7/check ban <player> - ban player for cheats",
                "&8• &7/check set - sets check location"
        );
    }

    @Contextual
    public static class Check {

        @Description(" ")
        public String adminStart = "&aYou started checking {PLAYER}";

        @Description(" ")
        public String adminEnd = "&aYou ended checking {PLAYER}";

        @Description(" ")
        public String adminBan = "&aYou banned {PLAYER} for cheats";

        @Description(" ")
        public String adminLocationSet = "&aLocation check set!";

        @Description(" ")
        public String adminReload = "&aConfiguration reloaded!";

        @Description(" ")
        public Broadcast broadcast = new Broadcast();

        @Description(" ")
        public Start start = new Start();

        @Description(" ")
        public Task task = new Task();

        @Contextual
        public static class Start {

            @Description("# Available variables: {PLAYER}, {ADMIN}")

            public String startTitle = "&c{PLAYER}, You are checking now!";

            public String startSubTitle = "&7By: {ADMIN}!";

            public List<String> startMessage = Collections.singletonList("&cYou are checking by: {ADMIN}");
        }

        @Contextual
        public static class Task {

            @Description("# Available variables: {PLAYER}, {ADMIN}")

            public String actionBar = "&cYou are checking!";

            public String title = "&cYou are checking!";

            public String subTitle = "&7More info on chat!";

            public List<String> message = Collections.singletonList("&cYou are checking by: {ADMIN}");
        }


        @Contextual
        public static class Broadcast {
            @Description("# Available variables: {PLAYER}, {ADMIN}")
            public List<String> admit = Collections.singletonList("&cPlayer &e{PLAYER} &cconfessed to cheats!");

            @Description(" ")
            public List<String> startCheck = Collections.singletonList("&cPlayer &e{PLAYER} &cis checking by &e{ADMIN}&c!");

            @Description(" ")
            public List<String> endCheck = Collections.singletonList("&cPlayer &e{PLAYER} &cwas checked by &e{ADMIN}&c!");

            @Description(" ")
            public List<String> banCheck = Collections.singletonList("&cPlayer &e{PLAYER} &cwas checked and banned by &e{ADMIN}&c!");

            @Description(" ")
            public List<String> logoutCheck = Collections.singletonList("&cPlayer &e{PLAYER} &clogged out while checking!");
        }
    }
}
