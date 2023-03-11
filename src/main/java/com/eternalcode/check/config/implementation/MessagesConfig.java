package com.eternalcode.check.config.implementation;

import com.eternalcode.check.config.ReloadableConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationType;
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

    @Description({
            "# ",
            "# EternalCheck - A simple plugin for checking suspicious players.",
            "# ",
            "# Discord: https://discord.gg/TBgbN3fruj",
            "# Website: https://eternalcode.pl/",
            "#",
            "# Notification types:",
            "# CHAT, ACTIONBAR, TITLE, SUBTITLE, NONE",
            "#",
            "# Usage: [CHAT, ACTIONBAR]test",
            "# in this case, the message will be sent to the player's on chat and actionbar!",
            "# ",
            "# Usage: [NONE]test",
            "# in this case, the message wont be sent!",
            "# ",
            "# We are supporting MiniMessage: https://docs.adventure.kyori.net/minimessage.html",
            " "
    })
    public Argument argument = new Argument();

    @Description(" ")
    public Check check = new Check();

    @Contextual
    public static class Argument {
        public Notification permission = Notification.chat("&cYou don't have permission! &7({PERMISSIONS})");

        @Description(" ")
        public Notification correctUsage = Notification.chat("&eCorrect usage: &7{USAGE}");

        @Description(" ")
        public Notification correctUsageList = Notification.chat("&7{USAGE}");

        @Description(" ")
        public Notification offlinePlayer = Notification.chat("&cThat player is offline!");

        @Description(" ")
        public Notification roomNotSet = Notification.chat("&cUse &e/check setlocation &cto set check location!");

        @Description(" ")
        public Notification notChecking = Notification.chat("&cThis player wasn't checking");

        @Description(" ")
        public Notification isChecking = Notification.chat("&cThis player is checking now!");

        @Description(" ")
        public Notification youArentChecked = Notification.chat("&cYou aren't being checked right now!");

        @Description(" ")
        public Notification cantUseCommand = Notification.chat("&cYou are being checked! You can use only: &e/helpop, &e/admit");

        @Description(" ")
        public Notification bypass = Notification.chat("&cThis player have bypass permission!");
    }

    @Contextual
    public static class Check {

        @Description("# Available variables: {PLAYER}, {ADMIN}")

        public List<Notification> startMessages = Arrays.asList(
                Notification.chat("&cYou are checking by: {ADMIN}"),
                Notification.title("&c{PLAYER}, You are checking now!"),
                Notification.subtitle("&7By: {ADMIN}!")
        );

        @Description("# Available variables: {PLAYER}, {ADMIN}")

        public List<Notification> taskMessages = Arrays.asList(
                Notification.chat("&cYou are checking by: {ADMIN}"),
                Notification.actionbar("&cYou are checking by: {ADMIN}"),
                Notification.title("&cYou are checking!"),
                Notification.subtitle("&7More info on chat!")
        );

        @Description(" ")
        public Admin admin = new Admin();

        @Description(" ")
        public Broadcast broadcast = new Broadcast();

        @Contextual
        public static class Admin {

            @Description(" ")
            public Notification start = Notification.chat("&aYou started checking {PLAYER}");

            @Description(" ")
            public Notification end = Notification.chat("&aYou ended checking {PLAYER}");

            @Description(" ")
            public Notification ban = Notification.chat("&aYou banned {PLAYER} for cheats");

            @Description(" ")
            public Notification locationSet = Notification.chat("&aLocation check set!");

            @Description(" ")
            public Notification reload = Notification.chat("&aConfiguration reloaded!");

        }

        @Contextual
        public static class Broadcast {
            @Description("# Available variables: {PLAYER}, {ADMIN}")
            public List<Notification> admit = Collections.singletonList(Notification.chat("&cPlayer &e{PLAYER} &cconfessed to cheats!"));

            @Description(" ")
            public List<Notification> startCheck = Collections.singletonList(Notification.chat("&cPlayer &e{PLAYER} &cis checking by &e{ADMIN}&c!"));

            @Description(" ")
            public List<Notification> endCheck = Collections.singletonList(Notification.chat("&cPlayer &e{PLAYER} &cwas checked by &e{ADMIN}&c!"));

            @Description(" ")
            public List<Notification> banCheck = Collections.singletonList(Notification.chat("&cPlayer &e{PLAYER} &cwas checked and banned by &e{ADMIN}&c!"));

            @Description(" ")
            public List<Notification> logoutCheck = Collections.singletonList(Notification.chat("&cPlayer &e{PLAYER} &clogged out while checking!"));
        }
    }
}
