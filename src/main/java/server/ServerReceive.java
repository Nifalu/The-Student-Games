package server;

import gameLogic.GameList;
import gameLogic.Lobby;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToClient;
import utility.io.CommandsToServer;
import utility.io.SendToClient;

import java.util.HashMap;

/**
 * receives commands on the server side
 */
public class ServerReceive implements Runnable {

  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(ServerReceive.class);

  /**
   * traffic logger
   */
  private static final Logger logTraffic = LogManager.getLogger("Traffic");

  /**
   * no ping logger
   */
  private static final Logger logTrafficNoPing = LogManager.getLogger("TrafficNoPing");

  /**
   * SendToClient object used to communicate with the client
   */
  private final SendToClient sendToClient = new SendToClient();

  /**
   * the clients clienthandler
   */
  private final ClientHandler client;

  /**
   * saves a line of a message
   */
  private final String line;


  /**
   * Processes a new messsage received by the client
   *
   * @param client clientHandler that sent the message
   * @param line   message
   */
  public ServerReceive(ClientHandler client, String line) {
    this.client = client;
    this.line = line;
  }

  /**
   * Processes the incoming message and sends it to the declared class
   */
  @Override
  public void run() {
    process();
  }

  /**
   * splits incoming messages and then processes them
   */
  private synchronized void process() {
    // Incoming message is split into command (cmd) and message (msg)
    String[] input;
    String msg;
    CommandsToServer cmd;

    input = line.split("--", 2); // Splits the String (limit - 1) times at the first "-"
    if (input.length > 1) {
      msg = input[1];
    } else {
      msg = line;
    }

    try {
      cmd = CommandsToServer.valueOf(input[0].toUpperCase());
    } catch (IllegalArgumentException e) {
      // " " is sent received when the client has quit.
      if (!input[0].equals("")) {
        logger.warn("Received unknown command. ( " + input[0] + " )");
      }
      return;
    }

    if (cmd == CommandsToServer.PING) {
      logTraffic.trace(line);
    } else {
      logTrafficNoPing.trace(line);
    }

    switch (cmd) {

      case ECHO: //returns the message
        sendToClient.send(client, CommandsToClient.PRINT, msg);
        break;

      case PRINT: // prints the message to console
        System.out.println(msg);
        break;

      case QUIT: // Disconnects the client, no further arguments needed.
        client.disconnectClient(); // disconnects client
        break;

      case PING: // answers a Ping with a Pong
        client.getConnectionToClientMonitor().ping(msg);
        break;

      case CHAT: // broadcasts a message to everyone
        client.chat.broadcast(client, msg);
        break;

      case LOBBYCHAT: // broadcasts a message to everyone
        client.chat.lobbyBroadcast(client, msg);
        break;

      case WHISPER: // sends a message to a specific client
        client.chat.whisper(client, msg);
        break;

      case NICK: // changes the users nickname
        client.nameClass.changeNameTo(client.user.getUsername(), msg);
        break;

      case NAME: // sends a message to the Name class
        client.nameClass.receiveFromClient.setMessage(msg);
        break;

      case CREATELOBBY: // creates a lobby
        client.lobbyhelper.connectLobby(msg);
        break;

      case LOBBY:  // speaks to the lobby
        client.lobbyhelper.receiveFromClient.setMessage(msg);
        break;

      case PRINTUSERLIST: // prints the userlist
        client.lobbyhelper.printUserListAndSendToClient();
        break;

      case PRINTLOUNGINGLIST: // prints the lounginglist
        client.lobbyhelper.printLoungingListAndSendToClient();
        break;

      case PRINTOPENLOBBIES: // prints the open lobbies list
        client.lobbyhelper.printOpenLobbiesAndSendToClient();
        break;

      case PRINTFINISHEDLOBBIES: // prints the finished lobbies list
        client.lobbyhelper.printFinishedLobbiesAndSendToClient();
        break;

      case PRINTONGOINGLOBBIES: // prints teh ongoing lobbies list
        client.lobbyhelper.printOnGoingLobbiesAndSendToClient();
        break;

      case PRINTLOBBIES: // prints all lobies list
        client.lobbyhelper.printLobbiesAndSendToClient();
        break;

      case PRINTHIGHSCORE: // prints the high score
        client.user.getLobby().getHighScore(client.user.getClienthandler());
        break;

      case CHANGELOBBY: // changes the lobby
        try {
          int number = Integer.parseInt(msg.replaceAll("\\s", ""));
          if (number < GameList.getLobbyList().size() && number >= 0) {
            if (GameList.getLobbyList().get(number).getLobbyStatus() == 1 ||
                    GameList.getLobbyList().get(number).getLobbyStatus() == 69) {
              if (client.user.getIsReady()) {
                if (!client.user.getIsPlaying()) {
                  client.user.getLobby().removeFromWaitingList(client.user.getClienthandler());
                  client.user.setReadyToPlay(false);
                  client.lobbyhelper.changeLobby(msg);
                  if (GameList.getLobbyList().get(number).getLobbyStatus() == 1) {
                    client.user.getLobby().checkIfCharsTaken();
                  }
                } else {
                  sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You have to finish this game.");
                }
              } else {
                client.user.setReadyToPlay(false);
                client.lobbyhelper.changeLobby(msg);
              }
            } else  if (GameList.getLobbyList().get(number).getLobbyStatus() == 0){
              if (!client.user.getIsPlaying()) {
                if (client.user.getIsReady()) {
                  client.user.getLobby().removeFromWaitingList(client.user.getClienthandler());
                  client.user.setReadyToPlay(false);
                }
                sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You are now a spectator.");
                client.lobbyhelper.changeLobby(msg);
              } else {
                sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You have to finish this game.");
              }
            }
          } else {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "This lobby doesn't exist.");
          }
        } catch (Exception e) {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, msg + " is not a valid number!");
        }
        break;

