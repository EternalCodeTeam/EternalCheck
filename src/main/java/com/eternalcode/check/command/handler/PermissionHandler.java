package com.eternalcode.check.command.handler;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnoucer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;
import panda.utilities.text.Joiner;

public class PermissionHandler implements dev.rollczi.litecommands.handle.PermissionHandler<CommandSender> {

    private final MessagesConfig messages;
    private final NotificationAnnoucer announcer;

    public PermissionHandler(MessagesConfig messages, NotificationAnnoucer announcer) {
        this.messages = messages;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        Formatter formatter = new Formatter()
                .register("{PERMISSION}", Joiner.on(", ")
                        .join(requiredPermissions.getPermissions())
                        .toString());

        this.announcer.annouceMessage(commandSender, this.messages.argument.permission, formatter);
    }

}
