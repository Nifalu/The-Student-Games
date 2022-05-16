package gameLogic;

import server.ClientHandler;
import server.ServerManager;
import server.User;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * GameList class the contains all the list which are relevant in the game.
 */
public class GameList {

  /**
   * HashMap with all the Lobbies, can be accessed via the lobby number.
   */
  private static final HashMap<Integer, Lobby> lobbyList = new HashMap<>();

  /**
   * returns the lobbylist
   *
   * @return LobbyList
   */
  public synchronized static HashMap<Integer, Lobby> getLobbyList() {
    return lobbyList;
  }

  /**
   * returns the userlist
   *
   * @return userlist
   */
  public synchronized static HashMap<Integer, User> getUserlist() {
    return ServerManager.getUserlist();
  }

  /**
   * returns a String of all users
   *
   * @return String
   */
  public synchronized static String printUserList() {
    String s = "";
    int i;
    for (Iterator<Integer> it = getUserlist().keySet().iterator(); it.hasNext();) {
      i = it.next();
      s = s + " " + getUserlist().get(i).getUsername();
    }
    return s;
  }

  /**
   * prints out users in lobby
   *
   * @param lobby Lobby
   * @return String
   */
  public synchronized static String printUserInLobby(Lobby lobby) {
    ArrayList<ClientHandler> userList = ServerManager.getActiveClientList();
    String s = "";
    ClientHandler client;
    for (Iterator<ClientHandler> it = userList.iterator(); it.hasNext();) {
      client = it.next();
      if (client.user.getLobby().equals(lobby)) {
        s = s + "      -" + client.user.getUsername() + "%";
      }
    }
    return s;
  }

  /**
   * prints out all users in a lobby
   *
   * @return String
   */
  public synchronized static String printLoungingList() {
    String print = "";
    int i;
    for (Iterator<Integer> it = getLobbyList().keySet().iterator(); it.hasNext();) {
      i = it.next();
      print = print + getLobbyList().get(i).getLobbyName() + "%" + printUserInLobby(getLobbyList().get(i));
    }
    return print;
  }

  /**
   * prints out all open lobbies
   *
   * @param lobbyList Hashmap with all lobbies
   * @return a String of all open lobbies
   */
  public synchronized static String printLobbies(HashMap<Integer, Lobby> lobbyList) {
    String s = "";
    int counter = 0;
    int i;
    for (Iterator<Integer> it = lobbyList.keySet().iterator(); it.hasNext();) {
      i = it.next();
      int lobbyStatus = lobbyList.get(i).getLobbyStatus();
      String lobbyStatusString = "";
      switch (lobbyStatus) {
        case 1:
          lobbyStatusString = "open";

          break;
        case 0:
          lobbyStatusString = "ongoing";
          break;
        case -1:
          lobbyStatusString = "finished";
          break;
        case 69:
          lobbyStatusString = "Zwischengeschoss";
          break;
      }
      s = s + counter + ". " + lobbyList.get(i).getLobbyName() + " [" + lobbyStatusString + "] §";
      counter++;
    }
    return s;
  }

  /**
   * @return all the lobbies with the status open, which is saved in the lobby object as an int.
   * The status open is saved as 1.
   */
  public synchronized static HashMap<Integer, Lobby> getOpenLobbies() {
    HashMap<Integer, Lobby> openLobbies = new HashMap<>();
    int activeLobbyCounter = 0;
    int i;
    for (Iterator<Integer> it = getLobbyList().keySet().iterator(); it.hasNext();) {
      i = it.next();
      if (lobbyList.get(i).getLobbyStatus() == 1) {
        openLobbies.put(activeLobbyCounter, lobbyList.get(i));
        activeLobbyCounter++;
      }
    }
    return openLobbies;
  }

  /**
   * @return all the lobbies with the status finished, which is saved in the lobby object as an int.
   * The status finished is saved as -1.
   */
  public synchronized static HashMap<Integer, Lobby> getFinishedLobbies() {
    HashMap<Integer, Lobby> finishedLobbies = new HashMap<>();
    int InActiveLobbyCounter = 0;
    int i;
    for (Iterator<Integer> it = getLobbyList().keySet().iterator(); it.hasNext();) {
      i = it.next();
      if (lobbyList.get(i).getLobbyStatus() == -1) {
        finishedLobbies.put(InActiveLobbyCounter, lobbyList.get(i));
        InActiveLobbyCounter++;
      }
    }
    return finishedLobbies;
  }

  /**
   * @return all the lobbies with the status on going, which is saved in the lobby object as an int.
   * The status on going is saved as 0.
   */
  public synchronized static HashMap<Integer, Lobby> getOnGoingLobbies() {
    HashMap<Integer, Lobby> OnGoingLobbies = new HashMap<>();
    int OnGoingLobbyCounter = 0;
    int i;
    for (Iterator<Integer> it = getLobbyList().keySet().iterator(); it.hasNext();) {
      i = it.next();
      if (lobbyList.get(i).getLobbyStatus() == 0) {
        OnGoingLobbies.put(OnGoingLobbyCounter, lobbyList.get(i));
        OnGoingLobbyCounter++;
      }
    }
    return OnGoingLobbies;
  }

}