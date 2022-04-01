package Server;

import java.net.InetAddress;
import java.util.Random;


/**
 * The User Object holds various information that is connected to this individual User.
 * Since this User Object is known by the ServerManager, it can also store serverManager related information
 * like avatars, positions, scores etc...
 */
public class User {

  private final ClientHandler clienthandler;
  private final int id;
  private final InetAddress ip;
  private String username;
  private boolean firstTime = true;
  private final int district;

  public User(ClientHandler clientHandler, InetAddress ip, String username, int id) {
    this.clienthandler = clientHandler;
    this.id = id;
    this.ip = ip;
    this.username = username;
    this.district = assignDistrict();
  }

  /**
   * assigns districts from 1-12. There shouldn't be more than 2 clients in one district.
   * district one is "reserved" in case that the other ones get full.
   *
   * @return randomInt or 1
   */
  public synchronized static int assignDistrict() {
    Random random = new Random();
    return random.nextInt(12) + 1;
  }


  // ----- GETTERS -----
  public synchronized String getUsername() {
    return username;
  }

  public synchronized int getDistrict() {
    return district;
  }

  public synchronized int getId() {
    return id;
  }

  public synchronized InetAddress getIp() {
    return ip;
  }

  public synchronized boolean isFirstTime() {
    return firstTime;
  }

  public synchronized ClientHandler getClienthandler() {
    return clienthandler;
  }

  // ----- SETTERS -----
  public synchronized void setUsername(String username) {
    this.username = username;
  }

  public synchronized void setFirstTime(boolean firstTime) {
    this.firstTime = firstTime;
  }


}

