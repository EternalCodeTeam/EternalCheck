package com.eternalcode.check.command.implementation;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.StringUtils;
import panda.utilities.text.Formatter;

import java.util.Optional;

@Section(route = "admit", aliases = { "przyznajsie", "ff" })
public class AdmitCommand {

    private final MessagesConfig messages;
    private final PluginConfig config;
    private final CheckedUserService checkedUserService;
    private final Server server;
    private final NotificationAnnouncer announcer;

    public AdmitCommand(MessagesConfig messages, PluginConfig config, CheckedUserService checkedUserService, Server server, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.config = config;
        this.checkedUserService = checkedUserService;
        this.server = server;
        this.announcer = announcer;
    }

    @Execute
    public void execute(Player player) {
        Optional<CheckedUser> userOptional = this.checkedUserService.find(player.getUniqueId());

        if (!userOptional.isPresent()) {
            this.announcer.annouceMessage(player.getUniqueId(), this.messages.argument.youArentChecked);

            return;
        }

        CheckedUser user = userOptional.get();

        this.checkedUserService.remove(player.getUniqueId());

        this.server.dispatchCommand(this.server.getConsoleSender(), StringUtils.replace(this.config.commands.admit, "{PLAYER}", player.getName()));

        Formatter formatter = new Formatter()
                .register("{PLAYER}", user.getName())
                .register("{ADMIN}", user.getAdmin());

        for (Player all : this.server.getOnlinePlayers()) {
            this.messages.check.broadcast.admit.forEach(message -> this.announcer.annouceMessage(all.getUniqueId(), formatter.format(message)));
        }
    }
}