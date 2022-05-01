package gameLogic;

import server.ClientHandler;
import utility.io.CommandsToClient;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToClient;

/**
 * this class handles everything related to creating andS joining a lobby.
 */
public class CreateLobbyHelper {

    /**
     * sendToClient object to communicate with the client
     */
    private final SendToClient sendToClient = new SendToClient();

    /**
     * receiveFromProtocol object to receive messages from the client
     */
    public final ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();

    /**
     * knows the clientHands
     */
    private final ClientHandler clienthandler;

    /**
     * knows the user
     */
    server.User user;

    /**
     * add createLobby
     * @param clienthandler server.ClientHandler
     */
    public CreateLobbyHelper(ClientHandler clienthandler) {
        this.clienthandler = clienthandler;
    }

    /**
     * This method first prints a list of all the open lobbies, then proceeds to ask if the user wants to create a new lobby.
     * After that the user is asked what lobby they want to join (via the lobby number (begins with zero)).
     *
     * @param clienthandler sends and receives the information through the clienthandler.
     */
    public void askWhatLobbyToJoin(ClientHandler clienthandler) {
        if (GameList.getOpenLobbies().size() == 0) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "There are no open Lobbies yet.");
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Hello there, here are the open lobbies:");
            String openLobbyList = GameList.printLobbies(GameList.getOpenLobbies());
            sendToClient.send(clienthandler, CommandsToClient.PRINT, openLobbyList);
        }
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "Would you like to create your own lobby?");
        String answer1 = receiveFromClient.receive();
        if (answer1.equalsIgnoreCase("YES")) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Enter Name of the lobby below:");
            String answer2 = receiveFromClient.receive();
            answer2 = answer2.replaceAll(" ", "_").toLowerCase();
            connectLobby(answer2);
        }

        sendToClient.send(clienthandler, CommandsToClient.PRINT, "What's the number of the lobby which you " +
                "would like to choose? ");
        String answer = receiveFromClient.receive();
        changeLobby(answer);
    }

    /**
     * Checks if a lobby exists.
     * @param number The number of the lobby
     * @return boolean true (Lobby already exists) or false (lobby doesn't exist yet)
     */
    public boolean checkIfOpenLobbyExists(String number) {
        try {
            int lobbyNumber = Integer.parseInt(number);
            return lobbyNumber < GameList.getOpenLobbies().size() && lobbyNumber >= 0;
        } catch (NumberFormatException e) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, number + " is not a valid number!");
            return false;
        }
    }

    /**
     * Changes the lobby of a user and checks if the lobby exists.
     * @param number The number of the new Lobby
     */
    public synchronized void changeLobby(String number) {
            number = number.replaceAll("\\s", "");
            if (checkIfOpenLobbyExists(number)) {
                int lobbynumber = Integer.parseInt(number);
                clienthandler.user.setLobby(GameList.getOpenLobbies().get(lobbynumber));
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now member of Lobby: " +
                        GameList.getOpenLobbies().get(lobbynumber).getLobbyName());
            } else {
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "Whoops that lobby does not exist. ");
            }

    }


    /**
     * Connects puts a newly created lobby into the lobbyList.
     * @param name name of the lobby
     */
    public synchronized void connectLobby(String name) {
        name = name.replaceAll(" ", "_").toLowerCase();
        Lobby lobby = new Lobby(name);
        GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have created Lobby " + lobby.getLobbyName());
    }

    /**
     * Prints a list containing all the users to the client.
     */
    public synchronized void printUserListAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "UserList: " + GameList.printUserList());
    }

    /**
     * Prints a list with all the lobbies with their status
     * and the players in the specific lobby and sends it to the client.
     */
    public synchronized void printLoungingListAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLoungingList());
        sendToClient.send(clienthandler, CommandsToClient.PRINTFRIENDSGUI, GameList.printLoungingList());
    }

    /**
     * Prints all the lobbies with the status open.
     */
    public synchronized void printOpenLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getOpenLobbies()));
    }

    /**
     * Prints all the lobbies with the status finished and sends it to the client.
     */
    public synchronized void printFinishedLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getFinishedLobbies()));
    }

    /**
     * Prints all the lobbies with the status ongoing and sends it to the client.
     */
    public synchronized void printOnGoingLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getOnGoingLobbies()));
    }

    /**
     * Prints all the lobbies and indicates their status and sends it to the client.
     */
    public synchronized void printLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getLobbyList()));
        sendToClient.send(clienthandler, CommandsToClient.PRINTLOBBIESGUI, GameList.printLobbies(GameList.getLobbyList()));
    }

    /**
     * Prints all the players that are in the same lobby as the client.
     */
    public synchronized void printPlayersInLobby () {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printUserInLobby(clienthandler.user.getLobby()));
    }

}
