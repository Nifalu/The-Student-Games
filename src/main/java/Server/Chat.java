package Server;

import utility.CommandsToClient;
import utility.SendToClient;


/**
 * Chat contains all methods needed for chatting.
 * Note that broadcast functionality is implemented in the SendToClient class.
 * This class only needs to do the formatting and/or defining which user or group is the recipient.
 */
public class Chat {
   SendToClient sendToClient = new SendToClient();

  /**
   * sends a message to one specific client
   * used for chatting between two clients (instead of chatting with everyone)
   * @param sender String
   * @param input String
   */
  public synchronized void whisper(ClientHandler sender, String input) {
    String[] splitUserAndMsg = input.split("-", 2);
    String recipient = splitUserAndMsg[0];
    String msg = splitUserAndMsg[1];

    for (ClientHandler clientHandler : ServerManager.getActiveClientList()) {
      if(clientHandler.user.getUsername().equalsIgnoreCase(recipient)) {
        // Send message to recipient:
        sendToClient.send(clientHandler, CommandsToClient.PRINT, sender.user.getUsername() + " to " + recipient + ": " + msg);
      } else {
        // Recipient does not exist:
        sendToClient.send(sender,CommandsToClient.PRINT, recipient + " is not here...");
      }
    }
    // Send message also to myself:
    sendToClient.send(sender, CommandsToClient.PRINT, sender.user.getUsername() + " to " + recipient + ": " + msg);
  }


  /**
   * prepares a chat message for broadcast
   * @param client ClientHandler
   * @param msg String
   */
  public synchronized void broadcast(ClientHandler client, String msg) {
    sendToClient.serverBroadcast(CommandsToClient.PRINT, client.user.getUsername() + ": " + msg);
  }

}
