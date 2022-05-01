package server;

import gameLogic.GameList;
import gameLogic.Lobby;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The ServerManager manages everything not-Game-related on the server.
 * It knows who's online or connect new Users.
 */
public class ServerManager {

  /**
   * a list with all users
   */
  final static HashMap<Integer, User> userlist = new HashMap<>();

  /**
   * a list with all currently active clients (connected ClientHandlers)
   */
  final static ArrayList<server.ClientHandler> activeClientList = new ArrayList<>();


  /**
   * Creates a new User, Adds the User to the Lists.
   *
   * @param clientHandler ClientHandler
   * @param username      String
   * @return User
   */
  public synchronized static User connect(ClientHandler clientHandler, String username) {
    // new user is added
    User user = new User(clientHandler, username);
    userlist.put(userlist.size(), user);
    user.setUserListNumber(userlist.size());
    activeClientList.add(clientHandler);
    return user;
  }

  /**
   * removes a user from the userlist
   *
   * @param user User
   */
  public synchronized static void disconnect(User user) {
    userlist.remove(user.getUserListNumber() - 1, user);
  }


  /**
   * Creates the Main Lobby where everyone is in when they connect to the server or leave a game.
   */
  public static synchronized void createMainLobby() {
    Lobby lobby = new Lobby("StandardLobby");
    GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
    lobby.setLobbyStatusToStandard();
  }

  /**
   * returns an arraylist with all actively connected Clients
   *
   * @return ArrayList
   */
  public synchronized static ArrayList<ClientHandler> getActiveClientList() {
    return activeClientList;
  }

  /**
   * Returns a Hashmap with all current Users
   *
   * @return HashMap
   */
  public synchronized static HashMap<Integer, User> getUserlist() {
    return userlist;
  }


}
