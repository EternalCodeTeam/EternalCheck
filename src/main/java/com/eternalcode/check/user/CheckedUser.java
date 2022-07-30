package com.eternalcode.check.user;

import com.eternalcode.check.shared.position.Position;

import java.util.Objects;
import java.util.UUID;

public class CheckedUser {

    private final UUID uniqueId;
    private final String name;
    private final String admin;
    private final Position lastPosition;

    CheckedUser(UUID uniqueId, String name, String admin, Position lastPosition) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.admin = admin;
        this.lastPosition = lastPosition;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public String getAdmin() {
        return this.admin;
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

        return this.uniqueId.equals(user.uniqueId) && this.admin.equals(user.admin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uniqueId, this.admin);
    }
}
