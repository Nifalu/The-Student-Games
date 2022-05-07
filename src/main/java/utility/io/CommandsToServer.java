package utility.io;

/**
 * contains all the commands for the server
 */
public enum CommandsToServer {
  /**
   * Tells the server to return the message.
   */
  ECHO,

  /**
   * Prints out the message on the server Console
   */
  PRINT,

  /**
   * Used by the Connection Monitors to send Pings and Pongs with Timestamps to figure out when someone has timed out.
   */
  PING,

  /**
   * Tell the server that you quit the game entirely (disconnect / close program)
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
   * Sends a message to the server to be put in the global chat.
   */
  CHAT,

  /**
   * Sends a message to the server to be put in the lobby chat.
   */
  LOBBYCHAT,

  /**
   * Sends a message to the server to be sent to a single user.
   */
  WHISPER,

  /**
   * Communicate with the Lobby class
   */
  LOBBY,

  /**
   * used with a name to create a new Lobby
   */
  CREATELOBBY,

  /**
   * used without any more information to state that a player is ready
   */
  READY,

  /**
   * used without any more information to state that a player is no longer ready
   */
  UNREADY,

  /**
   * used without any more information to start the game the player is in.
   */
  START,

  /**
   * used to roll the dice.
   */
  ROLLDICE,

  /**
   * used to roll the special dice
   */
  DICEDICE,

  /**
   * used to answer the quiz
   */
  QUIZ,

  /**
   * used to cheat a player on a specified position. (needs a position)
   */
  WWCD,

  /**
   * prints out a list of all users
   */
  PRINTUSERLIST,

  /**
   * prints out a list of all lobbies and all users in those lobbies.
   */
  PRINTLOUNGINGLIST,

  /**
   * prints out all open lobbies
   */
  PRINTOPENLOBBIES,

  /**
   * prints out all finished lobbies
   */
  PRINTFINISHEDLOBBIES,

  /**
   * prints out all ongoing lobbies
   */
  PRINTONGOINGLOBBIES,

  /**
   * moves a player to a new lobby
   */
  CHANGELOBBY,

  /**
   * prints out all lobbies
   */
  PRINTLOBBIES,

  /**
   * prints out the highscore
   */
  PRINTHIGHSCORE,

  /**
   * checks how many dicedice the player has left
   */
  DICEDICELEFT,

  /**
   * changes the players character
   */
  CHANGECHARACTER,

  /**
   * disables a character in the char selection screen
   */
  DISABLECHARACTERGUI,

  /**
   * prints out players in lobby
   */
  PRINTPLAYERSINLOBBY, CHECKIFCHARSTAKEN,
}
