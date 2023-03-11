package com.eternalcode.check.config;

import com.eternalcode.check.config.composer.DurationComposer;
import com.eternalcode.check.config.composer.NotificationComposer;
import com.eternalcode.check.config.composer.PositionComposer;
import com.eternalcode.check.notification.Notification;
import com.eternalcode.check.shared.position.Position;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.MemberResolver;
import net.dzikoysk.cdn.reflect.Visibility;

import java.io.File;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private final static Cdn CDN = CdnFactory
            .createYamlLike()
            .getSettings()
            .withComposer(Position.class, new PositionComposer())
            .withComposer(Duration.class, new DurationComposer())
            .withComposer(Notification.class, new NotificationComposer())
            .withMemberResolver(Visibility.PRIVATE)
            .build();

    private final Set<ReloadableConfig> configs = new HashSet<>();
    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public <T extends ReloadableConfig> void load(T config) {
        CDN.load(config.resource(this.dataFolder), config)
                .orThrow(RuntimeException::new);

        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);

        this.configs.add(config);
    }

    public <T extends ReloadableConfig> void save(T config) {
        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);
    }

    public void reload() {
        for (ReloadableConfig config : this.configs) {
            load(config);
        }
    }

}