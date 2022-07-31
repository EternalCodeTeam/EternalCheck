package com.eternalcode.check.controller;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Optional;

public class CheckedUserChatController implements Listener {

    private final PluginConfig config;
    private final CheckedUserService checkedUserService;

    public CheckedUserChatController(PluginConfig config, CheckedUserService checkedUserService) {
        this.config = config;
        this.checkedUserService = checkedUserService;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!this.config.settings.onlyMessageFromAdmin) {
            return;
        }

        Player player = event.getPlayer();

        for (Player recipient : new ArrayList<>(event.getRecipients())) {
            Optional<CheckedUser> userOptional = this.checkedUserService.find(recipient.getUniqueId());

            if (!userOptional.isPresent()) {
                continue;
            }

            CheckedUser user = userOptional.get();

            if (player.getName().equals(user.getChecker())) {
                continue;
            }

            if (player.getName().equals(user.getName())) {
                continue;
            }

            event.getRecipients().remove(recipient);

        }
    }
}
