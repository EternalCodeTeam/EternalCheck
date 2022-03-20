package com.eternalcode.check.listener;

import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserManager;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import panda.std.Option;

public class AsyncPlayerChatListener implements Listener {
    private final PluginConfiguration config;
    private final UserManager userManager;

    public AsyncPlayerChatListener(PluginConfiguration config, UserManager userManager) {
        this.config = config;
        this.userManager = userManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (this.config.settings.onlyAdminChat) {
            for (Player recipient : event.getRecipients()) {
                Option<User> userOption = this.userManager.find(recipient.getUniqueId());
                if (userOption.isEmpty()) {
                    continue;
                }

                User user = userOption.get();

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
}
