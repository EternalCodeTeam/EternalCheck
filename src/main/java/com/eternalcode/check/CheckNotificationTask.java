package com.eternalcode.check;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

public final class CheckNotificationTask implements Runnable {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final Server server;

    public CheckNotificationTask(CheckedUserService checkedUserService, NotificationAnnouncer announcer, MessagesConfig messages, Server server) {
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
    }

    @Override
    public void run() {
        for (CheckedUser user : this.checkedUserService.checkedUsers()) {
            Player player = this.server.getPlayer(user.getUniqueId());

            if (player == null) {
                continue;
            }

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getChecker());

            for (Notification notification : this.messages.check.taskMessages) {
                this.announcer.sendAnnounce(player, notification, formatter);
            }
        }
    }
}