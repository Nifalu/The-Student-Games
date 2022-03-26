package Server;

import Server.ClientHandler;
import Server.User;

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
    User user = new User(clientHandler, ip, username, district, userlist.size());
    userlist.put(userlist.size(), user);
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

  public HashMap<Integer, User> getUserlist() {
    return userlist;
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

  /**
   * sends a message to one specific client
   * used for chatting between two clients (instead of chatting with everyone)
   * @param recipient String
   * @param msg String
   */
  public void whisper(String recipient, String sender, String msg) {
    for (ClientHandler clientHandler : activeClientList) {
      if(clientHandler.user.getUsername().equals(recipient)) {
        clientHandler.send(sender + " to " + recipient + ": " + msg);
      }
    }

  }


  // maybe add sendTo method to message one client
}
