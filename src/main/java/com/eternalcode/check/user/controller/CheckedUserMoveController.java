package com.eternalcode.check.user.controller;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class CheckedUserMoveController implements Listener {

    private final CheckedUserService checkedUserService;
    private final PluginConfig config;

    public CheckedUserMoveController(CheckedUserService checkedUserService, PluginConfig config) {
        this.checkedUserService = checkedUserService;
        this.config = config;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (this.config.settings.canMove) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.checkedUserService.find(uniqueId).ifPresent(user -> event.setCancelled(true));
    }

}