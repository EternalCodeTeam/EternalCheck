package com.eternalcode.check.listener;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.UserService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final MessagesConfig messages;
    private final PluginConfig config;
    private final UserService userService;
    private final Server server;
    private final NotificationAnnouncer announcer;

    public PlayerQuitListener(MessagesConfig messages, PluginConfig config, UserService userService, Server server, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.config = config;
        this.userService = userService;
        this.server = server;
        this.announcer = announcer;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.userService.find(uniqueId).ifPresent(user -> {

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", player.getName());

            for (Player all : this.server.getOnlinePlayers()) {
                this.messages.check.broadcast.logoutCheck.forEach(message -> this.announcer.annouceMessage(all.getUniqueId(), formatter.format(message)));
            }

            this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.logout, "{PLAYER}", player.getName()));

            this.userService.remove(uniqueId);
        });
    }
}