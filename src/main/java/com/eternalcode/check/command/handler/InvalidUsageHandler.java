package com.eternalcode.check.command.handler;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnoucer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

public class InvalidUsageHandler implements dev.rollczi.litecommands.handle.InvalidUsageHandler<CommandSender> {

    private final NotificationAnnoucer announcer;
    private final MessagesConfig messagesConfig;

    public InvalidUsageHandler(MessagesConfig messagesConfig, NotificationAnnoucer announcer) {
        this.messagesConfig = messagesConfig;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, Schematic scheme) {
        if (scheme.isOnlyFirst()) {
            this.announcer.annouceMessage(commandSender, this.messagesConfig.argument.correctUsage, new Formatter()
                    .register("{USAGE}", scheme.first()));

            return;
        }

        for (String schemematic : scheme.getSchematics()) {
            this.announcer.annouceMessage(commandSender, this.messagesConfig.argument.correctUsageList, new Formatter().register("{USAGE}", schemematic));
        }
    }
}
