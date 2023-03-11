package com.eternalcode.check.command.implementation;

import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.NotificationAnnouncer;
import com.eternalcode.check.shared.position.PositionAdapter;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "check")
@Permission("eternalcheck.setlocation")
public class CheckSetLocationCommand {

    private final NotificationAnnouncer announcer;
    private final ConfigManager configManager;
    private final MessagesConfig messages;
    private final PluginConfig config;

    public CheckSetLocationCommand(NotificationAnnouncer announcer, ConfigManager configManager, MessagesConfig messages, PluginConfig config) {
        this.announcer = announcer;
        this.configManager = configManager;
        this.messages = messages;
        this.config = config;
    }

    @Execute(route = "setlocation")
    @Permission("eternalcheck.setlocation")
    void execute(Player player) {
        this.config.checkLocation = PositionAdapter.convert(player.getLocation().clone());

        this.configManager.save(this.config);

        this.announcer.sendAnnounce(player, this.messages.check.admin.locationSet);
    }

}
