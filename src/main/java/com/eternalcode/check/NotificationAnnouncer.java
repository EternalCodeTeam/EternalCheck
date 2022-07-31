package com.eternalcode.check;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;
import java.util.UUID;

public final class NotificationAnnouncer {

    @Inject
    private AudienceProvider audienceProvider;
    @Inject
    private MiniMessage miniMessage;

    public void annouceTitle(UUID uniqueId, String title, String subTitle, Duration fadeIn, Duration stay, Duration fadeOut) {
        Audience audience = this.audienceProvider.player(uniqueId);

        Title.Times times = Title.Times.times(fadeIn, stay, fadeOut);

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
}
