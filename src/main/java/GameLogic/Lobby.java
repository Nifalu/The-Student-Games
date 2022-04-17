package GameLogic;

import Server.User;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * creates a Lobby Object
 */

public class Lobby {

    final int minToStart = 2;
    String name;
    Game game;
    private final SendToClient sendToClient = new SendToClient();
    public ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

    /**
     * Int status is -1 if the game is already finished, 0 if the game is ongoing, 1 if the game is open.
     */
    private int status;
    final private int lobbyNumber;
    /**
     * contains a HashMap of all the users in the Lobby
     */

    HashMap<Integer, Server.User> usersReady = new HashMap<>();

    public Lobby(String name) {
        this.name = name;
        this.status = 1; // status of lobby is automatically set to open
        startThread();
        this.lobbyNumber = GameList.getLobbyList().size();
    }


    // ------------------------ GETTERS -----------------------------------

    public HashMap<Integer, User> getUsersReady() {
        return usersReady;
    }

    public HashMap<Integer, User> getUsersInLobby() {
        HashMap<Integer, User> usersInLobby = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < GameList.getUserlist().size(); i++) {
            try {
                if (GameList.getUserlist().get(i).getLobby().equals(this)) {
                    usersInLobby.put(counter, GameList.getUserlist().get(i));
                    counter++;
                }
            } catch (Exception e) { }
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

    /**
     * Checcks if a user is in a lobby. If it is true it will set the player to the waiting list
     * and sends a message to all users with its lobby
     * @param clientHandler User that is ready to play
     */
    public void readyToPlay(Server.ClientHandler clientHandler) {
        if (getLobbyStatus() == 1) {
            clientHandler.user.setReadyToPlay(true);
            waitingToPlay(clientHandler);
            sendToClient.send(clientHandler, CommandsToClient.PRINT, "You are now waiting…");
                lobbyBroadcastToPlayer(clientHandler.user.getUsername() + " is ready for a Game in Lobby: "
                        + clientHandler.user.getLobby().getLobbyName());
                lobbyBroadcastToPlayer("People in the Lobby " + clientHandler.user.getLobby().getLobbyName() + ": " +
                        clientHandler.user.getLobby().getUsersInLobby().size() + "; People ready: " + clientHandler.user.getLobby().getUsersReady().size());
        } else {
            sendToClient.send(clientHandler, CommandsToClient.PRINT, "please choose a lobby");
        }
    }

    /**
     * Sets a player to the waiting list of the lobby
     * @param clientHandler User who is ready to play the game.
     */
    public void waitingToPlay(Server.ClientHandler clientHandler) {
        if (!usersReady.containsValue(clientHandler) && getLobbyStatus() == 1) {
            int size = usersReady.size();
            clientHandler.user.setReadyToPlay(true);
            usersReady.put(size, clientHandler.user);
        }
    }

    /**
     * Removes a user from the waiting list
     * @param clientHandler User who is not ready to play the game anymore.
     */
    public void removeFromWaitingList(Server.ClientHandler clientHandler) {
        clientHandler.user.setReadyToPlay(false);
        sendToClient.send(clientHandler, CommandsToClient.PRINT, "You are not waiting anymore.");
        lobbyBroadcastToPlayer(clientHandler.user.getUsername() + " is not ready.");
        usersReady.values().remove(clientHandler.user);
        lobbyBroadcastToPlayer("People in the Lobby " + clientHandler.user.getLobby().getLobbyName() + ": " +
                clientHandler.user.getLobby().getUsersInLobby().size() + "; People ready: " + clientHandler.user.getLobby().getUsersReady().size());
    }

    /**
     * Receives all commands from the users of the lobby and sends it to its game.
     */
    public void startThread() {
        Thread LobbyWaitForMessageThread = new Thread(() -> {
            String msg;
            String[] answer;
            while(true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                answer = msg.split("§");
                if (getLobbyStatus() != 69) {
                    if (msg.equals("start") && getLobbyStatus() == 1) {
                        if (usersReady.size() >= minToStart) {
                            setLobbyStatusToOnGoing();
                            game = new Game(this, usersReady);
                            Thread gameThread = new Thread(game);
                            gameThread.start();
                        }
                    } else if (getLobbyStatus() == 0) {
                        switch (answer[0]) {
                            case "dice":
                                game.setRolledDice(answer[1], 6);
                                break;
                            case "dicedice":
                                game.setRolledDice(answer[1], 4);
                                break;
                            case "quiz":
                                game.quizAnswer(answer[1], answer[2]);
                                break;
                            case "wwcd":
                                game.cheat(answer[1], Integer.parseInt(answer[2]));
                                break;
                        }
                    }
                }
            }
        });
        LobbyWaitForMessageThread.setName("LobbyWaitForMessageThread"); // set name of thread
        LobbyWaitForMessageThread.start(); // start thread
    }

    public boolean getIsReadyToStartGame() {
        return (usersReady.size() >= minToStart);
    }

    /**
     * Sends a message to all lobby members
     * @param msg Message to be sent to the users
     */
    public void lobbyBroadcastToPlayer(String msg) {
        sendToClient.lobbyBroadcastDice(getUsersInLobby(), CommandsToClient.PRINT, msg);
    }

}

