package com.eternalcode.check.notification;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Notification {

    protected final Set<NotificationType> types = new HashSet<>();
    protected final String message;

    protected Notification(String message, NotificationType... types) {
        this.message = message;
        this.types.addAll(Arrays.asList(types));
    }

    protected Notification(String message, Collection<NotificationType> types) {
        this.message = message;
        this.types.addAll(types);
    }

    public Set<NotificationType> types() {
        return Collections.unmodifiableSet(this.types);
    }

    public String message() {
        return this.message;
    }

    public static Notification of(String message, NotificationType... types) {
        return new Notification(message, types);
    }

    public static Notification of(String message, Collection<NotificationType> types) {
        return new Notification(message, types);
    }

    public static Notification actionbar(String message) {
        return new Notification(message, NotificationType.ACTIONBAR);
    }

    public static Notification chat(String message) {
        return new Notification(message, NotificationType.CHAT);
    }

    public static Notification title(String message) {
        return new Notification(message, NotificationType.TITLE);
    }

    public static Notification subtitle(String message) {
        return new Notification(message, NotificationType.SUBTITLE);
    }

    public static Notification bossbar(String message) {
        return new Notification(message, NotificationType.BOSSBAR);
    }

    public static Notification none() {
        return new Notification("none", NotificationType.NONE);
    }
}
