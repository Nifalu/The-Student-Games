package utility.IO;

import Server.ClientHandler;
import Server.ServerManager;

import java.io.IOException;

/**
 * Offers multiple ways to send a message to a specific Client while also checking if the commands are valid
 */
public class SendToClient {

  /**
   * Send a message to all Clients on the Server
   *
   * @param cmd Enum
   * @param msg String
   */
  public void serverBroadcast(CommandsToClient cmd, String msg) {
    for (ClientHandler clientHandler : ServerManager.getActiveClientList()) {
      send(clientHandler, cmd, msg);
    }
  }


  // Send a message to a group of people ? Lobby?
  /*
  public void lobbyBroadcast( >>some list of recipients<< ,CommandsToClient cmd, String msg) {
    for (ClientHandler clientHandler : >>list of recipients<< ) {
      send(clientHandler, cmd, msg);
    }
  }
   */

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

    }
  }


  /**
   * Sends the Message
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
      System.out.println("cannot reach user");
    }
  }
}
