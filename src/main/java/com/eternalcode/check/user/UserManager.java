package com.eternalcode.check.user;

import org.bukkit.Location;
import panda.std.Option;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final Map<UUID, User> userMap = new HashMap<>();

    public void create(UUID uniqueId, String name, String admin, Location lastLocation) {
        this.userMap.put(uniqueId, new User(uniqueId, name, admin, lastLocation));
    }

    public Option<User> find(UUID uniqueId) {
        return Option.of(this.userMap.get(uniqueId));
    }

    public void remove(UUID uniqueId) {
        this.userMap.remove(uniqueId);
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(this.userMap.values());
    }
}
