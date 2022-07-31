package com.eternalcode.check.user;

import com.eternalcode.check.shared.position.Position;

import java.util.Objects;
import java.util.UUID;

public class CheckedUser {

    private final UUID uniqueId;
    private final String name;
    private final String checker;
    private final Position lastPosition;

    CheckedUser(UUID uniqueId, String name, String checker, Position lastPosition) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.checker = checker;
        this.lastPosition = lastPosition;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public String getChecker() {
        return this.checker;
    }

    public Position getLastPosition() {
        return this.lastPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CheckedUser user = (CheckedUser) o;

        return this.uniqueId.equals(user.uniqueId) && this.checker.equals(user.checker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uniqueId, this.checker);
    }
}
