package Server;

import GameLogic.GameList;
import utility.IO.CommandsToServer;
import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

public class ServerReceive {

  private final SendToClient sendToClient = new SendToClient();
  private final ClientHandler client;


  public ServerReceive(ClientHandler client) {
    this.client = client;
  }

  /**
   * Processes the incoming message and sends it to the declared class
   * @param line String to process
   */
  public synchronized void process(String line) {
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
      sendToClient.send(client, CommandsToClient.PRINT, "Unknown command: " + line);
      return;
    }

    switch (cmd) {

      case ECHO:
        sendToClient.send(client, CommandsToClient.PRINT, msg);
        break;

      case PRINT:
        System.out.println(msg);
        break;

      case QUIT: // Disconnects the Client, no further arguments needed.
        client.disconnectClient(); // disconnects Client
        break;

      case PING: // answers a Ping with a Pong
        client.getConnectionToClientMonitor().ping(msg);
        break;

      case CHAT: // broadcasts a message to everyone
        client.chat.broadcast(client, msg);
        break;

      case WHISPER: // sends a message to a specific client
        client.chat.whisper(client, msg);
        break;

      case CHANGENAME: // changes the users nickname
        client.nameClass.changeNameTo(client.user.getUsername(), msg);
        break;

      case NAME: // sends a message to the Name class
        client.nameClass.receiveFromClient.setMessage(msg);
        break;

      case CREATELOBBY:
        client.lobbyhelper.connectLobby(msg);
        break;

      case LOBBY:
        client.lobbyhelper.receiveFromClient.setMessage(msg);
        break;

      case PRINTUSERLIST:
        client.lobbyhelper.printUserListAndSendToClient();
        break;

      case PRINTLOUNGINGLIST:
        client.lobbyhelper.printLoungingListAndSendToClient();
        break;

      case PRINTOPENLOBBIES:
        client.lobbyhelper.printOpenLobbiesAndSendToClient();
        break;

      case PRINTFINISHEDLOBBIES:
        client.lobbyhelper.printFinishedLobbiesAndSendToClient();
        break;

      case PRINTONGOINGLOBBIES:
        client.lobbyhelper.printOnGoingLobbiesAndSendToClient();
        break;

      case PRINTLOBBIES:
        client.lobbyhelper.printLobbiesAndSendToClient();
        break;


      case CHANGELOBBY:
        if (client.user.getIsReady()) {
          if (client.user.getLobby().getLobbyStatus() != 0) {
            client.user.getLobby().removeFromWaitingList(client.user.getClienthandler());
            client.user.setReadyToPlay(false);
            client.lobbyhelper.changeLobby(msg);
          } else {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You have to finish this game.");
          }
        } else if (!client.user.getIsPlaying()) {
          client.user.setReadyToPlay(false);
          client.lobbyhelper.changeLobby(msg);
        }
        break;

      case READY:
        if (!client.user.getIsReady()) {
          if (client.user.getLobby().getLobbyStatus() == 1) {
            client.lobbyhelper.readyToPlay(client.user.getClienthandler());
          } else if (client.user.getLobby().getLobbyStatus() == 0) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You missed the start of the Game.");
          } else if (client.user.getLobby().getLobbyStatus() == -1) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "Game is already over.");
          } else if (client.user.getLobby().getLobbyStatus() == 69) {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You have to choose another lobby.");
          }
        }
        break;

      case UNREADY:
        if (client.user.getIsReady() && client.user.getLobby().getLobbyStatus() == 1) {
          client.user.getLobby().removeFromWaitingList(client.user.getClienthandler());
          client.user.setReadyToPlay(false);
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You are not waiting anymore.");
        }
        break;

      case START:
        if (client.user.getIsReady()) {
          if (client.user.getLobby().getIsReadyToStartGame()) {
            client.user.getLobby().receiveFromProtocol.setMessage("start");
          } else {
            sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "There are not enough people to start the game.");
          }
        } else {
          sendToClient.send(client.user.getClienthandler(), CommandsToClient.PRINT, "You need to be ready to start the game.");
        }
        break;

      case ROLLDICE:
        client.user.getLobby().receiveFromProtocol.setMessage("dice§" + client.user.getUsername());
        break;

      case DICEDICE:
        client.user.getLobby().receiveFromProtocol.setMessage("dicedice§" + client.user.getUsername());
        break;

      case WWCD:
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

      case QUIZ:
        client.user.getLobby().receiveFromProtocol.setMessage("quiz§" + client.user.getUsername() + "§" + msg);
        break;

    }
  }
}
