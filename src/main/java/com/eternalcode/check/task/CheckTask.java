package com.eternalcode.check.task;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserManager;
import com.eternalcode.check.util.ChatUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import panda.utilities.text.Formatter;

import java.util.UUID;

public class CheckTask implements Runnable {

    private final BukkitAudiences bukkitAudiences;
    private final MessagesConfiguration messages;
    private final PluginConfiguration config;
    private final UserManager userManager;

    private final int stay, fadeOut, fadeIn;

    public CheckTask(MessagesConfiguration messages, PluginConfiguration config, BukkitAudiences bukkitAudiences, UserManager userManager) {
        this.messages = messages;
        this.config = config;
        this.bukkitAudiences = bukkitAudiences;
        this.userManager = userManager;

        this.stay = this.config.settings.title.stay;
        this.fadeOut = this.config.settings.title.fadeOut;
        this.fadeIn = this.config.settings.title.fadeIn;
    }

    @Override
    public void run() {
        for (User user : this.userManager.getUsers()) {
            UUID uniqueId = user.getUniqueId();

            Audience audience = this.bukkitAudiences.player(uniqueId);

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getAdmin());

            if (this.config.settings.title.onRunnable) {
                ChatUtils.sendTitle(audience,
                        formatter.format(this.messages.check.player.runnable.title),
                        formatter.format(this.messages.check.player.runnable.subTitle),
                        this.fadeIn, this.stay, this.fadeOut);
            }

            if (this.config.settings.actionBarEnabled) {
                ChatUtils.sendActionBar(audience, formatter.format(this.messages.check.player.runnable.actionBar));
            }

            if (this.config.settings.messageEnabled) {
                for (String message : this.messages.check.player.runnable.message) {
                    audience.sendMessage(Component.text(ChatUtils.colour(formatter.format(message))));
                }
            }
        }
    }
}
