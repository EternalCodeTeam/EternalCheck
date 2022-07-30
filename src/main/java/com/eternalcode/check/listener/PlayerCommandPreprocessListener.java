package com.eternalcode.check.listener;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.UserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class PlayerCommandPreprocessListener implements Listener {

    private final MessagesConfig messages;
    private final PluginConfig config;
    private final UserService userService;
    private final NotificationAnnouncer announcer;


    public PlayerCommandPreprocessListener(MessagesConfig messages, PluginConfig config, UserService userService, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.config = config;
        this.userService = userService;
        this.announcer = announcer;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (this.config.settings.canUseCommand) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        String command = event.getMessage().split(" ")[0];

        this.userService.find(uniqueId).ifPresent(user -> {
            if (this.config.settings.availableCommands.contains(command)) {
                return;
            }

            event.setCancelled(true);
            this.announcer.annouceMessage(uniqueId, this.messages.argument.cantUseCommand);

        });
    }
}
