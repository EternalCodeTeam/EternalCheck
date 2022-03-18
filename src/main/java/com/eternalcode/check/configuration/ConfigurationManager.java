package com.eternalcode.check.configuration;

import com.eternalcode.check.configuration.composer.LocationComposer;
import com.eternalcode.check.configuration.composer.StringComposer;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import org.bukkit.Location;
import org.bukkit.Server;

public class ConfigurationManager {

    private final Cdn cdn;

    public ConfigurationManager(Server server) {
        this.cdn = CdnFactory.createYamlLike()
                .getSettings()
                .withComposer(Location.class, new LocationComposer(server))
                .withComposer(String.class, new StringComposer())
                .build();
    }

    public <T extends ConfigWithResource> void loadAndRender(T config) {
        cdn.load(config.getResource(), config)
                .orElseThrow(RuntimeException::new);

        cdn.render(config, config.getResource())
                .orElseThrow(RuntimeException::new);
    }

    public <T extends ConfigWithResource> void render(T config) {
        cdn.render(config, config.getResource())
                .orElseThrow(RuntimeException::new);
    }

}
