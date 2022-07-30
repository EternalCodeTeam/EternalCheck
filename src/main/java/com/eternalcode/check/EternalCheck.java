package com.eternalcode.check;

import com.eternalcode.check.command.argument.PlayerArgument;
import com.eternalcode.check.command.argument.UserArgument;
import com.eternalcode.check.command.implementation.AdmitCommand;
import com.eternalcode.check.command.implementation.CheckCommand;
import com.eternalcode.check.command.message.InvalidUseMessage;
import com.eternalcode.check.command.message.PermissionMessage;
import com.eternalcode.check.config.ConfigManager;
import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.config.implementation.PluginConfig;
import com.eternalcode.check.controller.CheckedPlayerChatController;
import com.eternalcode.check.controller.CheckedPlayerCommandController;
import com.eternalcode.check.controller.CheckedPlayerMoveController;
import com.eternalcode.check.controller.LogoutPunishmentController;
import com.eternalcode.check.shared.legacy.LegacyColorProcessor;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class EternalCheck extends JavaPlugin {

    private static EternalCheck instance;

    private ConfigManager configManager;
    private PluginConfig config;
    private MessagesConfig messages;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;

    private NotificationAnnouncer notificationAnnouncer;

    private UserService userService;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;

        Metrics metrics = new Metrics(this, 15964);

        Server server = this.getServer();

        this.configManager = new ConfigManager(this.getDataFolder());

        this.config = new PluginConfig();
        this.messages = new MessagesConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();

        this.notificationAnnouncer = new NotificationAnnouncer(this.audienceProvider, this.miniMessage);

        this.userService = new UserService();

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "EternalCheck")
                .argument(Player.class, new PlayerArgument(this.messages, server))
                .argument(User.class, new UserArgument(this.messages, this.userService, server))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual(""))

                .permissionHandler(new PermissionMessage(this.messages, this.notificationAnnouncer))
                .invalidUsageHandler(new InvalidUseMessage(this.messages, this.notificationAnnouncer))

                .commandInstance(new AdmitCommand(this.messages, this.config, this.userService, server, this.notificationAnnouncer))
                .commandInstance(new CheckCommand(this.configManager, this.messages, this.config, this.userService, server, this.notificationAnnouncer))

                .register();

        Stream.of(
                new CheckedPlayerChatController(this.config, this.userService),
                new CheckedPlayerCommandController(this.messages, this.config, this.userService, this.notificationAnnouncer),
                new CheckedPlayerMoveController(this.config, this.userService),
                new LogoutPunishmentController(this.messages, this.config, this.userService, server, this.notificationAnnouncer)
        ).forEach(listener -> server.getPluginManager().registerEvents(listener, this));

        if (!this.config.settings.runnable.enabled) {
            return;
        }

        server.getScheduler().runTaskTimerAsynchronously(this,
                new CheckNotificationTask(this.messages, this.config, this.userService, this.notificationAnnouncer),
                0L, 20L * this.config.settings.runnable.interval
        );

    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

    public static EternalCheck getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
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

    public UserService getUserService() {
        return this.userService;
    }
}