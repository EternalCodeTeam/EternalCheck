package com.eternalcode.check.configuration.composer;

import org.bukkit.Location;
import org.bukkit.Server;
import panda.std.Result;

public class LocationComposer implements SimpleComposer<Location> {

    private final Server server;

    public LocationComposer(Server server) {
        this.server = server;
    }

    @Override
    public Result<Location, Exception> deserialize(String source) {
        String[] split = source.split(";");

        return Result.ok(new Location(this.server.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])));
    }

    @Override
    public Result<String, Exception> serialize(Location entity) {
        String world = "world";

        if (entity.getWorld() != null) {
            world = entity.getWorld().getName();
        }

        return Result.ok((world +
                ";" + entity.getBlockX()) +
                ";" + entity.getBlockY() +
                ";" + entity.getBlockZ() +
                ";" + entity.getYaw() +
                ";" + entity.getPitch());
    }
}