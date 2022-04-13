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
/*
public class PlayingFields {

    private static final SendToClient sendToClient = new SendToClient();
    public static HashMap<User, Integer> position = new HashMap<>();

    public static void putPlayersToStart(User user) { position.put(user,0); }

    //receives the rolled number and changes the position of the current player.
    public static void changePosition(User user, int move) {
        int currentPosition = position.get(user);
        int newPosition = currentPosition + move;

        //checks if the new position is already occupied and switches places if necessary.
        if (position.containsValue(newPosition) && newPosition <= 90) {
            for(Map.Entry<User, Integer> entry: position.entrySet()) {
                if(entry.getValue() == newPosition) {
                    position.replace(entry.getKey(), currentPosition);
                    sendAll(user.getUsername() + " pushed back " + entry.getKey().getUsername() + " to " + currentPosition);
                }
            }
        }

        //puts the player to the new position
        position.replace(user, newPosition);
        if (newPosition <= 90) {
            sendAll(user.getUsername() + " moved from: " + currentPosition + " to " + newPosition);
        } else {
            sendAll(user.getUsername() + " moved from: " + currentPosition + " to Bachelorfeier");
        }
        checkField(user, newPosition);
    }

    //checks if the new field is a special one
    public static void checkField(Server.User user, int field) {

        // 2 + 56 ladder up
        String msg = null;
        if (field == 2) {
            sendAll(user.getUsername() + ": Leiter hoch");
            changePosition(user, 15 - field);
        } else if (field == 56) {
            sendAll(user.getUsername() + ": Leiter hoch");
            changePosition(user, 59 - field);
        }
        // 21 - 89 ladder down
        else if (field == 21) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 14 - field);
        } else if (field == 27) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 10 - field);
        } else if (field == 53) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 36 - field);
        } else if (field == 58) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 40 - field);
        } else if (field == 81) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 78 - field);
        } else if (field == 89) {
            sendAll(user.getUsername() + ": Leiter runter");
            changePosition(user, 68 - field);
        }
        //Cards
        else if (field == 18 || field == 46 || field == 74) {
            String card = Cards.getCards();
            String arr[] = card.split(" ", 2);
            int positionToChange = Integer.parseInt(arr[0]);
            String textKarten = arr[1];
            sendAll(user.getUsername() + " zieht eine Ereigniskarte: " + textKarten);
            changePosition(user, positionToChange);
        }
        // Quiz
        else if (field == 23 || field == 50) {
            String quizQuestion = Quiz.quiz();
            String quiz[] = quizQuestion.split("§");
            sendAll(quiz[0]);
            changePosition(user, Integer.parseInt(quiz[2]));
        }
        // This is the end
        else if (field > 90) {
            sendAll(user.getUsername() + " has successfully graduated from university");
        }
    }

    //returns the position of the user
    public static int getPosition(User user) {
        return position.get(user);
    }

    //sends the given message to all players of this game
    public static void sendAll(String msg) {
        sendToClient.lobbyBroadcast(position, CommandsToClient.PRINT, msg);
    }
}
*/
