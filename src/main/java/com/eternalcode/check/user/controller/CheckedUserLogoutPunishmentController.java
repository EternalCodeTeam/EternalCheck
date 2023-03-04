package com.eternalcode.check.user.controller;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.user.CheckedUserService;
import com.eternalcode.check.user.event.CheckedUserLogoutEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

import java.util.UUID;

public class CheckedUserLogoutPunishmentController implements Listener {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnoucer announcer;
    private final EventCaller eventCaller;
    private final MessagesConfig messages;
    private final PluginConfig config;
    private final Server server;

    public CheckedUserLogoutPunishmentController(CheckedUserService checkedUserService, NotificationAnnoucer announcer, MessagesConfig messages, PluginConfig config, EventCaller eventCaller, Server server) {
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;
        this.messages = messages;
        this.config = config;
        this.eventCaller = eventCaller;
        this.server = server;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.checkedUserService.find(uniqueId).ifPresent(user -> {
            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", player.getName());

            for (Notification notification : this.messages.check.broadcast.logoutCheck) {
                this.announcer.annouceMessage(player, notification, formatter);
            }

            this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.logout, "{PLAYER}", player.getName()));

            this.checkedUserService.unmarkChecked(uniqueId);

            Player admin = this.server.getPlayer(user.getChecker());

            this.eventCaller.callEvent(new CheckedUserLogoutEvent(user, admin));
        });
    }
}