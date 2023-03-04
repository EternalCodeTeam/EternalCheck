package com.eternalcode.check.command.implementation;

import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.NotificationAnnoucer;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "check", aliases = "sprawdz")
@Permission("eternalcheck.reload")
public class CheckReloadCommand {

    private final NotificationAnnoucer annoucer;
    private final ConfigManager configManager;
    private final MessagesConfig messages;

    public CheckReloadCommand(NotificationAnnoucer annoucer, ConfigManager configManager, MessagesConfig messages) {
        this.annoucer = annoucer;
        this.configManager = configManager;
        this.messages = messages;
    }

    @Execute(route = "reload", aliases = "rl")
    @Permission("eternalcheck.check.reload")
    void execute(Player player) {
        this.configManager.reload();

        this.annoucer.annouceMessage(player, this.messages.check.admin.reload);
    }
}
