package com.eternalcode.check.command.implementation;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.shared.position.PositionAdapter;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserService;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

@Section(route = "check", aliases = "sprawdz")
@Permission("EternalCheck.check")
public class CheckCommand {

    private final ConfigManager configManager;
    private final MessagesConfig messages;
    private final PluginConfig config;
    private final UserService userService;
    private final Server server;
    private final NotificationAnnouncer announcer;

    public CheckCommand(ConfigManager configManager, MessagesConfig messages, PluginConfig config, UserService userService, Server server, NotificationAnnouncer announcer) {
        this.configManager = configManager;
        this.messages = messages;
        this.config = config;
        this.userService = userService;
        this.server = server;
        this.announcer = announcer;
    }

    @Execute
    public void execute(Player player) {
        this.messages.argument.help.forEach(message -> this.announcer.annouceMessage(player.getUniqueId(), message));
    }

    @Execute(route = "set", aliases = "ustaw")
    @Permission("EternalCheck.check.set")
    public void executeSet(Player player) {
        this.config.checkLocation = PositionAdapter.convert(player.getLocation().clone());

        this.configManager.save(this.config);

        this.announcer.annouceMessage(player.getUniqueId(), this.messages.check.adminLocationSet);
    }

    @Execute(route = "reload", aliases = "rl")
    @Permission("EternalCheck.check.reload")
    public void executeReload(Player player) {
        this.configManager.reload();

        this.announcer.annouceMessage(player.getUniqueId(), this.messages.check.adminReload);
    }


    @Execute(route = "start", aliases = "rozpocznij", min = 1)
    public void executeStart(Player player, @Arg @By("player") @Name("player") Player playerArgument) {
        if (playerArgument.hasPermission("EternalCheck.bypass")) {
            this.announcer.annouceMessage(player.getUniqueId(), this.messages.argument.bypass);

            return;
        }

        if (this.userService.find(playerArgument.getUniqueId()).isPresent()) {
            this.announcer.annouceMessage(player.getUniqueId(), this.messages.argument.isChecking);

            return;
        }

        if (this.config.checkLocation.getWorld() == null || this.config.checkLocation == null) {
            this.announcer.annouceMessage(player.getUniqueId(), this.messages.argument.roomNotSet);

            return;
        }

        this.userService.create(playerArgument.getUniqueId(), playerArgument.getName(), player.getName(), PositionAdapter.convert(playerArgument.getLocation().clone()));

        player.teleport(PositionAdapter.convert(this.config.checkLocation));
        playerArgument.teleport(PositionAdapter.convert(this.config.checkLocation));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", playerArgument.getName())
                .register("{ADMIN}", player.getName());

        this.announcer.annouceMessage(player.getUniqueId(), formatter.format(this.messages.check.adminStart));

        for (Player all : this.server.getOnlinePlayers()) {
            this.messages.check.broadcast.startCheck.forEach(message -> this.announcer.annouceMessage(all.getUniqueId(), formatter.format(message)));
        }

        this.messages.check.start.startMessage.forEach(message -> this.announcer.annouceMessage(playerArgument.getUniqueId(), formatter.format(message)));

        if (this.config.settings.title.startTitleMessageEnabled) {
            this.announcer.annouceTitle(playerArgument.getUniqueId(), formatter.format(this.messages.check.start.startTitle),
                    formatter.format(this.messages.check.start.startSubTitle),
                    this.config.settings.title.fadeIn,
                    this.config.settings.title.stay,
                    this.config.settings.title.fadeOut
            );
        }
    }

    @Execute(route = "end", aliases = "koniec", min = 1)
    public void executeEnd(Player player, @Arg @By("user") @Name("player") User user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(PositionAdapter.convert(user.getLastPosition()));
        this.userService.remove(user.getUniqueId());

        this.announcer.annouceMessage(player.getUniqueId(), this.messages.check.adminEnd.replace("{PLAYER}", playerArgument.getName()));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        for (Player all : this.server.getOnlinePlayers()) {
            this.messages.check.broadcast.endCheck.forEach(message -> this.announcer.annouceMessage(all.getUniqueId(), formatter.format(message)));
        }
    }

    @Execute(route = "ban", aliases = "zbanuj", min = 1)
    public void executeBan(Player player, @Arg @By("user") @Name("player") User user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(PositionAdapter.convert(user.getLastPosition()));
        this.userService.remove(user.getUniqueId());

        this.announcer.annouceMessage(player.getUniqueId(), this.messages.check.adminBan.replace("{PLAYER}", playerArgument.getName()));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        for (Player all : this.server.getOnlinePlayers()) {
            this.messages.check.broadcast.banCheck.forEach(message -> this.announcer.annouceMessage(all.getUniqueId(), formatter.format(message)));
        }

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.ban, "{PLAYER}", user.getName()));
    }
}