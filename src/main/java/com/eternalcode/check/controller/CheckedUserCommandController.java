package com.eternalcode.check.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class CheckedUserCommandController extends AbstractController {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (this.config.settings.canUseCommand) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        String command = event.getMessage().split(" ")[0];

        this.checkedUserService.find(uniqueId).ifPresent(user -> {
            if (this.config.settings.availableCommands.contains(command)) {
                return;
            }

            event.setCancelled(true);
            this.notificationAnnouncer.annouceMessage(uniqueId, this.messages.argument.cantUseCommand);

        });
    }
}
