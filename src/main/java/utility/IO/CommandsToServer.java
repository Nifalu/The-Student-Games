package utility.IO;

public enum CommandsToServer {
  /**
   * Tells the Server to return the message.
   */
  ECHO,

  /**
   * Prints out the message on the Server Console
   */
  PRINT,

  /**
   * Sends Pings and Pongs with Timestamps so the Connection Monitors can figure out when someone has timed out.
   */
  PING,

  /**
   * Tell the Server that you quit the game entirely (disconnect / close program)
   */
  QUIT,

  /**
   * Communicate with the Name class.
   */
  NAME,

  /**
   * Changes the nickname
   */
  NICK,

  /**
   * Sends a message to the Server to be put in the global chat.
   */
  CHAT,

  /**
   * Sends a message to the Server to be put in the lobby chat.
   */
  LOBBYCHAT,

  /**
   * Sends a message to the Server to be sent to a single user.
   */
  WHISPER,

  /**
   * Communicate with the Lobby class
   */
  LOBBY,
  CREATELOBBY,
  READY,
  UNREADY,
  START,
  ROLLDICE,
  DICEDICE,
  QUIZ,
  WWCD,
  PRINTUSERLIST,
  PRINTLOUNGINGLIST,
  PRINTOPENLOBBIES,
  PRINTFINISHEDLOBBIES,
  PRINTONGOINGLOBBIES,
  CHANGELOBBY,
  PRINTLOBBIES,
  PRINTHIGHSCORE,
}
