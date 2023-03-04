package com.eternalcode.check.notification;

import com.eternalcode.check.config.implementation.PluginConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

public class NotificationAnnoucer {

    private static final Formatter EMPTY_FORMATTER = new Formatter();

    private final PluginConfig config;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;
    private Title.Times titleTimes;
    private final Server server;

    public NotificationAnnoucer(PluginConfig config, AudienceProvider audienceProvider, MiniMessage miniMessage, Server server) {
        this.audienceProvider = audienceProvider;
        this.config = config;
        this.miniMessage = miniMessage;
        this.server = server;
    }

    public void annouceMessage(CommandSender sender, Notification notification) {
        this.annouceMessage(sender, notification, EMPTY_FORMATTER);
    }

    public void annouceMessage(CommandSender sender, Notification notification, Formatter formatter) {
        Audience audience = this.audience(sender);

        String message = formatter.format(notification.message());

        for (NotificationType notificationType : notification.types()) {
            switch (notificationType) {
                case TITLE: {
                    Title title = Title.title(this.miniMessage.deserialize(message), Component.empty(), this.times());

                    audience.showTitle(title);

                    break;
                }

                case SUBTITLE: {
                    Title title = Title.title(Component.empty(), this.miniMessage.deserialize(message), this.times());

                    audience.showTitle(title);

                    break;
                }

                case ACTIONBAR: {
                    audience.sendActionBar(this.miniMessage.deserialize(message));

                    break;
                }

                case CHAT: {
                    audience.sendMessage(this.miniMessage.deserialize(message));

                    break;
                }

                case NONE: {
                    break;
                }
            }
        }

    }

    public void annouceMessageAll(Notification notification, Formatter formatter) {
        this.server.getOnlinePlayers().forEach(player -> this.annouceMessage(player, notification, formatter));
    }

    private Audience audience(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            return this.audienceProvider.player(player.getUniqueId());
        }

        return this.audienceProvider.console();
    }

    private Title.Times times() {
        if (this.titleTimes == null) {
            this.titleTimes = Title.Times.times(
                    this.config.settings.title.fadeIn,
                    this.config.settings.title.stay,
                    this.config.settings.title.fadeOut
            );
        }

        return this.titleTimes;
    }
}
