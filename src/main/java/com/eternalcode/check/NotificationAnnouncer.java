package com.eternalcode.check;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

import java.time.Duration;
import java.util.UUID;

public final class NotificationAnnouncer {

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NotificationAnnouncer(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void annouceTitle(UUID uniqueId, String title, String subTitle) {
        Audience audience = this.audienceProvider.player(uniqueId);

        audience.sendTitlePart(TitlePart.TITLE, this.miniMessage.deserialize(title));
        audience.sendTitlePart(TitlePart.SUBTITLE, this.miniMessage.deserialize(subTitle));
    }

    public void annouceTitle(UUID uniqueId, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Audience audience = this.audienceProvider.player(uniqueId);

        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));

        audience.sendTitlePart(TitlePart.TITLE, this.miniMessage.deserialize(title));
        audience.sendTitlePart(TitlePart.SUBTITLE, this.miniMessage.deserialize(subTitle));
        audience.sendTitlePart(TitlePart.TIMES, times);
    }

    public void annouceActionBar(UUID uniqueId, String message) {
        this.audienceProvider.player(uniqueId).sendMessage(this.miniMessage.deserialize(message));
    }

    public void annouceMessage(UUID uniqueId, String message) {
        this.audienceProvider.player(uniqueId).sendMessage(this.miniMessage.deserialize(message));
    }

    public void annouceMessage(Audience audience, String message) {
        audience.sendMessage(this.miniMessage.deserialize(message));
    }
}
