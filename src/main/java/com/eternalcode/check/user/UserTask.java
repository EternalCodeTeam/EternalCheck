package com.eternalcode.check.user;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import panda.utilities.text.Formatter;

import java.util.UUID;

public final class UserTask implements Runnable {

    private final MessagesConfig messages;
    private final PluginConfig config;
    private final UserService userService;
    private final NotificationAnnouncer announcer;

    private final int stay, fadeOut, fadeIn;

    public UserTask(MessagesConfig messages, PluginConfig config, UserService userService, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.config = config;
        this.userService = userService;
        this.announcer = announcer;

        this.stay = this.config.settings.title.stay;
        this.fadeOut = this.config.settings.title.fadeOut;
        this.fadeIn = this.config.settings.title.fadeIn;
    }

    @Override
    public void run() {
        for (User user : this.userService.getUsers()) {
            UUID uniqueId = user.getUniqueId();

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getAdmin());

            if (this.config.settings.title.taskTitleMessageEnabled) {
                this.announcer.annouceTitle(uniqueId, this.messages.check.task.title, this.messages.check.task.subTitle, this.stay, this.fadeOut, this.fadeIn);
            }

            if (this.config.settings.taskActionBarEnabled) {
                this.announcer.annouceActionBar(uniqueId, this.messages.check.task.actionBar);
            }

            if (this.config.settings.taskMessageEnabled) {
                this.messages.check.task.message.forEach(message -> this.announcer.annouceMessage(uniqueId, formatter.format(message)));
            }
        }
    }
}