package com.eternalcode.check.caller;

import com.eternalcode.check.user.event.CheckedUserEvent;
import org.bukkit.Server;

public final class EventCaller {

    private final Server server;

    public EventCaller(Server server) {
        this.server = server;
    }

    public <T extends CheckedUserEvent> T callEvent(T event) {
        this.server.getPluginManager().callEvent(event);

        return event;
    }
}
