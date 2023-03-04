package com.eternalcode.check.command.implementation;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.shared.position.PositionAdapter;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import com.eternalcode.check.user.event.CheckedUserBanEvent;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

@Route(name = "check", aliases = "sprawdz")
@Permission("eternalcheck.check")
public class CheckBanCommand {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnoucer annoucer;
    private final MessagesConfig messages;
    private final EventCaller eventCaller;
    private final PluginConfig config;
    private final Server server;

    public CheckBanCommand(CheckedUserService checkedUserService, NotificationAnnoucer annoucer, MessagesConfig messages, EventCaller eventCaller, PluginConfig config, Server server) {
        this.checkedUserService = checkedUserService;
        this.annoucer = annoucer;
        this.messages = messages;
        this.eventCaller = eventCaller;
        this.config = config;
        this.server = server;
    }

    @Execute(route = "ban", aliases = "zbanuj", required = 1)
    void execute(Player player, @Arg @Name("player") CheckedUser user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(PositionAdapter.convert(user.getLastPosition()));

        this.checkedUserService.unmarkChecked(user.getUniqueId());

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        this.annoucer.annouceMessage(player, this.messages.check.admin.ban, formatter);

        for (Notification notification : this.messages.check.broadcast.banCheck) {
            this.annoucer.annouceMessageAll(notification, formatter);
        }

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.ban, "{PLAYER}", user.getName()));

        this.eventCaller.callEvent(new CheckedUserBanEvent(user, player));
    }
}
