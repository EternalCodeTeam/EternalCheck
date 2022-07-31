package com.eternalcode.check.controller;

import com.eternalcode.check.NotificationAnnouncer;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.user.CheckedUserService;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.panda_lang.utilities.inject.annotations.Inject;

public abstract class AbstractController implements Listener {

    @Inject
    public Server server;

    @Inject
    public PluginConfig config;
    @Inject
    public MessagesConfig messages;

    @Inject
    public CheckedUserService checkedUserService;

    @Inject
    public NotificationAnnouncer notificationAnnouncer;

}
