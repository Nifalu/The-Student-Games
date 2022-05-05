package utility.io;

import client.ClientManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Sends a message to the server while also checking if the Command is valid.
 */
public class SendToServer {

  /**
   * client traffic logger
   */
  private static final Logger clientTraffic = LogManager.getLogger("ClientTraffic");

  /**
   * no ping logger
   */
  private static final Logger clientTrafficNoPing = LogManager.getLogger("ClientTrafficNoPing");


  /**
   * Validates the Command and sends the message
   *
   * @param cmd Enum
   * @param msg String
   */
  public synchronized void send(CommandsToServer cmd, String msg) {

    if (cmd == CommandsToServer.PING) {
      clientTraffic.trace(cmd + msg);
    } else {
      clientTraffic.trace(cmd + msg);
      clientTrafficNoPing.trace(cmd + msg);
    }

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

      case LOBBYCHAT: // Sends a chat to lobby
        sendTo("LOBBYCHAT--" + msg);
        break;

      case NICK: // Changes the Nickname
        sendTo("NICK--" + msg);
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

      case PRINTUSERLIST:
        sendTo("PRINTUSERLIST--");
        break;

      case PRINTLOBBIES:
        sendTo("PRINTLOBBIES--");
        break;

      case PRINTLOUNGINGLIST:
        sendTo("PRINTLOUNGINGLIST--");
        break;

      case PRINTOPENLOBBIES:
        sendTo("PRINTOPENLOBBIES--");
        break;

      case PRINTFINISHEDLOBBIES:
        sendTo("PRINTFINISHEDLOBBIES--");
        break;

      case PRINTONGOINGLOBBIES:
        sendTo("PRINTONGOINGLOBBIES--");
        break;

      case PRINTPLAYERSINLOBBY:
        sendTo("PRINTPLAYERSINLOBBY");
        break;

      case PRINTHIGHSCORE:
        sendTo("PRINTHIGHSCORE--");
        break;

      case CHANGELOBBY:
        sendTo("CHANGELOBBY--" + msg);
        break;

      case READY:
        sendTo("READY--");
        break;

      case UNREADY:
        sendTo("UNREADY--");
        break;

      case START:
        sendTo("START--");
        break;

      case ROLLDICE:
        sendTo("ROLLDICE--");
        break;

      case DICEDICE:
        sendTo("DICEDICE--");
        break;

      case QUIZ:
        sendTo("QUIZ--" + msg);
        break;

      case WWCD:
        sendTo("WWCD--" + msg);
        break;

      case ECHO: // Sends a message to be echoed by the server
        sendTo("ECHO--" + msg);
        break;

      case DICEDICELEFT:
        sendTo("DICEDICELEFT--" + msg);
        break;

      case CHANGECHARACTER:
        sendTo("CHANGECHARACTER--" + msg);
        break;
    }
  }

  /**
   * Sends the message to the server
   *
   * @param msg String
   */
  private synchronized void sendTo(String msg) {
    try {
      ClientManager.getOut().write(msg);
      ClientManager.getOut().newLine();
      ClientManager.getOut().flush();
    } catch (IOException e) {
      System.out.println("cannot reach server");
    }
  }
}
