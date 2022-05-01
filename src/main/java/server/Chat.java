package server;

import utility.io.CommandsToClient;
import utility.io.SendToClient;


/**
 * Chat contains all methods needed for chatting.
 * Note that broadcast functionality is implemented in the SendToClient class.
 * This class only needs to do the formatting and/or defining which user or group is the recipient.
 */
public class Chat {

  /**
   * SendToClient object used to communicate with the client
   */
  SendToClient sendToClient = new SendToClient();

  /**
   * sends a message to one specific client
   * used for chatting between two clients (instead of chatting with everyone)
   *
   * @param sender ClientHandler
   * @param input  String
   */
  public synchronized void whisper(ClientHandler sender, String input) {
    String[] splitUserAndMsg = input.split("-", 2);
    String recipient = splitUserAndMsg[0];
    String msg = splitUserAndMsg[1];
    boolean found = false;

    for (ClientHandler clientHandler : ServerManager.getActiveClientList()) {
      if (clientHandler.user.getUsername().equalsIgnoreCase(recipient)) {
        // Send message to recipient:
        sendToClient.send(clientHandler, CommandsToClient.CHAT, sender.user.getUsername() + " to " + recipient + ": " + msg);
        found = true;
      }
    }
    // Recipient does not exist:
    if (found) {
      // Send message also to myself:
      sendToClient.send(sender, CommandsToClient.CHAT, sender.user.getUsername() + " to " + recipient + ": " + msg);
    } else {
      sendToClient.send(sender, CommandsToClient.CHAT, recipient + " is not here...");
    }
  }


  /**
   * prepares a chat message for broadcast to all user
   *
   * @param client ClientHandler
   * @param msg    message
   */
  public synchronized void broadcast(ClientHandler client, String msg) {
    sendToClient.serverBroadcast(CommandsToClient.CHAT, "[GLOBAL] " + client.user.getUsername() + ": " + msg);
  }

  /**
   * prepares a chat message for broadcast to all user in the same lobby
   *
   * @param client ClientHandler
   * @param msg    message
   */
  public synchronized void lobbyBroadcast(ClientHandler client, String msg) {
    sendToClient.lobbyBroadcast(client.user.getLobby().getUsersInLobby(), CommandsToClient.CHAT,
        "[" + client.user.getLobby().getLobbyName() + "] " + client.user.getUsername() + ": " + msg);
  }
}
