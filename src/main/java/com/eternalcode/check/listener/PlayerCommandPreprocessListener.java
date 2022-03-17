package com.eternalcode.check.listener;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.UserManager;
import com.eternalcode.check.util.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class PlayerCommandPreprocessListener implements Listener {

    private final MessagesConfiguration messages;
    private final PluginConfiguration config;
    private final UserManager userManager;

    public PlayerCommandPreprocessListener(MessagesConfiguration messages, PluginConfiguration config, UserManager userManager) {
        this.messages = messages;
        this.config = config;
        this.userManager = userManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        String command = event.getMessage().split(" ")[0];

        if (this.config.settings.cantUseCommand) {
            this.userManager.find(uniqueId).peek(user -> {
                if (!this.config.settings.availableCommands.contains(command)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatUtils.colour(this.messages.check.arguments.cantUseCommand));
                }
            });
        }
    }
}
