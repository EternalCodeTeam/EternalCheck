package com.eternalcode.check.command.handler;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;
import panda.utilities.text.Joiner;

public class PermissionHandler implements Handler<CommandSender, RequiredPermissions> {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    public PermissionHandler(NotificationAnnouncer announcer, MessagesConfig messages) {
        this.announcer = announcer;
        this.messages = messages;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        Formatter formatter = new Formatter()
                .register("{PERMISSION}", Joiner.on(", ")
                        .join(requiredPermissions.getPermissions())
                        .toString());

        this.announcer.sendAnnounce(commandSender, this.messages.argument.permission, formatter);
    }

}
