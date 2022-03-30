package GameLogic;

import Server.User;

import java.util.HashMap;
import java.util.Map;

/**
 * The board is made up of 90 fields, divided into 30 fields per academic year.
 * There are ladders where you skip fields or fall back if you land on them.
 */
public class PlayingFields {
    public HashMap<User, Integer> position = new HashMap<>();

    public void startGame(User user) {
        position.put(user,0);
    }

    public void changePosition(User user, int move) {
        int currentPosition = position.get(user);
        int newPosition = currentPosition + move;
        if (position.containsValue(newPosition)) {
            // iterate each entry of hashmap
            for(Map.Entry<User, Integer> entry: position.entrySet()) {

                // if give value is equal to value from entry
                // position will be switched
                if(entry.getValue() == newPosition) {
                    position.replace(entry.getKey(), currentPosition);
                    break;
                }
            }
        }
        position.replace(user, currentPosition + move);
        checkField(user, position.get(user));
    }

    public void checkField(User user, int field) {
        // 2 + 56 ladder up
        if (field == 2) {
            changePosition(user, 15 - field);
        } else if (field == 56) {
            changePosition(user, 59 - field);
        } // 21 - 89 ladder down
        else if (field == 21) {
            changePosition(user, 14 - field);
        } else if (field == 27) {
            changePosition(user, 10 - field);
        } else if (field == 53) {
            changePosition(user, 36 - field);
        } else if (field == 58) {
            changePosition(user, 40 - field);
        } else if (field == 81) {
            changePosition(user, 78 - field);
        } else if (field == 89) {
            changePosition(user, 68 - field);
        } // Ereigniskarten
        else if (field == 18 || field == 46 || field == 74) {

        } // Quiz
        else if (field == 23 || field == 50) {

        } else if (field > 90) {

        }
    }

}
