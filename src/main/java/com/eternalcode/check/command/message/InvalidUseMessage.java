package com.eternalcode.check.command.message;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvalidUseMessage implements InvalidUsageHandler<CommandSender> {

	private final MessagesConfig messagesConfig;
	private final NotificationAnnouncer announcer;

	public InvalidUseMessage(MessagesConfig messagesConfig, NotificationAnnouncer announcer) {
		this.messagesConfig = messagesConfig;
		this.announcer = announcer;
	}

	@Override
	public void handle(CommandSender commandSender, LiteInvocation invocation, Schematic scheme) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;

			if (scheme.getSchematics().size() > 1) {
				for (String schem : scheme.getSchematics()) {
					this.announcer.announceMessage(player.getUniqueId(), this.messagesConfig.argument.correctUsageList.replace("{USAGE}", schem));
				}
			} else {
				this.announcer.announceMessage(player.getUniqueId(), this.messagesConfig.argument.correctUsage.replace("{USAGE}", scheme.getSchematics().get(0)));
			}
		}
	}
}
