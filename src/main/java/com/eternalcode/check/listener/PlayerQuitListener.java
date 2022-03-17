package com.eternalcode.check.listener;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.UserManager;
import com.eternalcode.check.util.ChatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import panda.utilities.text.Formatter;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final MessagesConfiguration messages;
    private final PluginConfiguration config;
    private final UserManager userManager;
    private final Server server;

    public PlayerQuitListener(MessagesConfiguration messages, PluginConfiguration config, UserManager userManager, Server server) {
        this.messages = messages;
        this.config = config;
        this.userManager = userManager;
        this.server = server;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.userManager.find(uniqueId).peek(user -> {

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", player.getName());

            this.messages.check.broadcast.logoutCheck
                    .forEach(message -> this.server.broadcastMessage(ChatUtils.colour(formatter.format(message))));

            this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.logout, "{PLAYER}", player.getName()));

            this.userManager.remove(uniqueId);
        });
    }
}
