package com.eternalcode.check.command.implementation;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.UserManager;
import com.eternalcode.check.util.ChatUtils;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Section;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Section(route = "admit", aliases = { "przyznajsie", "ff" })
public class AdmitCommand {

    private final MessagesConfiguration messages;
    private final PluginConfiguration config;
    private final UserManager userManager;
    private final Server server;

    public AdmitCommand(MessagesConfiguration messages, PluginConfiguration config, UserManager userManager, Server server) {
        this.messages = messages;
        this.config = config;
        this.userManager = userManager;
        this.server = server;
    }

    @Execute
    public void execute(Player player) {
        if (this.userManager.find(player.getUniqueId()).isEmpty()) {
            player.sendMessage(ChatUtils.colour(this.messages.check.arguments.youArentChecked));
            return;
        }
        this.userManager.remove(player.getUniqueId());

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.admit, "{PLAYER}", player.getName()));

        this.messages.check.broadcast.admit.forEach(message -> this.server.broadcastMessage(ChatUtils.colour(StringUtils.replace(message, "{PLAYER}", player.getName()))));
    }
}
