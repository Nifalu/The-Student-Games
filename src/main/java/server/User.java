package server;

import gameLogic.GameList;
import gameLogic.Lobby;

import java.util.Random;


/**
 * The User Object holds various information that is connected to this individual User.
 * Since this User Object is known by the ServerManager, it can also store serverManager related information
 * like avatars, positions, scores etc...
 */
public class User {

  /**
   * tells if the user is still online
   */
  private boolean isOnline;

  /**
   * the users clienthandler
   */
  private ClientHandler clienthandler;

  /**
   * the users uuid
   */
  private final String uuid;

  /**
   * the users username
   */
  private String username;

  /**
   * notes whether the user has connected before
   */
  private boolean firstTime = true;

  /**
   * the users district
   */
  private final int district;

  /**
   * notes whether the user is ready to play or not
   */
  private boolean isReady;

  /**
   * let the game know if the user tried to cheat into the finish
   */
  private boolean punished;

  /**
   * the users current lobby
   */
  private Lobby lobby;

  /**
   * the field on the board
   */
  private int playingField;

  /**
   * notes whether the dice has been rolled or not
   */
  private boolean rolledDice;

  /**
   * notes how many dicedice are left
   */
  private int specialDiceLeft = 3;

  /**
   * notes whether the user is playing
   */
  private boolean isPlaying;

  /**
   * notes whether the user is actively rolling the dice or not
   */
  private boolean isNotActivelyRollingTheDice;

  /**
   * the player Nr. in the GUI
   * decides which circle represents this client
   */
  public int gameTokenNr = 0;

  /**
   * the character the player chose
   */
  public int characterNr = 0;

  /**
   * notes whether it's game over for the user or not
   */
  public boolean gameOver;

  /**
   * creates a new user object
   *
   * @param clientHandler ClientHandler
   * @param username      String
   */
  public User(ClientHandler clientHandler, String username, String uuid, boolean isOnline) {
    this.clienthandler = clientHandler;
    this.username = username;
    this.district = assignDistrict();
    this.uuid = uuid;
    this.lobby = GameList.getLobbyList().get(0);
    this.isOnline = isOnline;
  }


  // ----- GETTERS -----


  /**
   * returns the information if a user is still online
   *
   * @return boolean: online
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * returns the clients username
   *
   * @return String: username
   */
  public synchronized String getUsername() {
    return username;
  }

  /**
   * returns the clients district
   *
   * @return int: district
   */
  public synchronized int getDistrict() {
    return district;
  }

  /**
   * returns whether the client has been online before or not
   *
   * @return boolean
   */
  public synchronized boolean isFirstTime() {
    return firstTime;
  }

  /**
   * returns the clients clienthandler
   *
   * @return ClientHandler
   */
  public synchronized ClientHandler getClienthandler() {
    return clienthandler;
  }

  /**
   * returns the clients lobby
   *
   * @return Lobby
   */
  public synchronized gameLogic.Lobby getLobby() {
    return lobby;
  }

  /**
   * returns whether the client is ready to play or not
   *
   * @return boolean
   */
  public synchronized boolean getIsReady() {
    return isReady;
  }

  /**
   * returns the clients current position (which field) on the board
   *
   * @return int: field number
   */
  public synchronized int getPlayingField() {
    return playingField;
  }

  /**
   * returns whether the client has rolled a dice or not
   *
   * @return boolean
   */
  public synchronized boolean getRolledDice() {
    return rolledDice;
  }

  /**
   * returns how many dicedice (dice with max. 4) the player has left
   *
   * @return int
   */
  public int getSpecialDiceLeft() {
    return specialDiceLeft;
  }

  /**
   * returns whether the client is playing a game or not
   *
   * @return boolean
   */
  public boolean getIsPlaying() {
    return isPlaying;
  }

  /**
   * returns whether the client is acrively rolling the dice or not
   *
   * @return boolean
   */
  public boolean getIsNotActivelyRollingTheDice() {
    return isNotActivelyRollingTheDice;
  }


  /**
   * returns whether the game is over or not
   *
   * @return boolean
   */
  public boolean getGameOver() {
    return gameOver;
  }

  /**
   * returns the users Uuid
   * @return String
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * returns whether the user is punishedd or not
   *
   * @return boolean
   */
  public boolean isPunished() {
    return punished;
  }

  // ----- SETTERS -----

  /**
   * changes the clients username
   *
   * @param username String
   */
  public synchronized void setUsername(String username) {
    this.username = username;
  }

  /**
   * reduces the amount of special dice the player has left
   */
  public void usedSpecialDice() {
    specialDiceLeft--;
  }

  /**
   * resets the amount of special dice the player has left
   */
  public void resetSpecialDice() {
    specialDiceLeft = 3;
  }

  /**
   * sets whether the client has connected for the first time or not
   *
   * @param firstTime boolean
   */
  public synchronized void setFirstTime(boolean firstTime) {
    this.firstTime = firstTime;
  }

  /**
   * sets the clients current lobby
   *
   * @param newLobby Lobby
   */
  public synchronized void setLobby(gameLogic.Lobby newLobby) {
    lobby = newLobby;
  }

  /**
   * marks the client as ready to play
   *
   * @param isReady boolean
   */
  public void setReadyToPlay(boolean isReady) {
    this.isReady = isReady;
  }

  /**
   * sets the current playing field
   *
   * @param newPlayingField int
   */
  public synchronized void setPlayingField(int newPlayingField) {
    playingField = newPlayingField;
  }

  /**
   * sets the number the dice has rolled
   *
   * @param rolledDice boolean
   */
  public void setRolledDice(boolean rolledDice) {
    this.rolledDice = rolledDice;
  }

  /**
   * sets whether the client is currently playing or not
   *
   * @param playing boolean
   */
  public void setIsPlaying(boolean playing) {
    isPlaying = playing;
  }

  /**
   * sets that the client is not actively rolling the dice
   */
  public void setNotActivelyRollingTheDice() {
    isNotActivelyRollingTheDice = true;
  }

  /**
   * sets that the client is actively rolling the dice
   */
  public void setIsActivelyRollingTheDice() {
    isNotActivelyRollingTheDice = false;
  }

  /**
   * sets the game status to over
   */
  public void setGameOver() {
    gameOver = true;
  }

  /**
   * sets the game status of player to reconnected
   */
  public void setNotGameOver() {
    gameOver = false;
  }

  /**
   * sets the users character choice
   */
  public void setCharacter(int nr) { characterNr = nr; }

  /**
   * sets the users ClientHandler
   * @param client ClientHandler
   */
  public void setClienthandler(ClientHandler client) {
    this.clienthandler = client;
  }

  /**
   * sets online status of the user
   *
   * @param online boolean
   */
  public void setOnline(boolean online) {
    isOnline = online;
  }

  /**
   * sets whether user tried to cheat into the finish
   *
   * @param punished boolean
   */
  public void setPunished(boolean punished) {
    this.punished = punished;
  }

  //-------------OTHER METHODS------------------------------

  /**
   * assigns a district to the client
   *
   * @return int between 1 and 12
   */
  public synchronized static int assignDistrict() {
    Random random = new Random();
    return random.nextInt(12) + 1;
  }
}
