package utility.IO;

import Client.ClientManager;

import java.io.IOException;

/**
 * Sends a message to the Server while also checking if the Command is valid.
 */
public class SendToServer {


  /**
   * Validates the Command and sends the message
   *
   * @param cmd Enum
   * @param msg String
   */
  public synchronized void send(CommandsToServer cmd, String msg) {

    switch (cmd) {

      case PING: // Sends a Ping
        sendTo("PING--" + msg);
        break;

      case QUIT: // Sends a quit message and disconnects
        sendTo("QUIT--");
        try {
          wait(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        ClientManager.disconnect();
        break;
      case CHAT: // Sends a chat to everyone
        sendTo("CHAT--" + msg);
        break;

      case NICK: // Changes the Nickname
        sendTo("CHANGENAME--" + msg);
        break;

      case WHISPER: // Sends a message to a single User
        sendTo("WHISPER--" + msg);
        break;

      case NAME: // Sends a message to the Name class
        sendTo("NAME--" + msg);
        break;

      case PRINT: // Sends a message to be printed out
        sendTo("PRINT--" + msg);
        break;

      case LOBBY:
        sendTo("LOBBY--" + msg);
        break;

      case CREATELOBBY:
        sendTo("CREATELOBBY--" + msg);
        break;

      case ECHO: // Sends a message to be echoed by the Server
        sendTo("ECHO--" + msg);
        break;
    }
  }

  /**
   * Sends the message to the server
   * @param msg String
   */
  private void sendTo(String msg) {
    try {
      ClientManager.getOut().write(msg);
      ClientManager.getOut().newLine();
      ClientManager.getOut().flush();
    } catch (IOException e) {
      System.out.println("cannot reach Server");
    }
  }

}
