package com.eternalcode.check.controller;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class CheckedUserMoveController implements Listener {

    private final PluginConfig config;
    private final CheckedUserService checkedUserService;

    public CheckedUserMoveController(PluginConfig config, CheckedUserService checkedUserService) {
        this.config = config;
        this.checkedUserService = checkedUserService;
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