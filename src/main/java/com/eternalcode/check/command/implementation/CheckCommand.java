package com.eternalcode.check.command.implementation;

import com.eternalcode.check.command.argument.PlayerArgument;
import com.eternalcode.check.command.argument.UserArgument;
import com.eternalcode.check.configuration.ConfigurationManager;
import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserManager;
import com.eternalcode.check.util.ChatUtils;
import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.MinArgs;
import dev.rollczi.litecommands.annotations.Permission;
import dev.rollczi.litecommands.annotations.PermissionExclude;
import dev.rollczi.litecommands.annotations.Section;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

@Section(route = "check", aliases = "sprawdz")
@Permission("eternalcheck.check")
public class CheckCommand {

    private final ConfigurationManager configurationManager;
    private final BukkitAudiences bukkitAudiences;
    private final MessagesConfiguration messages;
    private final PluginConfiguration config;
    private final UserManager userManager;
    private final Server server;

    public CheckCommand(ConfigurationManager configurationManager, BukkitAudiences bukkitAudiences, MessagesConfiguration messages, PluginConfiguration config, UserManager userManager, Server server) {
        this.configurationManager = configurationManager;
        this.bukkitAudiences = bukkitAudiences;
        this.messages = messages;
        this.config = config;
        this.userManager = userManager;
        this.server = server;
    }

    @Execute
    public void execute(Player player) {
        this.messages.check.arguments.help.forEach(message -> player.sendMessage(ChatUtils.colour(message)));
    }

    @Execute(route = "set", aliases = "ustaw")
    @PermissionExclude("eternalcheck.check")
    @Permission("eternalcheck.check.set")
    public void executeSet(Player player) {
        this.config.checkLocation = player.getLocation().clone();

        this.configurationManager.render(this.config);

        player.sendMessage(ChatUtils.colour(this.messages.check.setLocation));
    }

    @Execute(route = "start", aliases = "rozpocznij")
    @MinArgs(1)
    public void executeStart(Player player, @Arg(0) @Handler(PlayerArgument.class) Player playerArgument) {
        if (playerArgument.hasPermission("etrlCheck.bypass")) {
            player.sendMessage(ChatUtils.colour(this.messages.check.arguments.bypass));
            return;
        }

        if (!this.userManager.find(playerArgument.getUniqueId()).isEmpty()) {
            player.sendMessage(ChatUtils.colour(this.messages.check.arguments.isChecked));
            return;
        }

        if (this.config.checkLocation.getWorld() == null || this.config.checkLocation == null) {
            player.sendMessage(ChatUtils.colour(this.messages.check.arguments.roomNotSet));
            return;
        }

        this.userManager.create(playerArgument.getUniqueId(), playerArgument.getName(), player.getName(), playerArgument.getLocation().clone());

        player.teleport(this.config.checkLocation);
        playerArgument.teleport(this.config.checkLocation);

        Formatter formatter = new Formatter()
                .register("{PLAYER}", playerArgument.getName())
                .register("{ADMIN}", player.getName());

        player.sendMessage(ChatUtils.colour(formatter.format(this.messages.check.admin.start)));

        this.messages.check.broadcast.startCheck.forEach(message -> this.server.broadcastMessage(ChatUtils.colour(formatter.format(message))));

        this.messages.check.player.startMessage.forEach(message -> playerArgument.sendMessage(ChatUtils.colour(formatter.format(message))));

        if (this.config.settings.title.onStart) {
            Audience audience = this.bukkitAudiences.player(playerArgument);

            ChatUtils.sendTitle(audience, formatter.format(this.messages.check.player.startTitle),
                    formatter.format(this.messages.check.player.startSubTitle),
                    this.config.settings.title.fadeIn,
                    this.config.settings.title.stay,
                    this.config.settings.title.fadeOut
            );
        }
    }

    @Execute(route = "end", aliases = "koniec")
    @MinArgs(1)
    public void executeEnd(Player player, @Arg(0) @Handler(UserArgument.class) User user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(user.getLastLocation());
        this.userManager.remove(user.getUniqueId());

        player.sendMessage(ChatUtils.colour(StringUtils.replace(this.messages.check.admin.end, "{PLAYER}", user.getName())));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        this.messages.check.broadcast.endCheck.forEach(message -> this.server.broadcastMessage(ChatUtils.colour(formatter.format(message))));
    }

    @Execute(route = "ban", aliases = "zbanuj")
    @MinArgs(1)
    public void executeBan(Player player, @Arg(0) @Handler(UserArgument.class) User user) {
        Player playerArgument = this.server.getPlayer(user.getUniqueId());

        playerArgument.teleport(user.getLastLocation());
        this.userManager.remove(user.getUniqueId());

        player.sendMessage(ChatUtils.colour(StringUtils.replace(this.messages.check.admin.ban, "{PLAYER}", user.getName())));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", player.getName());

        this.messages.check.broadcast.banCheck.forEach(message -> this.server.broadcastMessage(ChatUtils.colour(formatter.format(message))));

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.ban, "{PLAYER}", user.getName()));
    }
}
