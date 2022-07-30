package com.eternalcode.check.user;

import com.eternalcode.check.shared.position.Position;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CheckedUserService {

    private final Map<UUID, CheckedUser> userMap = new HashMap<>();

    public void create(UUID uniqueId, String name, String admin, Position lastPosition) {
        this.userMap.put(uniqueId, new CheckedUser(uniqueId, name, admin, lastPosition));
    }

    public Optional<CheckedUser> find(UUID uniqueId) {
        return Optional.ofNullable(this.userMap.get(uniqueId));
    }

    public void remove(UUID uniqueId) {
        this.userMap.remove(uniqueId);
    }

    public Collection<CheckedUser> getUsers() {
        return Collections.unmodifiableCollection(this.userMap.values());
    }
}
