package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.IO.CommandsToServer;
import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

public class ServerReceive implements Runnable {

  private static final Logger logger = LogManager.getLogger(ServerReceive.class);
  private static final Logger logTraffic = LogManager.getLogger("Traffic");
  private static final Logger logTrafficNoPing = LogManager.getLogger("TrafficNoPing");

  private final SendToClient sendToClient = new SendToClient();
  private final ClientHandler client;
  private final String line;


  public ServerReceive(ClientHandler client, String line) {
    this.client = client;
    this.line = line;
    logTraffic.trace("Received message: " + line);
  }

  /**
   * Processes the incoming message and sends it to the declared class
   */
  @Override
  public void run() {
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

    if (cmd != CommandsToServer.PING) {
      logTrafficNoPing.trace("Received message: " + line);
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

      case NICK: // changes the users nickname
        client.nameClass.changeNameTo(client.user.getUsername(), msg);
        break;

      case NAME: // sends a message to the Name class
        client.nameClass.receiveFromClient.setMessage(msg);
        break;

      case LOBBY:
        client.lobbyhelper.receiveFromClient.setMessage(msg);
        break;
    }
  }
}
