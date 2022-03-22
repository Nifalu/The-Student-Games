package Game;

import Server.ClientHandler;
import Server.GameServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * This is the Center of the Game itself. Right now it only adds and contains its Users.
 */
public class Game {

  final HashMap<Integer, User> userlist = new HashMap<>();
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
    // new user is added
    int district = assignDistrict();
    User user = new User(ip, username, district, userlist.size() + 1);
    activeClientList.add(clientHandler);
    return user;
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

  /**
   * method sends a message to all clients
   */

  public void broadcastMessage(User sender, String msg) {
    // goes through all clients
    for (ClientHandler clientHandler : activeClientList) {

      // send message to everyone but not yourself

        if (clientHandler.user != sender) {
          clientHandler.send(msg);
        }
    }
  }
}
