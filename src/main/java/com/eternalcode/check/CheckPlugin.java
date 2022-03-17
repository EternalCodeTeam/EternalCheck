package com.eternalcode.check;

import com.eternalcode.check.command.argument.PlayerArgument;
import com.eternalcode.check.command.argument.UserArgument;
import com.eternalcode.check.command.bind.PlayerrBind;
import com.eternalcode.check.command.implementation.AdmitCommand;
import com.eternalcode.check.command.implementation.CheckCommand;
import com.eternalcode.check.command.message.PermissionMessage;
import com.eternalcode.check.command.message.UsageMessage;
import com.eternalcode.check.configuration.ConfigurationManager;
import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.configuration.implementation.PluginConfiguration;
import com.eternalcode.check.listener.AsyncPlayerChatListener;
import com.eternalcode.check.listener.PlayerCommandPreprocessListener;
import com.eternalcode.check.listener.PlayerMoveListener;
import com.eternalcode.check.listener.PlayerQuitListener;
import com.eternalcode.check.task.CheckTask;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserManager;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.stream.Stream;

@Plugin(name = "etrlCheck", version = "1.0.0")
@Author("Osnixer")
@ApiVersion( ApiVersion.Target.v1_13)
@Description("A simple plugin for checking suspicious players")
public class CheckPlugin extends JavaPlugin {

    @Getter private ConfigurationManager configurationManager;
    @Getter private PluginConfiguration configuration;
    @Getter private BukkitAudiences bukkitAudiences;
    @Getter private MessagesConfiguration messages;
    @Getter private static CheckPlugin instance;
    @Getter private LiteCommands liteCommands;
    @Getter private UserManager userManager;

    @Override
    public void onEnable() {
        Server server = this.getServer();

        this.configurationManager = new ConfigurationManager(server);
        this.configuration = new PluginConfiguration(this.getDataFolder(), "config.yml");
        this.messages = new MessagesConfiguration(this.getDataFolder(), "messages.yml");

        this.configurationManager.loadAndRender(this.configuration);
        this.configurationManager.loadAndRender(this.messages);

        this.userManager = new UserManager();

        this.bukkitAudiences = BukkitAudiences.create(this);

        this.liteCommands = LiteBukkitFactory.builder(server, "etrlCheck")
                .typeBind(ConfigurationManager.class, this.configurationManager)
                .typeBind(PluginConfiguration.class, this.configuration)
                .typeBind(MessagesConfiguration.class, this.messages)
                .typeBind(BukkitAudiences.class, this.bukkitAudiences)
                .typeBind(UserManager.class, this.userManager)
                .typeBind(Server.class, server)

                .parameterBind(Player.class, new PlayerrBind(this.messages))

                .argument(Player.class, new PlayerArgument(this.messages, server))
                .argument(User.class, new UserArgument(this.messages, this.userManager, server))

                .formattingUseScheme(UseSchemeFormatting.NORMAL)

                .message(ValidationInfo.NO_PERMISSION, new PermissionMessage(this.messages))
                .message(ValidationInfo.INVALID_USE, new UsageMessage(this.messages))

                .command(AdmitCommand.class, CheckCommand.class)

                .register();

        if (this.configuration.settings.runnable.enabled) {
            server.getScheduler().runTaskTimerAsynchronously(this,
                    new CheckTask(this.messages, this.configuration, this.bukkitAudiences, this.userManager),
                    0L, 20L * this.configuration.settings.runnable.interval);
        }

        Stream.of(
                new PlayerCommandPreprocessListener(this.messages, this.configuration, this.userManager),
                new PlayerMoveListener(this.configuration, this.userManager),
                new PlayerQuitListener(this.messages, this.configuration, this.userManager, server),
                new AsyncPlayerChatListener(this.configuration, this.userManager, server)
        ).forEach(listener -> server.getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatformManager().unregisterCommands();
    }

}
