package com.eternalcode.check.command.handler;

import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.command.CommandSender;

public class NotificationHandler implements Handler<CommandSender, Notification> {

    private final NotificationAnnouncer announcer;

    public NotificationHandler(NotificationAnnouncer announcer) {
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Notification value) {
        this.announcer.sendAnnounce(sender, value);
    }
}
