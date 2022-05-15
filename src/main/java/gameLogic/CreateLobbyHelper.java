package gameLogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ClientHandler;
import server.ServerManager;
import server.User;
import utility.io.CommandsToClient;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToClient;

import java.util.HashMap;

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

  private final Logger logger = LogManager.getLogger(CreateLobbyHelper.class);

  /**
   * add createLobby
   *
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
    try {
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
    } catch (NullPointerException e) {
      logger.info("LobbyReceiver did not receive a name. Server probably shut down while waiting");
    }
  }

  /**
   * Checks if a lobby exists.
   *
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

  public boolean checkIfOngoingLobbyExists(String number) {
    try {
      int lobbyNumber = Integer.parseInt(number);
      return lobbyNumber < GameList.getOnGoingLobbies().size() && lobbyNumber >= 0;
    } catch (NumberFormatException e) {
      sendToClient.send(clienthandler, CommandsToClient.PRINT, number + " is not a valid number!");
      return false;
    }
  }

  /**
   * Changes the lobby of a user and checks if the lobby exists.
   *
   * @param number The number of the new Lobby
   */
  /*public synchronized void changeLobby(String number) {
    number = number.replaceAll("\\s", "");
    if (checkIfOpenLobbyExists(number)) {
      int lobbynumber = Integer.parseInt(number);
      clienthandler.user.setLobby(GameList.getOpenLobbies().get(lobbynumber));
      sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now member of Lobby: " +
          GameList.getOpenLobbies().get(lobbynumber).getLobbyName());
    } else if (checkIfOngoingLobbyExists(number)) {
      int lobbynumber = Integer.parseInt(number);
      clienthandler.user.setLobby(GameList.getOnGoingLobbies().get(lobbynumber));
      sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now a spectator of Lobby: " +
              GameList.getOnGoingLobbies().get(lobbynumber).getLobbyName());
    } else {
      sendToClient.send(clienthandler, CommandsToClient.PRINT, "Whoops that lobby does not exist. ");
    }
  }
   */
  public synchronized void changeLobby(String number) {
    number = number.replaceAll("\\s", "");

    // enables the players old char in their old lobby
    // the same as the ENABLECURRENTCHARGUI command in the ServerReiceive
    clienthandler.user.getLobby().getCharactersTaken().put(clienthandler.user.characterNr, false);
    sendToClient.lobbyBroadcast(clienthandler.user.getLobby().getUsersInLobby(), CommandsToClient.ENABLECHARGUI, Integer.toString(clienthandler.user.characterNr));


    HashMap<Integer, User> playersInLobby = clienthandler.user.getLobby().getUsersInLobby();
    Lobby lobby = clienthandler.user.getLobby();

    clienthandler.user.characterNr = 0;
    Lobby currentLobby = clienthandler.user.getLobby();
    HashMap<Integer, User> playersInOldLobby = clienthandler.user.getLobby().getUsersInLobby();


    int lobbynumber = Integer.parseInt(number);
    if (lobbynumber < GameList.getLobbyList().size() && lobbynumber >= 0) {
      if (GameList.getLobbyList().get(lobbynumber).getLobbyStatus() == 1) {
        clienthandler.user.setLobby(GameList.getLobbyList().get(lobbynumber));
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now member of Lobby: " +
            GameList.getLobbyList().get(lobbynumber).getLobbyName());

        Lobby l = clienthandler.user.getLobby();

        // disables the taken ones and enables the free ones
        for (Integer key: l.getCharactersTaken().keySet()) {
          Boolean taken = l.getCharactersTaken().get(key);
          if (taken) {
            sendToClient.send(clienthandler.user.getClienthandler(), CommandsToClient.DISABLECHARGUI, Integer.toString(key));
          } else {
            sendToClient.send(clienthandler.user.getClienthandler(), CommandsToClient.ENABLECHARGUI, Integer.toString(key));
          }
        }
      } else if (GameList.getLobbyList().get(lobbynumber).getLobbyStatus() == 0) {
        clienthandler.user.setLobby(GameList.getLobbyList().get(lobbynumber));
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now a spectator of Lobby: " +
            GameList.getLobbyList().get(lobbynumber).getLobbyName());
      }

      for (int i = 1; i < 5; i++) {
        sendToClient.send(clienthandler.user.getClienthandler(), CommandsToClient.SETCHARTOKEN, "0--" + i);
      }

      for (Integer key : playersInOldLobby.keySet()) {
        User currentUser = playersInOldLobby.get(key);
        if (currentUser.getIsReady()) {
          sendToClient.lobbyBroadcast(playersInOldLobby, CommandsToClient.SETCHARTOKEN, currentUser.characterNr + "--" + currentUser.gameTokenNr);
        }
      }

    } else {
      sendToClient.send(clienthandler, CommandsToClient.PRINT, "Whoops that lobby does not exist. ");
    }
  }


  /**
   * Connects puts a newly created lobby into the lobbyList.
   *
   * @param name name of the lobby
   */
  public synchronized void connectLobby(String name) {
    name = name.replaceAll(" ", "_");
    Lobby lobby = new Lobby(name, false);
    String num = String.valueOf(GameList.getLobbyList().size());
    GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
    sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have created Lobby " + lobby.getLobbyName());
    sendToClient.serverBroadcast(CommandsToClient.PRINTLOBBIESGUI,
        num + ". " + lobby.getLobbyName() +" [" + lobby.getLobbyStatusAsString() + "]" );
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
    sendToClient.serverBroadcast(CommandsToClient.PRINTFRIENDSGUI, GameList.printLoungingList());
  }

  /**
   * Prints all the lobbies with the status open.
   */
  public synchronized void printOpenLobbiesAndSendToClient() {
    for (HashMap.Entry<Integer, Lobby> entry : GameList.getOpenLobbies().entrySet()) {
      sendToClient.send(clienthandler, CommandsToClient.PRINT,
          entry.getKey() + ". " + entry.getValue().getLobbyName() + " [" + entry.getValue().getLobbyStatusAsString() + "]");
    }
  }

  /**
   * Prints all the lobbies with the status finished and sends it to the client.
   */
  public synchronized void printFinishedLobbiesAndSendToClient() {
    for (HashMap.Entry<Integer, Lobby> entry : GameList.getFinishedLobbies().entrySet()) {
      sendToClient.send(clienthandler, CommandsToClient.PRINT,
          entry.getKey() + ". " + entry.getValue().getLobbyName() + " [" + entry.getValue().getLobbyStatusAsString() + "]");
    }
  }

  /**
   * Prints all the lobbies with the status ongoing and sends it to the client.
   */
  public synchronized void printOnGoingLobbiesAndSendToClient() {
    for (HashMap.Entry<Integer, Lobby> entry : GameList.getOnGoingLobbies().entrySet()) {
      sendToClient.send(clienthandler, CommandsToClient.PRINT,
          entry.getKey() + ". " + entry.getValue().getLobbyName() + " [" + entry.getValue().getLobbyStatusAsString() + "]");
    }
  }

  /**
   * Prints all the lobbies and indicates their status and sends it to the client.
   */
  public synchronized void printLobbiesAndSendToClient() {
    for (HashMap.Entry<Integer, Lobby> entry : GameList.getLobbyList().entrySet()) {
      sendToClient.send(clienthandler, CommandsToClient.PRINT,
          entry.getKey() + ". " + entry.getValue().getLobbyName() + " [" + entry.getValue().getLobbyStatusAsString() + "]");
      sendToClient.send(clienthandler, CommandsToClient.PRINTLOBBIESGUI,
          entry.getKey() + ". " + entry.getValue().getLobbyName() + " [" + entry.getValue().getLobbyStatusAsString() + "]");
    }
    //sendToClient.send(clienthandler, CommandsToClient.PRINTLOBBIESGUI, GameList.printLobbies(GameList.getLobbyList()));
  }

  /**
   * Prints all the players that are in the same lobby as the client.
   */
  public synchronized void printPlayersInLobby() {
    sendToClient.send(clienthandler, CommandsToClient.PRINT, GameList.printUserInLobby(clienthandler.user.getLobby()));
  }
}
