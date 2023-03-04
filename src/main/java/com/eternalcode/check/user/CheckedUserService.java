package com.eternalcode.check.user;

import com.eternalcode.check.shared.position.Position;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CheckedUserService {

    private final Map<UUID, CheckedUser> checkedUsers = new HashMap<>();

    public CheckedUser markChecked(UUID uniqueId, String name, String admin, Position lastPosition) {
        CheckedUser checkedUser = new CheckedUser(uniqueId, name, admin, lastPosition);

        this.checkedUsers.put(uniqueId, checkedUser);

        return checkedUser;
    }

    public Optional<CheckedUser> find(UUID uniqueId) {
        return Optional.ofNullable(this.checkedUsers.get(uniqueId));
    }

    public void unmarkChecked(UUID uniqueId) {
        this.checkedUsers.remove(uniqueId);
    }

    public Collection<CheckedUser> users() {
        return Collections.unmodifiableCollection(this.checkedUsers.values());
    }
}
