package com.eternalcode.check.command.handler;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

public class InvalidUsageHandler implements Handler<CommandSender, Schematic> {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messagesConfig;

    public InvalidUsageHandler(MessagesConfig messagesConfig, NotificationAnnouncer announcer) {
        this.messagesConfig = messagesConfig;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, Schematic scheme) {
        if (scheme.isOnlyFirst()) {
            Formatter formatter = new Formatter().register("{USAGE}", scheme.first());

            this.announcer.sendAnnounce(commandSender, this.messagesConfig.argument.correctUsage, formatter);

            return;
        }

        for (String schemematic : scheme.getSchematics()) {
            Formatter formatter = new Formatter().register("{USAGE}", schemematic);

            this.announcer.sendAnnounce(commandSender, this.messagesConfig.argument.correctUsageList, formatter);
        }
    }
}
