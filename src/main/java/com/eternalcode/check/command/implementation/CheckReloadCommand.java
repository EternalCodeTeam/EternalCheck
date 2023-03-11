package com.eternalcode.check.command.implementation;

import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "check")
@Permission("eternalcheck.reload")
public class CheckReloadCommand {

    private final NotificationAnnouncer announcer;
    private final ConfigManager configManager;
    private final MessagesConfig messages;

    public CheckReloadCommand(NotificationAnnouncer announcer, ConfigManager configManager, MessagesConfig messages) {
        this.announcer = announcer;
        this.configManager = configManager;
        this.messages = messages;
    }

    @Execute(route = "reload")
    @Permission("eternalcheck.check.reload")
    void execute(Player player) {
        this.configManager.reload();

        this.announcer.sendAnnounce(player, this.messages.check.admin.reload);
    }
}
