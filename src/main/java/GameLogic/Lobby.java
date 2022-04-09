package GameLogic;
import Server.User;

import java.util.HashMap;

/**
 * creates a Lobby Object
 */

public class Lobby {

    String name;
    Server.User user;
    /**
     * Int status is -1 if the game is already finished, 0 if the game is ongoing, 1 if the game is open.
     */
    int status;
    /**
     * contains a HashMap of all the users in the Lobby
     */
    HashMap<Integer, Server.User> usersInLobby = new HashMap<>();

    public Lobby(String name) {
        this.name = name;
        this.status = 1; // status of lobby is automatically set to open
    }


    // ------------------------ GETTERS -----------------------------------

    public HashMap<Integer, User> getUsersInLobby() {
        return usersInLobby;
    }

    public String getLobbyName() {
        return name;
    }

    public int getLobbyStatus() {
        return status;
    }

    // ----------------------- SETTERS ----------------------------------------

    public void setLobbyStatusToOpen() {
        status = 1;
    }

    public void setLobbyStatusToFinished() {
        status = -1;
    }

    public void setLobbyStatusToOnGoing() { status = 0; }

    // --------------------------------------------------------------------------

    /**
     * adds a user to the open lobby (to the list)
     * @param user the user which is added to the lobby in the method
     */
    public void addUserToLobby(Server.User user) {
        int size = usersInLobby.size();
        usersInLobby.put(size, user);
    }
}
