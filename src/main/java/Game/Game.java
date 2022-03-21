package Game;

import Client.GameClient;
import Server.ClientHandler;
import Server.GameServer;

import javax.swing.*;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * This is the Center of the Game itself. Right now it only adds and contains its Users.
 */
public class Game implements Runnable{

  final HashMap<InetAddress, User> userlist = new HashMap<>();
  final ArrayList<Server.ClientHandler> activeClientList = new ArrayList<>();

  public boolean running = false;

  private GameClient socketClient;
  private GameServer socketServer;

  /**
   * Game window
   */
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
  public User connect(InetAddress ip,ClientHandler clientHandler, String username, int port) {
    // new user is added
    if (!userlist.containsKey(ip)) {
      // generates and allocates district to new user
      int district = assignDistrict();
      User user = new User(ip, username, district, userlist.size(), port);
      userlist.put(ip, user);
    } else {
      // known user is not firstTime anymore
      userlist.get(ip).setFirstTime(false);
    }
    //sending PING to the server
    //socketClient.sendData("ping".getBytes());

    // Adds Client to activeClientList:
    activeClientList.add(clientHandler);

    return userlist.get(ip);
  }

  public synchronized void start() {
    running = true;
    new Thread(this).start();
    socketClient = new GameClient(this, "localhost");
    socketClient.start();
  }

  public synchronized void stop() {
    running = false;
  }

  public void run() {
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
