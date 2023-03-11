package com.eternalcode.check.command.implementation;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.CheckedUserDataConfig;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import com.eternalcode.check.shared.position.PositionAdapter;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import com.eternalcode.check.user.event.CheckedUserStartEvent;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

@Route(name = "check")
@Permission("eternalcheck.check")
public class CheckStartCommand {

    private final CheckedUserDataConfig checkedUserDataConfig;
    private final CheckedUserService checkedUserService;
    private final NotificationAnnouncer announcer;
    private final ConfigManager configManager;
    private final MessagesConfig messages;
    private final EventCaller eventCaller;
    private final PluginConfig config;

    public CheckStartCommand(CheckedUserDataConfig checkedUserDataConfig, CheckedUserService checkedUserService, NotificationAnnouncer announcer, ConfigManager configManager, MessagesConfig messages, EventCaller eventCaller, PluginConfig config) {
        this.checkedUserDataConfig = checkedUserDataConfig;
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;
        this.configManager = configManager;
        this.messages = messages;
        this.eventCaller = eventCaller;
        this.config = config;
    }

    @Execute(route = "start", required = 1)
    void execute(Player player, @Arg @Name("player") Player playerArgument) {
        if (playerArgument.hasPermission("eternalcheck.bypass")) {
            this.announcer.sendAnnounce(player, this.messages.argument.bypass);

            return;
        }

        if (this.checkedUserService.find(playerArgument.getUniqueId()).isPresent()) {
            this.announcer.sendAnnounce(player, this.messages.argument.isChecking);

            return;
        }

        if (this.config.checkLocation.getWorld() == null || this.config.checkLocation == null) {
            this.announcer.sendAnnounce(player, this.messages.argument.roomNotSet);

            return;
        }

        CheckedUser checkedUser = this.checkedUserService.markChecked(
                playerArgument.getUniqueId(),
                playerArgument.getName(),
                player.getName(),
                PositionAdapter.convert(playerArgument.getLocation().clone())
        );

        this.checkedUserDataConfig.addCheckedUser();
        this.configManager.save(this.checkedUserDataConfig);

        player.teleport(PositionAdapter.convert(this.config.checkLocation));
        playerArgument.teleport(PositionAdapter.convert(this.config.checkLocation));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", playerArgument.getName())
                .register("{ADMIN}", player.getName());

        this.announcer.sendAnnounce(player, this.messages.check.admin.start, formatter);

        for (Notification notification : this.messages.check.startMessages) {
            this.announcer.sendAnnounce(playerArgument, notification, formatter);
        }

        for (Notification notification : this.messages.check.broadcast.startCheck) {
            this.announcer.sendAnnounceAll(notification, formatter);
        }

        this.eventCaller.callEvent(new CheckedUserStartEvent(checkedUser, player));
    }
}
