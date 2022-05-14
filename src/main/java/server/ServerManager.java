package server;

import gameLogic.GameList;
import gameLogic.Lobby;
import utility.io.SendToClient;

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
   * @param client ClientHandler
   * @param login String
   * @param nameclass Name
   * @return User
   */
  public synchronized static User connect(ClientHandler client, String login, Name nameclass) {
    // retrieve username and uuid from login-string
    String[] splitter = login.split("!");
    String name = splitter[0];
    String uuid = splitter[1];

    // if a user with the given uuid already exists, connect the clienthandler with the user and return the user.
    User user;
    for (int i = 0; i < ServerManager.getUserlist().size(); i++) {
      if ((user = ServerManager.getUserlist().get(i)).getUuid().equals(uuid)) {
        System.out.println("Found user in list");
        if (user.isOnline()) {
          System.out.println("Found user in list -> is online");
          user.getClienthandler().disconnectClient(); // if a single user tries to connect twice, disconnect the first.
        }
        user.setClienthandler(client);
        activeClientList.add(client);
        user.setOnline(true);
        user.setNotGameOver();
        return user;
      }
    }
    // if no user with the given uuid is found, create a new user.
    System.out.println("no user was found. creating new one");
    name = nameclass.proposeUsernameIfTaken(name);
    user = new User(client, name, uuid, true);
    userlist.put(userlist.size(), user);
    activeClientList.add(client);

    return user;
  }

  /**
   * removes a user from the userlist
   *
   * @param user User
   */
  public synchronized static void disconnect(User user) {
    //userlist.remove(user.getUserListNumber() - 1, user);
  }


  /**
   * Creates the Main Lobby where everyone is in when they connect to the server or leave a game.
   */
  public static synchronized void createMainLobby() {
    Lobby lobby = new Lobby("ZG", true);
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
