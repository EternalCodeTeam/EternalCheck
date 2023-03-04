package com.eternalcode.check.command.implementation;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.shared.position.PositionAdapter;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import com.eternalcode.check.user.event.CheckedUserEndEvent;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

@Route(name = "check", aliases = "sprawdz")
@Permission("eternalcheck.check")
public class CheckEndCommand {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnoucer annoucer;
    private final EventCaller eventCaller;
    private final MessagesConfig messages;
    private final Server server;

    public CheckEndCommand(CheckedUserService checkedUserService, NotificationAnnoucer annoucer, EventCaller eventCaller, MessagesConfig messages, Server server) {
        this.checkedUserService = checkedUserService;
        this.annoucer = annoucer;
        this.eventCaller = eventCaller;
        this.messages = messages;
        this.server = server;
    }

    @Execute(route = "end", aliases = "koniec", required = 1)
    void execute(Player player, @Arg @Name("player") CheckedUser user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(PositionAdapter.convert(user.getLastPosition()));

        this.checkedUserService.unmarkChecked(user.getUniqueId());

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        this.annoucer.annouceMessage(player, this.messages.check.admin.end, formatter);

        for (Notification notification : this.messages.check.broadcast.endCheck) {
            this.annoucer.annouceMessageAll(notification, formatter);
        }

        this.eventCaller.callEvent(new CheckedUserEndEvent(user, player));
    }
}
