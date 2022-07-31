package com.eternalcode.check;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import org.panda_lang.utilities.inject.annotations.Inject;
import panda.utilities.text.Formatter;

import java.util.UUID;

public final class CheckNotificationTask implements Runnable {

    @Inject
    private MessagesConfig messages;
    @Inject
    private PluginConfig config;
    @Inject
    private CheckedUserService checkedUserService;
    @Inject
    private NotificationAnnouncer announcer;

    @Override
    public void run() {
        for (CheckedUser user : this.checkedUserService.getUsers()) {
            UUID userUniqueId = user.getUniqueId();

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", user.getName())
                    .register("{ADMIN}", user.getChecker());

            if (this.config.settings.title.taskTitleMessageEnabled) {
                this.announcer.annouceTitle(userUniqueId, this.messages.check.task.title, this.messages.check.task.subTitle, this.config.settings.title.stay, this.config.settings.title.fadeOut, this.config.settings.title.fadeIn);
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