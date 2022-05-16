package utility.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ClientHandler;
import server.ServerManager;
import server.User;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Offers multiple ways to send a message to a specific client while also checking if the commands are valid
 */
public class SendToClient {

  Logger logger = LogManager.getLogger(SendToClient.class);

  /**
   * Send a message to all Clients on the server
   *
   * @param cmd Enum
   * @param msg String
   */
  public synchronized void serverBroadcast(CommandsToClient cmd, String msg) {

    for (Iterator<ClientHandler> it = ServerManager.getActiveClientList().iterator(); it.hasNext();) {
      try {
        send(it.next(), cmd, msg);
      } catch (ConcurrentModificationException e) {
        logger.warn("ActiveClientList changed size while trying to do a serverbroadcast");
      }
    }
  }


  /**
   * Sends a command and a message to all clients in a specified hashmap
   *
   * @param map hashmap
   * @param cmd commandToClient
   * @param msg String message
   */
  public synchronized void lobbyBroadcast(HashMap<Integer, User> map, CommandsToClient cmd, String msg) {
    for (Iterator<User> it = map.values().iterator(); it.hasNext();) {
      try {
        send(it.next().getClienthandler(), cmd, msg);
      } catch (ConcurrentModificationException e) {
        logger.warn("User-list for LobbyBroadcast changed size while trying to do a lobbybroadcast");
      }
    }
  }

  /**
   * Validates the given Message and sends it to the recipient.
   *
   * @param recipient ClientHandler
   * @param cmd       Enum
   * @param msg       String
   */
  public synchronized void send(ClientHandler recipient, CommandsToClient cmd, String msg) {

    switch (cmd) {

      case PING: // sends a Ping
        sendTo(recipient, "PING--" + msg);
        break;

      case INITIALPING: // sends the initial Ping
        sendTo(recipient, "INITIALPING--" + msg);
        break;

      case PRINT: // sends a message to be printed out
        sendTo(recipient, "PRINT--" + msg);
        break;

      case LOBBY:
        sendTo(recipient, "PRINT--" + msg);
        break;

      case CHAT:
        sendTo(recipient, "CHAT--" + msg);
        break;

      case PRINTGUISTART:
        sendTo(recipient, "PRINTGUISTART--" + msg);
        break;

      case PRINTGUIGAMETRACKER:
        sendTo(recipient, "PRINTGUIGAMETRACKER--" + msg);
        break;

      case GUIMOVECHARACTER:
        sendTo(recipient, "GUIMOVECHARACTER--" + msg);
        break;

      case PRINTLOBBIESGUI:
        sendTo(recipient, "PRINTLOBBIESGUI--" + msg);
        break;

      case PRINTWINNERSGUI:
        sendTo(recipient, "PRINTWINNERSGUI--" + msg);
        break;

      case PRINTFRIENDSGUI:
        sendTo(recipient, "PRINTFRIENDSGUI--" + msg);
        break;

      case DICEDICELEFT:
        sendTo(recipient, "DICEDICELEFT--" + msg);
        break;

      case DISABLECHARGUI:
        sendTo(recipient, "DISABLECHARGUI--" + msg);
        break;

      case ENABLECHARGUI:
        sendTo(recipient, "ENABLECHARGUI--" + msg);
        break;

      case SETCHARTOKEN:
        sendTo(recipient, "SETCHARTOKEN--" + msg);
        break;

      case MUSIC:
        sendTo(recipient, "MUSIC--" + msg);
        break;

      case MARKPLAYER:
        sendTo(recipient, "MARKPLAYER--" + msg);
        break;

      case NAME:
        sendTo(recipient, "NAME--" + msg);
        break;

      case YOURTURN:
        sendTo(recipient,"YOURTURN--" + msg);
        break;

      case YOURQUIZ:
        sendTo(recipient,"YOURQUIZ--" + msg);
        break;
    }
  }


  /**
   * Sends the Message to the client
   *
   * @param recipient ClientHandler
   * @param msg       String
   */
  private synchronized void sendTo(ClientHandler recipient, String msg) {
    try {
      if (recipient.user.isOnline()) {
        recipient.getOut().write(msg);
        recipient.getOut().newLine();
        recipient.getOut().flush();
      }
    } catch (IOException e) {
      System.out.println("cannot reach user" + msg);
    }
  }
}
