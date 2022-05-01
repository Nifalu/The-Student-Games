package gameLogic;

import server.User;
import utility.io.CommandsToClient;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToClient;

import java.util.HashMap;

/**
 * creates a Lobby Object
 */
public class Lobby {

    /**
     * amount of players needed to start a game
     */
    final int minToStart = 2;

    /**
     * the name of the lobby
     */
    String name;

    /**
     * the corresponding game to the lobby
     */
    Game game;

    /**
     * SendToClient object to communicate with the client
     */
    private final SendToClient sendToClient = new SendToClient();

    /**
     * ReceiveFromProtocol object to communicate with the protocol
     */
    public ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

    /**
     * the highscore of the lobby
     */
    public static HighScore highScore = new HighScore();

    /**
     * the game status
     * Int status is -1 if the game is already finished, 0 if the game is ongoing, 1 if the game is open.
     */
    private int status;

    /**
     * the number of the lobby
     */
    final private int lobbyNumber;

    /**
     * contains a HashMap of all the users in the Lobby
     */
    HashMap<Integer, server.User> usersReady = new HashMap<>();

    /**
     * used to give each player joining the lobby a different character
     * +1 when a color has been given away
     * characterColorCounter is used as an index to the characterColors array
     */
    private int characterColorCounter = 0;

    /**
     * the possible colors for a player character
     */
    private String[] characterColors = new String[]{"red", "blue", "yellow", "green"};

    /**
     * creates a Lobby object with the given name
     *
     * @param name of the lobby
     */
    public Lobby(String name) {
        this.name = name;
        this.status = 1; // status of lobby is automatically set to open
        startThread();
        this.lobbyNumber = GameList.getLobbyList().size();
    }


    // ------------------------ GETTERS -----------------------------------

    /**
     * Creates a HashMap with all the users that are ready.
     *
     * @return the HashMap with alle the users that are ready.
     */
    public HashMap<Integer, User> getUsersReady() {
        return usersReady;
    }

