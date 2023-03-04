package com.eternalcode.check.user.event;

import com.eternalcode.check.user.CheckedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class CheckedUserEvent extends Event {

    private final CheckedUser checkedUser;
    private final Player admin;

    protected CheckedUserEvent(CheckedUser checkedUser, Player admin) {
        this.checkedUser = checkedUser;
        this.admin = admin;
    }

    public CheckedUser getCheckedUser() {
        return this.checkedUser;
    }

    public Player getAdmin() {
        return this.admin;
    }
}
