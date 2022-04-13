package GameLogic;
import Server.ServerManager;
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
    private int status;
    final private int lobbyNumber;
    /**
     * contains a HashMap of all the users in the Lobby
     */

    // this is now a Method in GameList
    //public HashMap<Integer, User> usersInLobby = new HashMap<>();


    // beide werden nachher nicht mehr vorhanden sein.
    HashMap<Server.User, Integer> lobbyUserIsIn = new HashMap<>();
    HashMap<Integer, Server.User> usersReady = new HashMap<>();

    public Lobby(String name) {
        this.name = name;
        this.status = 1; // status of lobby is automatically set to open
        this.lobbyNumber = GameList.getLobbyList().size();
    }


    // ------------------------ GETTERS -----------------------------------

    // muss man nachher noch löschen
    public HashMap<Integer, User> getUsersReady() {
        return usersReady;
    }

    public int getLobbyNumber() { return lobbyNumber; }

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

    // --------------------------ANDERE METHODEN-------------------------------

    /**
     * adds a user to the open lobby (to the list)
     * @param user the user which is added to the lobby in the method
     * */
    public void addUserToLobby(Server.User user) {
        user.setLobby(this);
    }

    public void removeUserFromLobby(Server.User user) {
        user.setLobby(null); // basic Lobby
    }


    public void waitingToPlay(Server.ClientHandler clientHandler) {
        if (!usersReady.containsValue(clientHandler)) {
            int size = usersReady.size();
            usersReady.put(size, clientHandler.user);
        }
    }
}
