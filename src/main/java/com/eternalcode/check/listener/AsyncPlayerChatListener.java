package com.eternalcode.check.listener;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import panda.utilities.iterable.ArrayIterable;

import java.util.ArrayList;
import java.util.Optional;

public class AsyncPlayerChatListener implements Listener {

    private final PluginConfig config;
    private final UserService userService;

    public AsyncPlayerChatListener(PluginConfig config, UserService userService) {
        this.config = config;
        this.userService = userService;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!this.config.settings.onlyMessageFromAdmin) {
            return;
        }

        Player player = event.getPlayer();

        for (Player recipient : new ArrayList<>(event.getRecipients())) {
            Optional<User> userOptional = this.userService.find(recipient.getUniqueId());

            if (!userOptional.isPresent()) {
                continue;
            }

            User user = userOptional.get();

            if (player.getName().equals(user.getAdmin())) {
                continue;
            }

            if (player.getName().equals(user.getName())) {
                continue;
            }

            event.getRecipients().remove(recipient);

        }
    }
}
