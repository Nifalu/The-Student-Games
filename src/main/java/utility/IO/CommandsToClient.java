package utility.IO;

public enum CommandsToClient {
  /**
   * used to communicate with the Lobby
   */
  LOBBY,

  /**
   * Prints out the message on the recipients Console
   */
  PRINT,

  /**
   * Sends an initial Ping that starts the Ping-Pong between the Connection Monitors on Server and Clientside.
   */
  INITIALPING,

  /**
   * Sends Pings and Pongs with Timestamps so the Connection Monitors can figure out when someone has timed out.
   */
  PING,
  CREATELOBBY,
  PRINTUSERLIST,
  PRINTLOUNGINGLIST,
  PRINTOPENLOBBIES,
  PRINTFINISHEDLOBBIES,
  PRINTONGOINGLOBBIES,
  CHANGELOBBY,
  PRINTLOBBIES,

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

}
