package Game;

import Server.ClientHandler;
import Server.GameServer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * This is the Center of the Game itself. Right now it only adds and contains its Users.
 */
public class Game {

  final HashMap<String, User> userlist = new HashMap<>();
  final ArrayList<Server.ClientHandler> activeClientList = new ArrayList<>();

  public Game() {
  }


  /**
   * Connects a Client to the game.
   * If the Client has been connected before and is already in the list, the matching user is returned.
   * if the Client connects for the first time, a new User is created and added to the list.
   *
   * @param ip       IP Address
   * @param username Username
   * @return user
   */
  public User connect(InetAddress ip,ClientHandler clientHandler, String username) {
    // new user is added if name is not known to the server
    if (!userlist.containsKey(username)) {
      int district = assignDistrict(); // generates district for new user
      User user = new User(ip, username, district, userlist.size());
      userlist.put(username, user);
    } else {
      // known user is not firstTime anymore
      userlist.get(username).setFirstTime(false);
    }

    // Adds Client to activeClientList:
    activeClientList.add(clientHandler);

    return userlist.get(username);
  }

  // generates a random district for new clients
  // max. 2 clients per district! (TO DO)
  public int assignDistrict(){
    Random random = new Random();
    return random.nextInt(12) + 1;
  }

  public ArrayList<ClientHandler> getActiveClientList() {
    return activeClientList;
  }
}
