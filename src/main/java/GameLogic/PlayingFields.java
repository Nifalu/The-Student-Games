package GameLogic;

import Server.ClientHandler;
import Server.User;
import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The board is made up of 90 fields, divided into 30 fields per academic year.
 * There are ladders where you skip fields or fall back if you land on them.
 */
public class PlayingFields {

    private final SendToClient sendToClient = new SendToClient();

    public static HighScore HighScore = new HighScore();

    public static HashMap<User, Integer> position = new HashMap<>();

    public static void putPlayersToStart(User user) { position.put(user,0); }

    public static void changePosition(User user, int move) {
        int currentPosition = position.get(user);
        int newPosition = currentPosition + move;
        if (position.containsValue(newPosition) && newPosition <= 90) {
            // iterate each entry of hashmap
            for(Map.Entry<User, Integer> entry: position.entrySet()) {

                // If given value is equal to value from entry
                // position will be switched
                if(entry.getValue() == newPosition) {
                    position.replace(entry.getKey(), currentPosition);
                    System.out.println(user.getUsername() + " pushed back " + entry.getKey().getUsername() + " to " + currentPosition);
                }
            }
        }
        position.replace(user, newPosition);
        if (newPosition <= 90) {
            System.out.println(user.getUsername() + " moved from: " + currentPosition + " to: " + newPosition);
            //sendToClient.send(clienthandler, CommandsToClient.PRINT, "Your new position: " + newPosition);
        }
        checkField(user, newPosition);

    }

    public static void checkField(Server.User user, int field) {
        // 2 + 56 ladder up
        if (field == 2) {
            System.out.println("Leiter hoch");
            changePosition(user, 15 - field);
        } else if (field == 56) {
            System.out.println("Leiter hoch");
            changePosition(user, 59 - field);
        } // 21 - 89 ladder down
        else if (field == 21) {
            System.out.println("Leiter runter");
            changePosition(user, 14 - field);
        } else if (field == 27) {
            System.out.println("Leiter runter");
            changePosition(user, 10 - field);
        } else if (field == 53) {
            System.out.println("Leiter runter");
            changePosition(user, 36 - field);
        } else if (field == 58) {
            System.out.println("Leiter runter");
            changePosition(user, 40 - field);
        } else if (field == 81) {
            System.out.println("Leiter runter");
            changePosition(user, 78 - field);
        } else if (field == 89) {
            System.out.println("Leiter runter");
            changePosition(user, 68 - field);
        } // Ereigniskarten
        else if (field == 18 || field == 46 || field == 74) {
            String card = Cards.getCards();
            String arr[] = card.split(" ", 2);
            int positionToChange = Integer.parseInt(arr[0]);
            String msg = arr[1];
            //TODO Print out message
            //user.send(user, CommandsToClient.PRINT, msg);
            //Server.ClientHandler.send(ServerProtocol.get(game, user, msg));
            System.out.println("Ereigniskarte: " + msg);
            changePosition(user, positionToChange);
        // Quiz
        /*
        }
        else if (field == 23 || field == 50) {
            int positionToChange = Quiz.quiz();
            changePosition(user, positionToChange);
         */
        } // This is the end
        else if (field > 90) {
            System.out.println(user.getUsername() + " Finished the game");
            HighScore.add("vladimir", 1);
        }
    }
    public static int getPosition(User user) {
        return position.get(user);
    }

}
