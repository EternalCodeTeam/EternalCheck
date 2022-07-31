package com.eternalcode.check.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class CheckedUserMoveController extends AbstractController {

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