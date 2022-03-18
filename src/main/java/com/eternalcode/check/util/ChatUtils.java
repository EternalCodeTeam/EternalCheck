package com.eternalcode.check.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;

import java.time.Duration;

@UtilityClass
public class ChatUtils {

    public static String colour(String toColour) {
        return ChatColor.translateAlternateColorCodes('&', toColour);
    }

    public static void sendTitle(Audience audience, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        //Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));

        Component titleMessage = Component.text(colour(title));
        Component subTitleMessage = Component.text(colour(subTitle));

        //audience.sendTitlePart(TitlePart.TIMES, times);
        audience.sendTitlePart(TitlePart.TITLE, titleMessage);
        audience.sendTitlePart(TitlePart.SUBTITLE, subTitleMessage);
    }

    public static void sendActionBar(Audience audience, String message) {
        audience.sendActionBar(Component.text(colour(message)));
    }
}
