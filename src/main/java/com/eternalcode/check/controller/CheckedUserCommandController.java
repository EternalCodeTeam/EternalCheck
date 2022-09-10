package com.eternalcode.check.controller;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class CheckedUserCommandController implements Listener {

	private final MessagesConfig messages;
	private final PluginConfig config;
	private final CheckedUserService checkedUserService;
	private final NotificationAnnouncer announcer;


	public CheckedUserCommandController(MessagesConfig messages, PluginConfig config, CheckedUserService checkedUserService, NotificationAnnouncer announcer) {
		this.messages = messages;
		this.config = config;
		this.checkedUserService = checkedUserService;
		this.announcer = announcer;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (this.config.settings.canUseCommand) {
			return;
		}

		Player player = event.getPlayer();
		UUID uniqueId = player.getUniqueId();
		String command = event.getMessage().split(" ")[0];

		this.checkedUserService.find(uniqueId).ifPresent(user -> {
			if (this.config.settings.availableCommands.contains(command)) {
				return;
			}

			event.setCancelled(true);
			this.announcer.announceMessage(uniqueId, this.messages.argument.cantUseCommand);

		});
	}
}
