package gameLogic;

import server.ClientHandler;
import server.User;
import utility.io.*;

import java.util.HashMap;

/**
 * creates a Lobby Object
 */
public class Lobby {

  /**
   * amount of players needed to start a game
   */
  final int minToStart = 2;

  /**
   * max amount of players to start a game
   */
  final int maxToStart = 4;

  /**
   * the name of the lobby
   */
  String name;

  /**
   * the corresponding game to the lobby
   */
  Game game;

  /**
   * SendToClient object to communicate with the client
   */
  private final SendToClient sendToClient = new SendToClient();

  /**
   * SendToServer object to communicate with the server
   */
  private final SendToServer sendToServer = new SendToServer();

  /**
   * ReceiveFromProtocol object to communicate with the protocol
   */
  public ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

  /**
   * the highscore of the lobby
   */
  public static HighScore highScore = new HighScore();

  /**
   * the game status
   * Int status is -1 if the game is already finished, 0 if the game is ongoing, 1 if the game is open.
   */
  private int status;

  /**
   * when set to true, the receiverThread stops
   */
  private static boolean stop = false;

  /**
   * contains a HashMap of all the users in the Lobby
   */
  HashMap<Integer, User> usersReady = new HashMap<>();

  /**
   * HashMap which saves all the possible characters and if they're already taken or not
   */
  private HashMap<Integer, Boolean> charactersTaken = new HashMap<>();



  /**
   * creates a Lobby object with the given name
   *
   * @param name of the lobby
   * @param standardLobby boolean if the lobby is the standardlobby
   */
  public Lobby(String name, boolean standardLobby) {
    this.name = name;
    this.status = 1; // status of lobby is automatically set to open
    if (!standardLobby) {
      startThread();
    }

    gameTokensTaken.put(1, false);
    gameTokensTaken.put(2, false);
    gameTokensTaken.put(3, false);
    gameTokensTaken.put(4, false);

    charactersTaken.put(1, false);
    charactersTaken.put(2, false);
    charactersTaken.put(3, false);
    charactersTaken.put(4, false);
    charactersTaken.put(5, false);
    charactersTaken.put(6, false);
  }


  // ------------------------ GETTERS -----------------------------------

  /**
   * Creates a HashMap with all the users that are ready.
   *
   * @return the HashMap with alle the users that are ready.
   */
  public HashMap<Integer, User> getUsersReady() {
    return usersReady;
  }

  /**
   * HashMap which saves all the player tokens in the game and which ones are already taken
   */
  public HashMap<Integer, Boolean> gameTokensTaken = new HashMap<>();


  /**
   * creates a HashMap with all the users that in that lobby
   *
   * @return the HashMap with all the users in the lobby
   */
  public HashMap<Integer, User> getUsersInLobby() {
    HashMap<Integer, User> usersInLobby = new HashMap<>();
    int counter = 0;
    for (int i = 0; i < GameList.getUserlist().size(); i++) {
      try {
        if (GameList.getUserlist().get(i).getLobby().equals(this)) {
          usersInLobby.put(counter, GameList.getUserlist().get(i));
          counter++;
        }
      } catch (Exception e) {
        System.out.println("Could not get user in lobby");
        break;
      }
    }
    return usersInLobby;
  }


  /**
   * returns the name of the lobby
   *
   * @return the name of the lobby as String.
   */
  public String getLobbyName() {
    return name;
  }

  /**
   * returns the status of the lobba as an int
   * 0 for all the ongoing lobbies, 1 for all the open lobbies, -1 for all the finished lobbies
   * and 69 for the standard lobby.
   *
   * @return int: lobbystatus
   */
  public int getLobbyStatus() {
    return status;
  }

  /**
   * returns the lobby status as a String
   * @return String
   */
  public String getLobbyStatusAsString() {
    switch(status) {
      case -1: return "finished";
      case 0: return "ongoing";
      case 1: return "open";
      case 69: return "Zwischengeschoss";
    }
    return "error";
  }


  // ----------------------- SETTERS ----------------------------------------


  /**
   * sets lobby status to finished (int -1)
   */
  public void setLobbyStatusToFinished() {
    status = -1;
  }

  /**
   * sets lobby status to ongoing (int 0)
   */
  public void setLobbyStatusToOnGoing() {
    status = 0;
  }

  /**
   * sets lobby status to standard (int 69)
   */
  public void setLobbyStatusToStandard() {
    status = 69;
  }

  // --------------------------ANDERE METHODEN-------------------------------


  /**
   * removes a user from a lobby and puts the user into the standard lobby.
   * Users shouldn't be in no lobby.
   *
   * @param user that is removed
   */
  public void removeUserFromLobby(User user) {
    user.setLobby(GameList.getLobbyList().get(0)); // basic Lobby
  }

