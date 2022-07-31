package com.eternalcode.check.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

import java.util.UUID;

public class CheckedUserLogoutPunishmentController extends AbstractController {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.checkedUserService.find(uniqueId).ifPresent(user -> {

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", player.getName());

            for (Player all : this.server.getOnlinePlayers()) {
                this.messages.check.broadcast.logoutCheck.forEach(message -> this.notificationAnnouncer.annouceMessage(all.getUniqueId(), formatter.format(message)));
            }

            this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.logout, "{PLAYER}", player.getName()));

            this.checkedUserService.remove(uniqueId);
        });
    }
}