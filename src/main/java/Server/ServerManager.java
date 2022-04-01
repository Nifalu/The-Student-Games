package Server;

import utility.CommandsToClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * The ServerManager manages everything not-Game-related on the Server.
 * It knows who's online or connect new Users.
 */
public class ServerManager {

  // List with all Users
  final static HashMap<Integer, User> userlist = new HashMap<>();
  // List with all currently Active Clients (connected ClientHandlers)
  final static ArrayList<Server.ClientHandler> activeClientList = new ArrayList<>();


  /**
   * Creates a new User, Adds the User to the Lists.
   *
   * @param ip            InetAddress
   * @param clientHandler ClientHandler
   * @param username      String
   * @return User
   */
  public synchronized static User connect(InetAddress ip, ClientHandler clientHandler, String username) {
    // new user is added
    User user = new User(clientHandler, ip, username, userlist.size());
    userlist.put(userlist.size(), user);
    activeClientList.add(clientHandler);
    return user;
  }

  public synchronized static ArrayList<ClientHandler> getActiveClientList() {
    return activeClientList;
  }

  public synchronized static HashMap<Integer, User> getUserlist() {
    return userlist;
  }


}
