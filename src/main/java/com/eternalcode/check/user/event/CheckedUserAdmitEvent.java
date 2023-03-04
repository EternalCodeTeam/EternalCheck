package com.eternalcode.check.user.event;

import com.eternalcode.check.user.CheckedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CheckedUserAdmitEvent extends CheckedUserEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public CheckedUserAdmitEvent(CheckedUser checkedUser, Player admin) {
        super(checkedUser, admin);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
