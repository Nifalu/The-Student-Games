package GameLogic;

import java.util.Random;

/**
 * If you land on an event card field, a random event card will be drawn from the list,
 * shown to all players and, if required, the person playing will be moved to the new position.
 */
public class Cards {
    // Beim Aufruf von getCards auf PlayingFields, sollte eine zufällige Ereigniskarte gezogen werden.
    // Der Inhalt sollte an alle versendet werden und die zu Felder die bewegt werden müssen (move),
    // wird an PlayingFields übergeben.

    public static String getCards() {
        String[] playingCards = new String[]{"6 Bonusnote bei A’Campo",
                "2 Jemand schickt die richtigen Lösungen für die Matheaufgaben dieser Woche in den Chat und du musst sie nicht selbst lösen. Gehe 2 Felder vor.",
                "3 Du freundest dich mit dem Mathegenie an. Gehe 3 Felder vorwärts.",
                "1 Dein Code besteht den Stylecheck und dein Tutor ist zufrieden. Gehe 1 Feld vorwärts.",
                "-9 Statistikprüfung nicht bestanden.",
                "-6 GDP oder EGDP nicht bestanden.",
                "-8 Deine Bachelorarbeit war miserabel.",
                "-2 Dein Code läuft nicht. Gehe  Felder zurück.",
                "-2 Jemand schickt dir die falschen Lösungen der dieswöchigen Matheaufgaben in den Chat und du schreibst sie ab. Gehe Felder zurück.",
                "-3 Du vergisst, dass du eine Vorlesung gehabt hättest. Gehe 3 Felder zurück.",
                "4 Stipendium erhalten.",
                "3 Tolle Lerngruppe.",
                "4 Deine Eltern unterstützen dich finanziell, so dass du nicht arbeiten musst.",
                "-4 Du hast dich für Analysis statt Mathematische Methoden entschieden. Gehe 4 Felder zurück.",
                "-5 Das Leben besteht aus Höhen und Tiefen, vor allem während des Studiums. Gehe 6 Schritte zurück.",
        };

        //TODO Ereigniskarten mit Bonuswürfel oder Tausch erstellen.
        //"Du hast kostenlosen Kaffee bekommen -> das Koffein lässt dich 2x würfeln"
        //"Dein Zoom-Meeting läuft. Du hast jedoch keine Internetverbindung. Setze 1x aus."
        //"Dein Stift geht während der Vorlesung kaputt und du kannst dir keine Notizen machen. Setz 1x aus."
        //"Du bist in der Vorlesung eingeschlafen und niemand weckt dich auf. Setze 1x aus."
        //"Du hast heute eine Statistikprüfung. Würfle deine Note."
        //"Quizfrage. Falls richtig: Felder überspringen. Und umgekehrt."
        //"Du hast absichtlich falsche Lösungen im Chat verbreitet. Alle anderen Spieler gehen x Felder zurück."
        //"Deine Gruppe im Programmierprojekt ist extrem motiviert. Du und ein Spieler deiner Wahl dürfen diese (oder nächste) Runde 2x würfeln."

        //"Du hast zu wild im Verso gefeiert (evlt. auswürfeln, ob man am nächsten Tag noch rechtzeitig aufsteht und man somit sogar gar keinen Nachteil hat)."
        //"Du hast Corona. Setze aus (alle im Umfeld setzen auch aus)."
        //"Du hast heute eine Statistikprüfung. Würfle deine Note."
        //"Hunger Games Event: Eine Horde von Wespen wurde soeben in den Saal freigelassen. Würfle deine Anzahl Stiche. (Pro Stich -2 Felder)."
        //"Hunger Games Event: Du meldest dich freiwillig als Tribut, um eine Matheaufgabe vorzuzeigen -> würfle, um zu sehen, ob deine Lösung dem Tutor gefällt."
        //"2 Mitglieder deiner Gruppe im Programmierprojekt brechen das Studium ab. Du würfelst ab jetzt neu mit einem Tetraeder (4er-Würfel) für den Rest des Jahres."
        //"Du hast dich barmherzig gezeigt und einem Kommilitonen im Fach x Nachhilfe gegeben, aber jetzt hast du diese Woche keine Zeit mehr für deine Aufgaben." +
        //        "Tausche den Platz mit dem letzten Spieler (wenn du ganz hinten sitzt, passiert nichts)."
        //"Deine Wasserflasche ist kaputt. Dein Laptop auch. Würfle, um zu sehen, ob dein alter Laptop noch funktioniert. Falls nicht, setze z.B. 3x aus."


        int length = playingCards.length;
        Random random = new Random();
        int randomInt = random.nextInt(length);

        return playingCards[randomInt];
    }


}
