package com.eternalcode.check.controller;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.UserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class CheckedPlayerMoveController implements Listener {

    private final PluginConfig config;
    private final UserService userService;

    public CheckedPlayerMoveController(PluginConfig config, UserService userService) {
        this.config = config;
        this.userService = userService;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (this.config.settings.canMove) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.userService.find(uniqueId).ifPresent(user -> event.setCancelled(true));
    }

}