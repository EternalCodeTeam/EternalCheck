package com.eternalcode.check.command.handler;

import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnoucer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.command.CommandSender;

public class NotificationHandler implements Handler<CommandSender, Notification> {

    private final NotificationAnnoucer annoucer;

    public NotificationHandler(NotificationAnnoucer annoucer) {
        this.annoucer = annoucer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Notification value) {
        this.annoucer.annouceMessage(sender, value);
    }
}