    /**
     * creates a HashMap with all the users that in that lobby
     *
     * @return the HashMap with all the users in the lobby
     */
    public HashMap<Integer, User> getUsersInLobby() {
        HashMap<Integer, User> usersInLobby = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < GameList.getUserlist().size(); i++) {
            try {
                if (GameList.getUserlist().get(i).getLobby().equals(this)) {
                    usersInLobby.put(counter, GameList.getUserlist().get(i));
                    counter++;
                }
            } catch (Exception e) {
                System.out.println("Could not get user in lobby");
            }
        }
        return usersInLobby;
    }

    /**
     * returns the number of the lobby
     *
     * @return the number of the lobby as int.
     */
    public int getLobbyNumber() {
        return lobbyNumber;
    }

    /**
     * returns the name of the lobby
     *
     * @return the name of the lobby as String.
     */
    public String getLobbyName() {
        return name;
    }

    /**
     * returns the status of the lobba as an int
     * 0 for all the ongoing lobbies, 1 for all the open lobbies, -1 for all the finished lobbies
     * and 69 for the standard lobby.
     *
     * @return int: lobbystatus
     */
    public int getLobbyStatus() {
        return status;
    }


    // ----------------------- SETTERS ----------------------------------------

    /**
     * sets lobby status to open (int 1), but is rarely used due to lobbies automatically being assigned open at the
     * beginning.
     */
    public void setLobbyStatusToOpen() {
        status = 1;
    }

    /**
     * sets lobby status to finished (int -1)
     */
    public void setLobbyStatusToFinished() {
        status = -1;
    }

    /**
     * sets lobby status to ongoing (int 0)
     */
    public void setLobbyStatusToOnGoing() {
        status = 0;
    }

    /**
     * sets lobby status to standard (int 69)
     */
    public void setLobbyStatusToStandard() {
        status = 69;
    }

    // --------------------------ANDERE METHODEN-------------------------------

    /**
     * adds a user to the open lobby (to the list)
     *
     * @param user the user which is added to the lobby in the method
     */
    public void addUserToLobby(server.User user) {
        user.setLobby(this);
    }

    /**
     * removes a user from a lobby and puts the user into the standard lobby.
     * Users shouldn't be in no lobby.
     *
     * @param user that is removed
     */
    public void removeUserFromLobby(server.User user) {
        user.setLobby(GameList.getLobbyList().get(0)); // basic Lobby
    }

    /**
     * Checcks if a user is in a lobby. If it is true it will set the player to the waiting list
     * and sends a message to all users with its lobby
     *
     * @param clientHandler User that is ready to play
     */
    public void readyToPlay(server.ClientHandler clientHandler) {
        if (getLobbyStatus() == 1) {
            waitingToPlay(clientHandler);
            sendToClient.send(clientHandler, CommandsToClient.PRINTGUIGAMETRACKER, "You are now waiting...");
            clientHandler.user.characterColor = characterColors[characterColorCounter]; // gives the player a color
            characterColorCounter += 1;
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
     *
     * @param clientHandler User who is ready to play the game.
     */
    public void waitingToPlay(server.ClientHandler clientHandler) {
        if (!usersReady.containsValue(clientHandler.user) && getLobbyStatus() == 1) {
            int size = usersReady.size();
            clientHandler.user.setReadyToPlay(true);
            usersReady.put(size, clientHandler.user);
        }
    }

    /**
     * Removes a user from the waiting list
     *
     * @param clientHandler User who is not ready to play the game anymore.
     */
    public void removeFromWaitingList(server.ClientHandler clientHandler) {
        usersReady.values().remove(clientHandler.user);
        clientHandler.user.setReadyToPlay(false);
        sendToClient.send(clientHandler, CommandsToClient.PRINT, "You are not waiting anymore.");
        clientHandler.user.characterColor = ""; // removes the players color
        characterColorCounter -= 1;
        lobbyBroadcastToPlayer(clientHandler.user.getUsername() + " is not ready.");
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
            while (true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                answer = msg.split("§");
                if (getLobbyStatus() != 69) {
                    if (msg.equals("start") && getLobbyStatus() == 1) { // starts the game
                        if (usersReady.size() >= minToStart) {
                            setLobbyStatusToOnGoing();
                            game = new Game(this, usersReady, highScore);
                            Thread gameThread = new Thread(game);
                            gameThread.start();

                            // GAME STARTS HERE
                        }
                    } else if (getLobbyStatus() == 0) {
                        switch (answer[0]) {
                            case "dice": // roll normal dice
                                game.setRolledDice(answer[1], 6);
                                break;
                            case "dicedice": // roll special dice
                                game.setRolledDice(answer[1], 4);
                                break;
                            case "quiz": // quiz answer
                                game.quizAnswer(answer[1], answer[2]);
                                break;
                            case "wwcd": // cheat code
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

    /**
     * Returns a String with the highscore of the top 10 players or "empty High Score" if high score is empty.
     *
     * @param clientHandler User asking for the high score
     */
    public void getHighScore(server.ClientHandler clientHandler) {
        if (highScore.getTop10().length() == 0) {
            sendToClient.send(clientHandler, CommandsToClient.PRINT, "empty High Score");
        } else {
            sendToClient.send(clientHandler, CommandsToClient.PRINT, highScore.getTop10());
        }
    }

    /**
     * returns whether enough people have joined a game to start it or not
     *
     * @return boolean
     */
    public boolean getIsReadyToStartGame() {
        return (usersReady.size() >= minToStart);
    }

    /**
     * Sends a message to all lobby members
     *
     * @param msg Message to be sent to the users
     */
    public void lobbyBroadcastToPlayer(String msg) {
        sendToClient.lobbyBroadcast(getUsersInLobby(), CommandsToClient.PRINT, msg);
    }
}
