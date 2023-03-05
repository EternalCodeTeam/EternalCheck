package com.eternalcode.check;

import com.eternalcode.check.caller.EventCaller;
import com.eternalcode.check.command.CommandConfiguration;
import com.eternalcode.check.command.argument.CheckedUserArgument;
import com.eternalcode.check.command.argument.PlayerArgument;
import com.eternalcode.check.command.configurer.CommandConfigurer;
import com.eternalcode.check.command.handler.NotificationHandler;
import com.eternalcode.check.command.implementation.AdmitCommand;
import com.eternalcode.check.command.implementation.CheckBanCommand;
import com.eternalcode.check.command.implementation.CheckEndCommand;
import com.eternalcode.check.command.implementation.CheckReloadCommand;
import com.eternalcode.check.command.implementation.CheckSetLocationCommand;
import com.eternalcode.check.command.implementation.CheckStartCommand;
import com.eternalcode.check.command.handler.InvalidUsageHandler;
import com.eternalcode.check.command.handler.PermissionHandler;
import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.CheckedUserDataConfig;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.notification.NotificationAnnouncer;
import com.eternalcode.check.updater.UpdaterNotificationController;
import com.eternalcode.check.updater.UpdaterService;
import com.eternalcode.check.user.controller.CheckedUserChatController;
import com.eternalcode.check.user.controller.CheckedUserCommandController;
import com.eternalcode.check.user.controller.CheckedUserLogoutPunishmentController;
import com.eternalcode.check.user.controller.CheckedUserMoveController;
import com.eternalcode.check.shared.legacy.LegacyColorProcessor;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class EternalCheck extends JavaPlugin {

    private ConfigManager configManager;
    private CheckedUserDataConfig checkedUserDataConfig;
    private CommandConfiguration commandConfig;
    private MessagesConfig messages;
    private PluginConfig config;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;

    private NotificationAnnouncer notificationAnnouncer;

    private CheckedUserService checkedUserService;

    private EventCaller eventCaller;

    private UpdaterService updaterService;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Server server = this.getServer();

        this.configManager = new ConfigManager(this.getDataFolder());

        this.checkedUserDataConfig = new CheckedUserDataConfig();
        this.commandConfig = new CommandConfiguration();
        this.config = new PluginConfig();
        this.messages = new MessagesConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);
        this.configManager.load(this.checkedUserDataConfig);
        this.configManager.load(this.commandConfig);

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();

        this.notificationAnnouncer = new NotificationAnnouncer(this.audienceProvider, this.miniMessage, this.config, server);

        this.checkedUserService = new CheckedUserService();

        this.eventCaller = new EventCaller(server);

        this.updaterService = new UpdaterService(this.getDescription());

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "eternalcheck")
                .argument(Player.class, new PlayerArgument(this.messages, server))
                .argument(CheckedUser.class, new CheckedUserArgument(this.checkedUserService, this.messages, server))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only players can use this command!"))

                .resultHandler(RequiredPermissions.class, new PermissionHandler(this.notificationAnnouncer, this.messages))
                .resultHandler(Schematic.class, new InvalidUsageHandler(this.messages, this.notificationAnnouncer))
                .resultHandler(Notification.class, new NotificationHandler(this.notificationAnnouncer))

                .commandInstance(
                        new AdmitCommand(this.messages, this.config, this.checkedUserService, server, this.notificationAnnouncer, this.eventCaller),
                        new CheckBanCommand(this.checkedUserService, this.notificationAnnouncer, this.messages, this.eventCaller, this.config, server),
                        new CheckEndCommand(this.checkedUserService, this.notificationAnnouncer, this.eventCaller, this.messages, server),
                        new CheckReloadCommand(this.notificationAnnouncer, this.configManager, this.messages),
                        new CheckSetLocationCommand(this.notificationAnnouncer, this.configManager, this.messages, this.config),
                        new CheckStartCommand(this.checkedUserDataConfig, this.checkedUserService, this.notificationAnnouncer, this.configManager, this.messages, this.eventCaller, this.config)
                )

                .commandGlobalEditor(new CommandConfigurer(this.commandConfig))

                .register();

        Metrics metrics = new Metrics(this, 15964);
        metrics.addCustomChart(new SingleLineChart("checked_users", () -> this.checkedUserDataConfig.getCheckedUsers()));

        Stream.of(
                new CheckedUserChatController(this.checkedUserService, this.config),
                new CheckedUserCommandController(this.checkedUserService, this.notificationAnnouncer, this.messages, this.config),
                new CheckedUserMoveController(this.checkedUserService, this.config),
                new CheckedUserLogoutPunishmentController(this.checkedUserService, this.notificationAnnouncer, this.messages, this.config, eventCaller, server),
                new UpdaterNotificationController(this.notificationAnnouncer, this.updaterService, this.config)
        ).forEach(listener -> server.getPluginManager().registerEvents(listener, this));

        if (this.config.settings.runnable.enabled) {
            server.getScheduler().runTaskTimerAsynchronously(
                    this,
                    new CheckNotificationTask(this.checkedUserService, this.notificationAnnouncer, this.messages, server),
                    20L,
                    20L * this.config.settings.runnable.interval);
        }
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public CheckedUserDataConfig getDataConfig() {
        return this.checkedUserDataConfig;
    }

    public CommandConfiguration getCommandConfig() {
        return this.commandConfig;
    }

    public PluginConfig getPluginConfig() {
        return this.config;
    }

    public MessagesConfig getMessagesConfig() {
        return this.messages;
    }

    public AudienceProvider getAudienceProvider() {
        return this.audienceProvider;
    }

    public MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    public NotificationAnnouncer getNotificationAnnouncer() {
        return this.notificationAnnouncer;
    }

    public CheckedUserService getUserService() {
        return this.checkedUserService;
    }

    public EventCaller getEventCaller() {
        return this.eventCaller;
    }

    public UpdaterService getUpdaterService() {
        return this.updaterService;
    }

    public LiteCommands<CommandSender> getLiteCommands() {
        return this.liteCommands;
    }
}