package com.eternalcode.check.configuration.implementation;

import com.eternalcode.check.configuration.AbstractConfigWithResource;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import org.bukkit.Location;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PluginConfiguration extends AbstractConfigWithResource {

    public PluginConfiguration(File folder, String child) {
        super(folder, child);
    }

    @Description({ "# ",
            "# Plik konfiguracyjny pluginu etrlCheck.",
            "# ",
            "# Discord: https://dc.eternalcode.pl/",
            "# Website: https://eternalcode.pl/", " " })

    @Description("# Ustawienia sprawdzania")
    public Settings settings = new Settings();

    @Description({ "", "# Wykonywane komendy" })
    public Commands commands = new Commands();

    @Contextual
    public static class Settings {

        public Runnable runnable = new Runnable();

        @Description(" ")
        public Title title = new Title();

        @Contextual
        public static class Runnable {
            @Description("# Czy task ma byc wlaczony?")
            public boolean enabled = true;

            @Description({ " ", "# Co ile sekund ma dzialac task?" })
            public int interval = 20;
        }

        @Contextual
        public static class Title {
            @Description("# Wartosci title")
            public int stay = 20;
            public int fadeIn = 40;
            public int fadeOut = 20;

            @Description({ " ", "# Czy title na starcie sprawdzania ma byc wlaczone?" })
            public boolean onStart = true;

            @Description({ " ", "# Czy title w tasku ma byc wlaczone?" })
            public boolean onRunnable = true;

        }

        @Description({ " ", "# Czy wiadomosc w tasku ma byc wlaczona?" })
        public boolean messageEnabled = true;

        @Description({ " ", "# Czy actionbar w tasku ma byc wlaczony?" })
        public boolean actionBarEnabled = true;

        @Description({ " ", "# Czy sprawdzany gracz ma widziec tylko wiadomosci sprawdzajacego admina?" })
        public boolean onlyAdminChat = true;

        @Description({ "", "# Czy gracz nie moze sie ruszasz podczas sprawdzania?" })
        public boolean cantMove = true;

        @Description({ "", "# Czy gracz nie moze uzywac komend podczas sprawdzania?" })
        public boolean cantUseCommand = true;

        @Description({ "", "# Komendy ktore można uzywać podczas sprawdzania,",
                "# jezeli canUseCommand jest ustawione na true" })
        public List<String> availableCommands = Arrays.asList("/admit", "/helpop", "/przyznajsie");
    }

    @Contextual
    public static class Commands {
        @Description("# Komenda wykonywana przy uzyciu /admit")
        public String admit = "tempban {PLAYER} 3d Przyznanie sie do cheatow!";

        @Description({ "", "# Komenda wykonywana przy logoutcie" })
        public String logout = "ban {PLAYER} Logout podczas sprawdzania!";

        @Description({ "", "# Komenda wykonywana przy uzyciu /check ban" })
        public String ban = "tempban {PLAYER} 14d Posiadanie cheatow!";
    }

    @Description({ " ", "# Miejsce sprawdzania - uzyj \"/check set\" w grze!" })
    public Location checkLocation = new Location(null, 0, 100, 0);
}
