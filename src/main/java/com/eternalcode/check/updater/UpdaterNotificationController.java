package com.eternalcode.check.updater;

import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterNotificationController implements Listener {

    private static final Notification NEW_VERSION_AVAILABLE = Notification.chat("<b><gradient:#8a1212:#fc6b03>EternalCheck:</gradient></b> <color:#fce303>New version of EternalCheck is available, please update!");

    private final NotificationAnnouncer announcer;
    private final UpdaterService updaterService;
    private final PluginConfig pluginConfig;

    public UpdaterNotificationController(NotificationAnnouncer announcer, UpdaterService updaterService, PluginConfig pluginConfig) {
        this.announcer = announcer;
        this.updaterService = updaterService;
        this.pluginConfig = pluginConfig;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!this.pluginConfig.settings.receiveUpdates) {
            return;
        }

        if (!player.hasPermission("eternalcheck.receiveupdates")) {
            return;
        }

        this.updaterService.isUpToDate().whenComplete((isUpToDate, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();

                return;
            }

            if (!isUpToDate) {
                this.announcer.sendAnnounce(player, NEW_VERSION_AVAILABLE);
            }
        });
    }
}
