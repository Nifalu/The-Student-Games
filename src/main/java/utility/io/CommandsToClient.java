package utility.io;

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

  /**
   * used to print out the lobbies in the GUI
   */
  PRINTLOBBIESGUI,

  /**
   * used to print out messages to the game tracker in the
   * Game GUI
   */
  PRINTGUIGAMETRACKER,

  /**
   * used to print out the winners of a game in the GUI
   */
  PRINTWINNERSGUI,

  /**
   * prints a String into the GUI (ListView) containing all Lobbie and users in the lobby.
   */
  PRINTFRIENDSGUI,

  /**
   * moves the player-figures on the GUI
   */
  GUIMOVECHARACTER,

  /**
   * tells the client how many dicedice he has left
   */
  DICEDICELEFT,

  MUSIC,

  DISABLECHARGUI,

  ENABLECHARGUI, SETCHARTOKEN, MARKPLAYER, NAME, YOURTURN, YOURQUIZ


}
