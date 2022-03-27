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

  /**
   * assigns districts from 1-12. There shouldn't be more than 2 clients in one district.
   * district one is "reserved" in case that the other ones get full.
   * @return
   */
  public int assignDistrict(){
    Random random = new Random();
    int counter = 0;
    int randomInt = random.nextInt(11) + 2;
    for (int i  = 0; i < userlist.size(); i++) {
      if (randomInt == userlist.get(i).getDistrict()) {
        counter++;
      }
    }
    if (counter < 2) {
      return randomInt;
    } else {
      return 1;
    }
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
