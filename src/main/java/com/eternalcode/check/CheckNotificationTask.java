package com.eternalcode.check;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import panda.utilities.text.Formatter;

import java.time.Duration;
import java.util.UUID;

public final class CheckNotificationTask implements Runnable {

    private final MessagesConfig messages;
    private final PluginConfig config;
    private final CheckedUserService checkedUserService;
    private final NotificationAnnouncer announcer;

    private final Duration stay, fadeOut, fadeIn;

    public CheckNotificationTask(MessagesConfig messages, PluginConfig config, CheckedUserService checkedUserService, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.config = config;
        this.checkedUserService = checkedUserService;
        this.announcer = announcer;

        this.stay = this.config.settings.title.stay;
        this.fadeOut = this.config.settings.title.fadeOut;
        this.fadeIn = this.config.settings.title.fadeIn;
    }

    @Override
    public void run() {
        for (CheckedUser user : this.checkedUserService.getUsers()) {
            UUID userUniqueId = user.getUniqueId();

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getAdmin());

            if (this.config.settings.title.taskTitleMessageEnabled) {
                this.announcer.annouceTitle(userUniqueId, this.messages.check.task.title, this.messages.check.task.subTitle, this.stay, this.fadeOut, this.fadeIn);
            }

            if (this.config.settings.taskActionBarEnabled) {
                this.announcer.annouceActionBar(userUniqueId, this.messages.check.task.actionBar);
            }

            if (this.config.settings.taskMessageEnabled) {
                this.messages.check.task.message.forEach(message -> this.announcer.annouceMessage(userUniqueId, formatter.format(message)));
            }
        }
    }
}