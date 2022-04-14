package GameLogic;
import Server.ServerManager;
import Server.User;
import utility.IO.ReceiveFromProtocol;

import java.util.HashMap;

/**
 * creates a Lobby Object
 */

public class Lobby {

    String name;
    Game game;
    public ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

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


    // beide werden nacher nicht mehr vorhanden sein.
    HashMap<Integer, Server.User> usersReady = new HashMap<>();

    public Lobby(String name) {
        this.name = name;
        this.status = 1; // status of lobby is automatically set to open
        startThread();
        this.lobbyNumber = GameList.getLobbyList().size();
    }


    // ------------------------ GETTERS -----------------------------------

    // muss man nachher noch löschen
    public HashMap<Integer, User> getUsersReady() {
        return usersReady;
    }

    public HashMap<Integer, User> getUsersInLobby() {
        HashMap<Integer, User> usersInLobby = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < GameList.getUserlist().size(); i++) {
            if (GameList.getUserlist().get(i).getLobby().equals(this)) {
                usersInLobby.put(counter, GameList.getUserlist().get(i));
                counter++;
            }
        }
        return usersInLobby;
    }

    public int getLobbyNumber() { return lobbyNumber; }

    public String getLobbyName() {
        return name;
    }

    public int getLobbyStatus() {
        return status;
    }

    public boolean getPlayerStillActiv (Server.User user) {
        boolean alive = false;
        for (Server.User players: getUsersReady().values()) {
            if (players.equals(user)) {
                alive = true;
                break;
            }
        }
        return alive;
    }


    // ----------------------- SETTERS ----------------------------------------

    public void setLobbyStatusToOpen() {
        status = 1;
    }

    public void setLobbyStatusToFinished() {
        status = -1;
    }

    public void setLobbyStatusToOnGoing() { status = 0; }

    public void setLobbyStatusToStandard() { status = 69; }

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
        if (!usersReady.containsValue(clientHandler) && getLobbyStatus() == 1) {
            int size = usersReady.size();
            usersReady.put(size, clientHandler.user);
        }
    }

    //private void startGame() {
    //    game = new Game(this, usersReady);
    //    Thread gameThread = new Thread(game);
    //    gameThread.start();
    //}

    public void startThread() {
        Thread LobbyWaitForMessageThread = new Thread(() -> {
            String msg;
            String[] answer;
            while(true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                answer = msg.split("-");
                if (msg.equals("start") && getLobbyStatus() == 1) {
                    game = new Game(this, usersReady);
                    Thread gameThread = new Thread(game);
                    gameThread.start();
                }
                else if (answer[0].equals("dice")) {
                    game.setRolledDice(answer[1], 6);
                }
                else if (answer[0].equals("dicedice")) {
                    game.setRolledDice(answer[1], 4);
                }
                else if (answer[0].equals("quiz")) {
                    game.quizAnswer(answer[1], answer[2]);
                }
            }
        });
        LobbyWaitForMessageThread.setName("LobbyWaitForMessageThread"); // set name of thread
        LobbyWaitForMessageThread.start(); // start thread
    }
}

