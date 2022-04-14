package GameLogic;

import Client.ClientReceive;
import Server.ClientHandler;
import Server.ServerManager;
import Server.ServerReceive;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;
import java.util.Random;

import java.util.HashMap;

/**
 * this class handles everything related to creating andS joining a lobby.
 */
public class CreateLobbyHelper {

    private final SendToClient sendToClient = new SendToClient();
    public final ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();
    private final ClientHandler clienthandler;


    Server.User user;

    //add createLobby
    public CreateLobbyHelper(Server.ClientHandler clienthandler) {
        this.clienthandler = clienthandler;
        this.user = clienthandler.user;
    }

    /**
     * This method first prints a list of all the open lobbies, then proceeds to ask if the user wants to create a new lobby.
     * After that the user is asked what lobby they want to join (via the lobby number (begins with zero)).
     *
     * @param clienthandler sends and receives the information through the clienthandler.
     */
    public void askWhatLobbyToJoin(Server.ClientHandler clienthandler) {
        user.setLobby(GameList.getLobbyList().get(0));
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
            connectLobby(answer2);
        }

        sendToClient.send(clienthandler, CommandsToClient.PRINT, "what's the number of the lobby which you " +
                "would like to choose? ");
        String answer = receiveFromClient.receive();
        int number = Integer.parseInt(answer);
        if (checkIfLobbyExists(number)) {
            user.setLobby(GameList.getOpenLobbies().get(number));
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                    + user.getLobby().getLobbyName() + ".");
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Oooops sorry there this Lobby doesn't exist. " +
                    "Please try again.");
            String answer3 = receiveFromClient.receive();
            int number3 = Integer.parseInt(answer3);
            if (checkIfLobbyExists(number3)) {
                user.setLobby(GameList.getOpenLobbies().get(number3));
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                        + user.getLobby().getLobbyName() + ".");
            } else {
                user.setLobby(GameList.getLobbyList().get(0));
                sendToClient.send(clienthandler, CommandsToClient.PRINT, " Sorry, this lobby doesn't exist," +
                        "you have been added to lobby "
                        + user.getLobby().getLobbyName() + ".");
            }
        }
    }

    public void readyToPlay(Server.ClientHandler clienthandler) {
        user.setReadyToPlay(true);
        user.getLobby().waitingToPlay(clienthandler);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now waiting…");
    }


    /**
     * checks if a lobby exists.
     * @param number The number of the lobby
     * @return boolean true (Lobby already exists) or false (lobby doesn't exist yet)
     */
    public boolean checkIfLobbyExists(int number) {
        HashMap<Integer, Lobby> lobbyList = GameList.getLobbyList();
        if (number >= lobbyList.size() || number < 0) {
            return false;
        } else {
            return true;
        }
    }

    public synchronized void changeLobby(String number) {
        int lobbyNumber = Integer.parseInt(number);
        if (lobbyNumber >= GameList.getLobbyList().size() || lobbyNumber < 0) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Whoops that lobby does not exist. ");
        }
        for (int i = 0; i < GameList.getLobbyList().size(); i++) {
            if (GameList.getLobbyList().get(i).getLobbyNumber() == lobbyNumber) {
                user.setLobby(GameList.getLobbyList().get(i));
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now member of Lobby : "  +
                        GameList.getLobbyList().get(i).getLobbyName());
            }
        }
    }


    /**
     * connects puts a newly created lobby into the lobbyList.
     *
     * @param name name of the lobby
     */
    public synchronized void connectLobby(String name) {
        Lobby lobby = new Lobby(name);
        GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have created Lobby " + lobby.getLobbyName());
    }

    public synchronized void printUserListAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "UserList: " + GameList.printUserList());
    }

    public synchronized void printLoungingListAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLoungingList());
    }

    public synchronized void printOpenLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getOpenLobbies()));
    }

    public synchronized void printFinishedLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getFinishedLobbies()));
    }

    public synchronized void printOnGoingLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getOnGoingLobbies()));
    }

    public synchronized void printLobbiesAndSendToClient() {
        sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printLobbies(GameList.getLobbyList()));
    }

}
