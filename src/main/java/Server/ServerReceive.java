package Server;

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
   *
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
        System.out.println("1");
        client.nameClass.receiveFromClient.setMessage(msg);
        break;
    }
  }
}