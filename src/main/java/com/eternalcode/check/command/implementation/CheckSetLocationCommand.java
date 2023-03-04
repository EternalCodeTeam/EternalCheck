package com.eternalcode.check.command.implementation;

import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.shared.position.PositionAdapter;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "check", aliases = "sprawdz")
@Permission("eternalcheck.setlocation")
public class CheckSetLocationCommand {

    private final NotificationAnnoucer annoucer;
    private final ConfigManager configManager;
    private final MessagesConfig messages;
    private final PluginConfig config;

    public CheckSetLocationCommand(NotificationAnnoucer annoucer, ConfigManager configManager, MessagesConfig messages, PluginConfig config) {
        this.annoucer = annoucer;
        this.configManager = configManager;
        this.messages = messages;
        this.config = config;
    }

    @Execute(route = "setlocation", aliases = "ustaw")
    @Permission("eternalcheck.setlocation")
    void execute(Player player) {
        this.config.checkLocation = PositionAdapter.convert(player.getLocation().clone());

        this.configManager.save(this.config);

        this.annoucer.annouceMessage(player, this.messages.check.admin.locationSet);
    }

}
