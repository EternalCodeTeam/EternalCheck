package com.eternalcode.check.command.message;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Joiner;

public class PermissionMessage implements PermissionHandler<CommandSender> {

    private final MessagesConfig messages;
    private final NotificationAnnouncer announcer;

    public PermissionMessage(MessagesConfig messages, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, LitePermissions litePermissions) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            this.announcer.annouceMessage(player.getUniqueId(), this.messages.argument.permission.replace("{PERMISSION}", Joiner.on(", ")
                    .join(litePermissions.getPermissions())
                    .toString()));
        }
    }
}
