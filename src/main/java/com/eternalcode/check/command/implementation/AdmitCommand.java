package com.eternalcode.check.command.implementation;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import com.eternalcode.check.user.event.CheckedUserAdmitEvent;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

import java.util.Optional;

@Route(name = "admit")
public class AdmitCommand {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnouncer announcer;
    private final EventCaller eventCaller;
    private final MessagesConfig messages;
    private final PluginConfig config;
    private final Server server;

    public AdmitCommand(MessagesConfig messages, PluginConfig config, CheckedUserService checkedUserService, Server server, NotificationAnnouncer announcer, EventCaller eventCaller) {
        this.messages = messages;
        this.config = config;
        this.checkedUserService = checkedUserService;
        this.server = server;
        this.announcer = announcer;
        this.eventCaller = eventCaller;
    }

    @Execute
    public void execute(Player player) {
        Optional<CheckedUser> userOptional = this.checkedUserService.find(player.getUniqueId());

        if (!userOptional.isPresent()) {
            this.announcer.sendAnnounce(player, this.messages.argument.youArentChecked);

            return;
        }

        CheckedUser user = userOptional.get();

        this.checkedUserService.unmarkChecked(player.getUniqueId());

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.admit, "{PLAYER}", player.getName()));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", user.getChecker());

        for (Notification notification : this.messages.check.broadcast.admit) {
            this.announcer.sendAnnounceAll(notification, formatter);
        }

        Player admin = this.server.getPlayer(user.getChecker());

        this.eventCaller.callEvent(new CheckedUserAdmitEvent(user, admin));
    }
}