package utility.io;

import server.ClientHandler;

/**
 * contains all commands for the client
 */
public enum CommandsToClient {
  /**
   * used to communicate with the Lobby
   */
  LOBBY,

  /**
   * Prints out the message on the recipients Console.
   */
  PRINT,

  /**
   * Sends an initial Ping that starts the Ping-Pong between the Connection Monitors on server and Clientside.
   * For more information check "ConnectionToClientMonitor"
   */
  INITIALPING,

  /**
   * Used by the ConnectionMonitors to send Pings and Pongs with timestamps to figure out when someone has timed out.
   */
  PING,

  /**
   * Sends a message to be displayed in the chat.
   */
  CHAT,

  /**
   * Sends a message to be displayed in the lobby chat.
   */
  LOBBYCHAT,

  /**
   * Sets the msg field in the StartController
   * StartController will decide, when and how to display it
   */
  PRINTGUISTART,
  PRINTLOBBIESGUI,

  /**
   * used to print out messages to the game tracker in the
   * Game GUI
   */
  PRINTGUIGAMETRACKER,
  PRINTWINNERSGUI,
  PRINTFRIENDSGUI,

  /**
   * moves the player-figures on the GUI
   */
  GUIMOVECHARACTER,


}
