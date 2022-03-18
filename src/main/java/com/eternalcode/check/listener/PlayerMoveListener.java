package com.eternalcode.check.listener;

import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final PluginConfiguration config;
    private final UserManager userManager;

    public PlayerMoveListener(PluginConfiguration config, UserManager userManager) {
        this.config = config;
        this.userManager = userManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (this.config.settings.cantMove) {
            this.userManager.find(uniqueId).peek(user -> event.setCancelled(true));
        }
    }

}