  /**
   * Checcks if a user is in a lobby. If it is true it will set the player to the waiting list
   * and sends a message to all users with its lobby
   *
   * @param clientHandler User that is ready to play
   */
  public void readyToPlay(ClientHandler clientHandler) {
    if (getLobbyStatus() == 1) {
      //System.out.println("size usersReady " + usersReady.size());
      //System.out.println("maxTo " + maxToStart);
      if (usersReady.size() < maxToStart) {
        waitingToPlay(clientHandler);
        sendToClient.send(clientHandler, CommandsToClient.PRINTGUIGAMETRACKER, "You are now waiting...");

        for (int i = 1; i < 5; i++) {
          if (!gameTokensTaken.get(i)) {
            clientHandler.user.gameTokenNr = i;
            gameTokensTaken.put(i, true);
            break;
          }
        }
        //lobbyBroadcastToPlayer(clientHandler.user.getUsername() + " is ready for a Game in Lobby: "
        //    + clientHandler.user.getLobby().getLobbyName());
        lobbyBroadcastToPlayer("People in the Lobby " + clientHandler.user.getLobby().getLobbyName() + ": " +
                clientHandler.user.getLobby().getUsersInLobby().size() + "; People ready: " + clientHandler.user.getLobby().getUsersReady().size());

        String character = Integer.toString(clientHandler.user.characterNr);
        String token = Integer.toString(clientHandler.user.gameTokenNr);
        sendToClient.lobbyBroadcast(clientHandler.user.getLobby().getUsersInLobby(), CommandsToClient.SETCHARTOKEN, character + "--" + token);
        //sendToServer.send(CommandsToServer.SETCHARTOKEN, Integer.toString(clientHandler.user.characterNr));

      } else {
        sendToClient.send(clientHandler, CommandsToClient.PRINT, "There are already " + maxToStart + " players ready.");
      }
    } else {
      sendToClient.send(clientHandler, CommandsToClient.PRINT, "please choose a lobby");
    }
  }

  /**
   * disables characters in the character selection GUI if they're already taken
   */
  public void checkIfCharsTaken() {
    for (int i = 1; i < 5; i++) {
      if (charactersTaken.containsKey(i)) {
        if (charactersTaken.get(i)) {
          sendToServer.send(CommandsToServer.DISABLECHARACTERGUI, Integer.toString(i));
        }
      }
    }
  }

  /**
   * Sets a player to the waiting list of the lobby
   *
   * @param clientHandler User who is ready to play the game.
   */
  public void waitingToPlay(ClientHandler clientHandler) {
    if (!usersReady.containsValue(clientHandler.user) && getLobbyStatus() == 1) {
      int size = usersReady.size();
      clientHandler.user.setReadyToPlay(true);
      usersReady.put(size, clientHandler.user);
    }
  }

  /**
   * Removes a user from the waiting list
   *
   * @param clientHandler User who is not ready to play the game anymore.
   */
  public void removeFromWaitingList(ClientHandler clientHandler) {
    usersReady.values().remove(clientHandler.user);
    clientHandler.user.setReadyToPlay(false);
    sendToClient.send(clientHandler, CommandsToClient.PRINT, "You are not waiting anymore.");

    for (int i = 1; i < 5; i++) {
      if (i == clientHandler.user.gameTokenNr) {
        gameTokensTaken.put(i, false);
      }
    }
    lobbyBroadcastToPlayer(clientHandler.user.getUsername() + " is not ready.");
    lobbyBroadcastToPlayer("People in the Lobby " + clientHandler.user.getLobby().getLobbyName() + ": " +
        clientHandler.user.getLobby().getUsersInLobby().size() + "; People ready: " + clientHandler.user.getLobby().getUsersReady().size());
  }

  /**
   * Receives all commands from the users of the lobby and sends it to its game.
   */
  public void startThread() {
    Thread LobbyWaitForMessageThread = new Thread(() -> {
      String msg;
      String[] answer;
      while (!stop && status != 96) {
        msg = receiveFromProtocol.receive(); // blocks until a message is received
        if (msg == null) {break;}
        answer = msg.split("??");
        if (msg.equals("start") && getLobbyStatus() == 1) { // starts the game
          if (usersReady.size() >= minToStart) {
            setLobbyStatusToOnGoing();
            game = new Game(this, usersReady, highScore);
            Thread gameThread = new Thread(game);
            gameThread.start();

            // GAME STARTS HERE
          }
        } else if (getLobbyStatus() == 0) {
          switch (answer[0]) {
            case "dice": // roll normal dice
              game.setRolledDice(answer[1], 6);
              break;
            case "dicedice": // roll special dice
              game.setRolledDice(answer[1], 4);
              break;
            case "quiz": // quiz answer
              game.quizAnswer(answer[1], answer[2]);
              break;
            case "wwcd": // cheat code
              game.cheat(answer[1], Integer.parseInt(answer[2]));
              break;
            case "skandal":
              game.cheatDice(answer[1], Integer.parseInt(answer[2]));
              break;
          }
        }
      }
      System.out.println("stopped lobby: " + name);
    });
    LobbyWaitForMessageThread.setName("LobbyWaitForMessageThread"); // set name of thread
    LobbyWaitForMessageThread.start(); // start thread
  }

  /**
   * Returns a String with the highscore of the top 10 players or "empty High Score" if high score is empty.
   *
   * @param clientHandler User asking for the high score
   */
  public void getHighScore(server.ClientHandler clientHandler) {
    sendToClient.send(clientHandler, CommandsToClient.PRINT, highScore.getTop10("global"));
    sendToClient.send(clientHandler, CommandsToClient.PRINTWINNERSGUI, highScore.getTop10("global"));
  }

  /**
   * returns whether enough people have joined a game to start it or not
   *
   * @return boolean
   */
  public boolean getIsReadyToStartGame() {
    return (usersReady.size() >= minToStart);
  }

  /**
   * returns the game of this lobby
   *
   * @return the game of this lobby
   */
  public Game getGame() {
    return game;
  }

  /**
   * returns the charactersTaken HashMap
   * @return HashMap
   */
  public HashMap<Integer, Boolean> getCharactersTaken() {
    return charactersTaken;
  }

  /**
   * Sends a message to all lobby members
   *
   * @param msg Message to be sent to the users
   */
  public void lobbyBroadcastToPlayer(String msg) {
    sendToClient.lobbyBroadcast(getUsersInLobby(), CommandsToClient.PRINTGUIGAMETRACKER, msg);
  }


  /**
   * stops the lobby
   */
  public void stopLobby() {
    stop = true;
    receiveFromProtocol.setMessage("-1");
  }
}
