package com.eternalcode.check;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.util.ArrayList;

public final class CheckNotificationTask implements Runnable {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnoucer announcer;
    private final MessagesConfig messages;
    private final Server server;

    public CheckNotificationTask(CheckedUserService checkedUserService, NotificationAnnoucer announcer, MessagesConfig messages, Server server) {
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
    }

    @Override
    public void run() {
        for (CheckedUser user : new ArrayList<>(this.checkedUserService.users())) {
            Player player = this.server.getPlayer(user.getUniqueId());

            if (player == null) {
                continue;
            }

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getChecker());

            for (Notification notification : this.messages.check.taskMessages) {
                this.announcer.annouceMessage(player, notification, formatter);
            }
        }
    }
}