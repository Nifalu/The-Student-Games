package utility.io;

import server.ClientHandler;
import server.ServerManager;
import server.User;

import java.io.IOException;
import java.util.HashMap;

/**
 * Offers multiple ways to send a message to a specific client while also checking if the commands are valid
 */
public class SendToClient {

  /**
   * Send a message to all Clients on the server
   *
   * @param cmd Enum
   * @param msg String
   */
  public void serverBroadcast(CommandsToClient cmd, String msg) {
    for (ClientHandler clientHandler : ServerManager.getActiveClientList()) {
      send(clientHandler, cmd, msg);
    }
  }


  /**
   * Sends a command and a message to all clients in a specified hashmap
   * @param map hashmap
   * @param cmd commandToClient
   * @param msg String message
   */
  public void lobbyBroadcast(HashMap<Integer, User> map, CommandsToClient cmd, String msg) {
    for(User user: map.values()) {
      send(user.getClienthandler(),cmd,msg);
    }
  }

  /**
   * Validates the given Message and sends it to the recipient.
   *
   * @param recipient ClientHandler
   * @param cmd       Enum
   * @param msg       String
   */
  public void send(ClientHandler recipient, CommandsToClient cmd, String msg) {

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
    }
  }



  /**
   * Sends the Message to the client
   *
   * @param recipient ClientHandler
   * @param msg       String
   */
  private void sendTo(ClientHandler recipient, String msg) {
    try {
      recipient.getOut().write(msg);
      recipient.getOut().newLine();
      recipient.getOut().flush();
    } catch (IOException e) {
      //System.out.println("cannot reach user" + msg);
    }
  }
}
