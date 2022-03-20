package com.eternalcode.check.configuration.implementation;

import com.eternalcode.check.configuration.AbstractConfigWithResource;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MessagesConfiguration extends AbstractConfigWithResource {

    public MessagesConfiguration(File folder, String child) {
        super(folder, child);
    }

    @Description({ "# ",
            "# Plik konfiguracyjny pluginu EternalCheck.",
            "# ",
            "# Discord: https://dc.eternalcode.pl/",
            "# Website: https://eternalcode.pl/", " " })

    public Argument arguments = new Argument();

    @Description(" ")
    public Check check = new Check();

    @Contextual
    public static class Argument {
        public String permission = "&4Blad: &cNie masz uprawnien! &7({PERMISSIONS})";

        @Description(" ")
        public String correctUsage = "&ePoprawne uzycie: &7{USAGE}";

        @Description(" ")
        public String onlyPlayer = "&4Blad: &cKomenda tylko dla graczy!";

        @Description(" ")
        public String offlinePlayer = "&4Blad: &cPodany gracz jest offline!";
    }

    @Contextual
    public static class Check {

        @Description(" ")
        public Argument arguments = new Argument();

        @Description(" ")
        public Player player = new Player();

        @Description(" ")
        public Admin admin = new Admin();

        @Description(" ")
        public Broadcast broadcast = new Broadcast();

        @Contextual
        public static class Argument {
            @Description("# Wiadomosci ")

            @Description(" ")
            public String roomNotSet = "&4Blad: &cMiejsce sprawdzania graczy nie jest ustawione!";

            @Description(" ")
            public String notChecked = "&4Blad: &cPodany gracz nie jest sprawdzany!";

            @Description(" ")
            public String isChecked = "&4Blad: &cPodany gracz jest juz sprawdzany!";

            @Description(" ")
            public String youArentChecked = "&4Blad: &cNie jestes sprawdzany!";

            @Description(" ")
            public String cantUseCommand = "&4Blad: &cJestes sprawdzany! Podczas sprawdzania mozesz użyc tylko: &6/helpop, &6/przyznajsie";

            @Description(" ")
            public String bypass = "&4Blad: &cTego gracza nie mozesz sprawdzic!";

            @Description(" ")
            public List<String> help = Arrays.asList(
                    "&8• &7/check start <player> - rozpoczyna sprawdzanie",
                    "&8• &7/check end <player> - konczy sprawdzanie",
                    "&8• &7/check ban <player> - banuje gracza za cheaty",
                    "&8• &7/check set - ustawia miejsce sprawdzania"
            );
        }

        @Contextual
        public static class Admin {
            @Description({"# Wiadomosci dla admina", "# Dostene zmienne: {PLAYER}"})

            @Description(" ")
            public String start = "&6Rozpoczeto sprawdzanie gracza: &c{PLAYER}";

            @Description(" ")
            public String end = "&6Zakonczono sprawdzanie gracza: &c{PLAYER}";

            @Description(" ")
            public String ban = "&6Zbanowano sprawdzanego gracza: &c{PLAYER}";

        }

        @Contextual
        public static class Player {

            public Runnable runnable = new Runnable();

            @Contextual
            public static class Runnable {
                @Description("# Wiadomosc na actionbarze")
                public String actionBar = "&cJestes sprawdzany!";

                @Description({ " ", "# Title", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
                public String title = "&cJestes sprawdzany!";

                @Description({ " ", "# SubTitle", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
                public String subTitle = "&7Informacje na chacie!";

                @Description({ " ", "# Wiadomosc ", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
                public List<String> message = Arrays.asList("&cJestes sprawdzany!", "&6Udaj sie na kanal na discordzie!");
            }

            @Description({ "# Wiadomosci ponizej pokaza sie tylko raz", "# gdy gracz jest sprawdzany " })

            @Description({ " ", "# Title", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public String startTitle = "&cJestes sprawdzany!";

            @Description({ " ", "# SubTitle ", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public String startSubTitle = "&7Postepuj zgonie z instrukcjami na chacie.";

            @Description({ " ", "# Wiadomosc ", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> startMessage = Arrays.asList("&cJestes sprawdzany przez: {ADMIN}",
                    "&cUdaj sie na kanal na discordzie!");
        }

        @Contextual
        public static class Broadcast {
            @Description({ "# Wiadomosc gdy gracz przyzna sie do cheatow", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> admit = Collections.singletonList("&6Gracz: &c{PLAYER} &6przyznal sie do cheatow!");

            @Description({ " ", "# Wiadomosc gdy gracz bedzie sprawdzany", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> startCheck = Collections.singletonList("&6Gracz: &c{PLAYER} &6jest sprawdzany przez: &c{ADMIN}!");

            @Description({ " ", "# Wiadomosc gdy gracz zostanie oczyszczony", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> endCheck = Collections.singletonList("&6Gracz: &c{PLAYER} &6okazal sie czysty, byl sprawdzany przez: &c{ADMIN}!");

            @Description({ " ", "# Wiadomosc gdy gracz zostanie zbanowany", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> banCheck = Collections.singletonList("&6Gracz: &c{PLAYER} &6okazal sie cheaterem i zostal zbanowany! Byl sprawdzany przez: &c{ADMIN}!");

            @Description({ " ", "# Wiadomosc gdy gracz wyloguje sie podczas sprawdzania", "# Dostepne zmienne: {PLAYER}, {ADMIN}" })
            public List<String> logoutCheck = Collections.singletonList("&6Gracz: &c{PLAYER} &6wylogowal sie podczas sprawdzania!");
        }

        @Description({ " ", "# Wiadomosc ustawionego miejsca sprawdzania" })
        public String setLocation = "&aUstawiono miejsce sprawdzania graczy!";

    }
}
