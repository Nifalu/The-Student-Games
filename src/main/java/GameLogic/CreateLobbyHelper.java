package GameLogic;

import Client.ClientReceive;
import Server.ClientHandler;
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
    Game Game;


    Server.User user;

    //add createLobby
    public CreateLobbyHelper(Server.ClientHandler clienthandler) {
        this.clienthandler = clienthandler;
    }

    /**
     * This method first prints a list of all the open lobbies, then proceeds to ask if the user wants to create a new lobby.
     * After that the user is asked what lobby they want to join (via the lobby number (begins with zero)).
     * @param clienthandler sends and receives the information through the clienthandler.
     */
    public void askWhatLobbyToJoin(Server.ClientHandler clienthandler) {
        if (GameList.getOpenLobbies().size() == 0) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT,"There are no open Lobbies yet.");
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT,"Hello there, here are the open lobbies:");
            HashMap<Integer, Lobby> openLobbies = GameList.getOpenLobbies();
            String openLobbyList = GameList.printLobbies(openLobbies);
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
        if (checkIfLobbyExists(answer)) {
            int number = Integer.parseInt(answer);
            GameList.getOpenLobbies().get(number).addUserToLobby(user);
            GameList.getUserInLobby().put(user, number);
            //GameList.getLobbyList().get(number).addUserToLobby(user);
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                    + GameList.getLobbyList().get(number).getLobbyName() + ".");
            //System.out.print("lobbyList size: " + GameList.getLobbyList().size());
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Oooops sorry there this Lobby doesn't exist. " +
                    "Please try again.");
            String answer3 = receiveFromClient.receive();
            int number2 = Integer.parseInt(answer3);
            if (checkIfLobbyExists(answer3)) {
                GameList.getOpenLobbies().get(number2).addUserToLobby(user);
                GameList.getUserInLobby().put(user, number2);
                //GameList.getLobbyList().get(number2).addUserToLobby(user);
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                        + GameList.getOpenLobbies().get(number2).getLobbyName() + ".");
        }
        Random random = new Random();
        int randomLobby = random.nextInt(GameList.getOpenLobbies().size());
        GameList.getOpenLobbies().get(randomLobby).addUserToLobby(user);
        GameList.getUserInLobby().put(user, randomLobby);
            //GameList.getLobbyList().get(randomLobby).addUserToLobby(user);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, " Sorry, this lobby doesn't exist, " +
                "we have added you randomly to another lobby.");
        sendToClient.send(clienthandler, CommandsToClient.PRINT, " You have been added to lobby "
                + GameList.getOpenLobbies().get(randomLobby).getLobbyName() + ".");
        }
        //askIfReadyToPlay(clienthandler, Integer.parseInt(answer));
    }

    public void readyToPlay(Server.ClientHandler clienthandler) {
        int lobbyNumber = GameList.getUserInLobby().get(user);
        GameList.getOpenLobbies().get(lobbyNumber).waitingToPlay(clienthandler);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now waiting…");
        //Game Game = new Game(lobbyNumber);
        //Thread GameThread = new Thread(Game);
        //GameThread.run();
    }

    public void startGame(Server.ClientHandler clienthandler) {
        int lobbyNumber = GameList.getUserInLobby().get(user);
        Game Game = new Game(lobbyNumber);
        Game.startGame();
    }

    /**
     * checks if a lobby exists.
     * @param number The number of the lobby
     * @return boolean true (Lobby already exists) or false (lobby doesn't exist yet)
     */
    public boolean checkIfLobbyExists(String number) {
        int number1 = Integer.parseInt(number);
        HashMap<Integer, Lobby> lobbyList = GameList.getLobbyList();
        return number1 <= lobbyList.size();
    }


    /**
     * connects puts a newly created lobby into the lobbyList.
     * @param name name of the lobby
     */
    public synchronized void connectLobby(String name) {
        Lobby lobby = new Lobby(name);
        GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
        sendToClient.send(clienthandler, CommandsToClient.PRINT,"You have created Lobby " + lobby.getLobbyName());
    }

}
