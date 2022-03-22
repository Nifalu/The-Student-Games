package Game;

import java.net.InetAddress;


/**
 * The User Object holds various information that is connected to this individual User.
 * Since this User Object is known by the Game, it can also store game related information
 * like avatars, positions, scores etc...
 */
public class User {

  private final int id;
  private final InetAddress ip;
  private String username;
  private boolean firstTime = true;
  private final int district;

  public User(InetAddress ip, String username, int district, int id) {
    this.id = id;
    this.ip = ip;
    this.username = username;
    this.district = district;
  }


  // ----- GETTERS -----
  public String getUsername() {
    return username;
  }

  public int getDistrict() {
    return district;
  }

  public int getId() {
    return id;
  }

  public InetAddress getIp() {
    return ip;
  }

  public boolean isFirstTime() {
    return firstTime;
  }


  // ----- SETTERS -----
  public void setUsername(String username) {
    this.username = username;
  }

  public void setFirstTime(boolean firstTime) {
    this.firstTime = firstTime;
  }


}