      case READY: // sets user status to ready
        if (!client.user.getIsReady()) {
          if (client.user.getLobby().getLobbyStatus() == 1) {
            client.user.getLobby().readyToPlay(client.user.getClienthandler());
          } else if (client.user.getLobby().getLobbyStatus() == 0) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You missed the start of the Game.");
          } else if (client.user.getLobby().getLobbyStatus() == -1) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "Game is already over.");
          } else if (client.user.getLobby().getLobbyStatus() == 69) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You have to choose another lobby.");
          }
        } else {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You are already ready.");
        }
        break;

      case UNREADY: // sets user status to not ready
        if (client.user.getIsReady() && client.user.getLobby().getLobbyStatus() == 1) {
          client.user.getLobby().removeFromWaitingList(client.user.getClienthandler());
          //client.user.setReadyToPlay(false);
        } else if (!client.user.getIsReady()) {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You haven't been ready.");
        }
        break;

      case START: // starts the game
        if (client.user.getLobby().getLobbyStatus() == 1) {
          if (client.user.getIsReady()) {
            if (client.user.getLobby().getIsReadyToStartGame()) {
              client.user.getLobby().receiveFromProtocol.setMessage("start");
            } else {
              sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "There are not enough people to start the game.");
            }
          } else {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You need to be ready to start the game.");
          }
        } else if (client.user.getLobby().getLobbyStatus() == -1) {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "Game has already started.");
        }
        break;

      case ROLLDICE: // rolls the dice
        client.user.getLobby().receiveFromProtocol.setMessage("dice§" + client.user.getUsername());
        break;

      case PRINTPLAYERSINLOBBY: // prints players in lobby
        client.lobbyhelper.printPlayersInLobby();
        break;

      case DICEDICE: // rolls the special dice
        client.user.getLobby().receiveFromProtocol.setMessage("dicedice§" + client.user.getUsername());
        break;

      case WWCD: // moves player to specified position
        try {
          if (!msg.equals("-1")) {
            msg = String.valueOf(Integer.parseInt(msg));
            client.user.getLobby().receiveFromProtocol.setMessage("wwcd§" + client.user.getUsername() + "§" + msg);
            break;
          }
        } catch (Exception e) {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT,
              msg + " is not a number");
        }

      case QUIZ: // speaks to the quiz
        client.user.getLobby().receiveFromProtocol.setMessage("quiz§" + client.user.getUsername() + "§" + msg);
        break;

      case DICEDICELEFT:
        sendToClient.send(client.user.getClienthandler(), CommandsToClient.DICEDICELEFT, Integer.toString(client.user.getSpecialDiceLeft()));
        break;

      case CHANGECHARACTER:
        //System.out.println("OLD CHAR NR: " + client.user.characterNr);
        // checks if player already selected a character -> enables it again if necessary
        HashMap<Integer, User> playersInLobby = client.user.getLobby().getUsersInLobby();
        Lobby lobby = client.user.getLobby();

        // enables the character again, if the player had already chosen one
        if (client.user.characterNr != 0) {
          sendToClient.lobbyBroadcast(playersInLobby, CommandsToClient.ENABLECHARGUI, Integer.toString(client.user.characterNr));
          lobby.charactersTaken.put(client.user.characterNr, false);
        }

        // disables the character for the others
        sendToClient.lobbyBroadcast(playersInLobby, CommandsToClient.DISABLECHARGUI, msg);
        lobby.charactersTaken.put(Integer.parseInt(msg), true);
        client.user.characterNr = Integer.parseInt(msg);
        break;

      case DISABLECHARGUI:
        //sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.DISABLECHARGUI, msg);
        if (!msg.equals("0")) {
          sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.DISABLECHARGUI, msg);
          client.user.getLobby().charactersTaken.put(Integer.parseInt(msg), true);
        }
        break;

      case ENABLECHARGUI:
        //sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.ENABLECHARGUI, msg);
        if (!msg.equals("0")) {
          sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.ENABLECHARGUI, msg);
          client.user.getLobby().charactersTaken.put(Integer.parseInt(msg), false);
        }
        break;

      /*case CHECKIFCHARSTAKEN:
        /*Lobby l = client.user.getLobby();
        for (Integer key : l.getUsersInLobby().keySet()) {
          User u = l.getUsersInLobby().get(key);
          if (u.characterNr != 0) {
            System.out.println("de user " + u.getUsername() + " het de charakter " + u.characterNr);
            l.charactersTaken.put(key, true);
            System.out.println("die eint hashmap hett jetz wert " + l.charactersTaken.get(key) + " bem key " + key);
            sendToClient.lobbyBroadcast(l.getUsersInLobby(), CommandsToClient.DISABLECHARGUI, Integer.toString(key));
          }
        }*/

        /*Lobby l = client.user.getLobby();
        for (Integer key : l.getUsersInLobby().keySet()) {
          User u = l.getUsersInLobby().get(key);
          if (u.characterNr != 0) {
            l.charactersTaken.put(key, true);
            sendToClient.lobbyBroadcast(l.getUsersInLobby(), CommandsToClient.DISABLECHARGUI, Integer.toString(key));
          }
        }*/
      //break;

      case SETCHARTOKEN:
        sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.SETCHARTOKEN, msg + "--" + client.user.gameTokenNr);
        break;

      case SETALLCHARTOKENS:
        Lobby currentLobby = client.user.getLobby();
        for (Integer key : currentLobby.getUsersInLobby().keySet()) {
          User currentUser = currentLobby.getUsersInLobby().get(key);
          sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.SETCHARTOKEN, currentUser.characterNr + "--" + currentUser.gameTokenNr);
        }
        break;

      case CHECKALLCHARS:
        Lobby l = client.user.getLobby();
        System.out.println("MOMENTANI LOBBY: " + l.getLobbyName());

        // enables all chars
        for (int i = 1; i < 7; i++) {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.ENABLECHARGUI, Integer.toString(i));
        }

        // disables the taken ones
        for (Integer key: l.charactersTaken.keySet()) {
          Boolean taken = l.charactersTaken.get(key);
          if (taken) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.DISABLECHARGUI, Integer.toString(key));
          } else {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.ENABLECHARGUI, Integer.toString(key));
          }
        }

        /*
        for (Integer key : l.getUsersInLobby().keySet()) {
          User u = l.getUsersInLobby().get(key); // gets the value out of the key
          // System.out.println("aso de user esch: " + u.getUsername() + " ond er hett de character " + u.characterNr);
          if (u.characterNr != 0) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.DISABLECHARGUI, Integer.toString(u.characterNr));
          }
        }*/
        break;

      case ENABLECURRENTCHARGUI:
        sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.ENABLECHARGUI, Integer.toString(client.user.characterNr));
        break;
    }
  }
}
