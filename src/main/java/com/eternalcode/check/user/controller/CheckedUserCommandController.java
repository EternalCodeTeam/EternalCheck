package com.eternalcode.check.user.controller;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.NotificationAnnoucer;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class CheckedUserCommandController implements Listener {

    private final CheckedUserService checkedUserService;
    private final NotificationAnnoucer announcer;
    private final MessagesConfig messages;
    private final PluginConfig config;

    public CheckedUserCommandController(CheckedUserService checkedUserService, NotificationAnnoucer announcer, MessagesConfig messages, PluginConfig config) {
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;
        this.messages = messages;
        this.config = config;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (this.config.settings.canUseCommand) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        String command = event.getMessage().split(" ")[0];

        this.checkedUserService.find(uniqueId).ifPresent(user -> {
            for (String allowedCommand : this.config.settings.availableCommands) {
                if (command.startsWith(allowedCommand)) {
                    return;
                }

                event.setCancelled(true);
                this.announcer.annouceMessage(player, this.messages.argument.cantUseCommand);
            }
        });
    }
}
