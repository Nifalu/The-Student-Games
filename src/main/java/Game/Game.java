package Game;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Random;


/**
 * This is the Center of the Game itself. Right now it only adds and contains its Users.
 */
public class Game {

  final HashMap<InetAddress, User> userlist = new HashMap<>();

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
  public User connect(InetAddress ip, String username) {
    // new user is added
    if (!userlist.containsKey(ip)) {
      // generates and allocates district to new user
      int district = assignDistrict();
      User user = new User(ip, username, district, userlist.size());
      userlist.put(ip, user);
    } else {
      // known user is not firstTime anymore
      userlist.get(ip).setFirstTime(false);
    }
    return userlist.get(ip);
  }

  // generates a random district for new clients
  // max. 2 clients per district! (TO DO)
  public int assignDistrict(){
    Random random = new Random();
    return random.nextInt(12) + 1;
  }

}
