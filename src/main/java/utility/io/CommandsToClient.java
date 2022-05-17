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

  /**
   * used to send/play music
   */
  MUSIC,

  /**
   * used to disable a character in the char selection screen
   */
  DISABLECHARGUI,

  /**
   * used to enable a character int he char selection screen
   */
  ENABLECHARGUI,

  /**
   * used to set a character token in the game screen
   */
  SETCHARTOKEN,

  /**
   * used to mark the player whose turn it is
   */
  MARKPLAYER,

  /**
   * used to display the users name in the GUI
   */
  NAME,

  /**
   * used to highlight on the gui when it's the clients turn
   */
  YOURTURN,

  /**
   * used to enable the quiz buttons in the game gui when the client has to answer a quiz question
   */
  YOURQUIZ


}
