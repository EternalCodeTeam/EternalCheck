package com.eternalcode.check.user;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

public class User {

    @Getter private final UUID uniqueId;
    @Getter private final String name;
    @Getter private final String admin;
    @Getter private final Location lastLocation;

    public User(UUID uniqueId, String name, String admin, Location lastLocation) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.admin = admin;
        this.lastLocation = lastLocation;
    }

}
