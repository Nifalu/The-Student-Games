package Game;

import java.io.IOException;
import java.net.*;


/**
 * The User Object holds various information that is connected to this individual User.
 * Since this User Object is known by the Game, it can also store game related information
 * like avatars, positions, scores etc...
 */
public class User {

  private final int id;
  private InetAddress ip;
  private String username;
  private boolean firstTime = true;
  private boolean isConnected = true;
  private final int  district;
  private DatagramSocket socket;
  private int port;

  private Thread send;

  public User(InetAddress ip, String username, int district, int id, int port) {
    this.id = id;
    this.ip = ip;
    this.port = port;
    this.username = username;
    this.district = district;
  }

  /**
   * LOGIN
   * @param address
   * @return
   */
  public boolean login(String address) {
    try {
      socket = new DatagramSocket();
      ip = InetAddress.getByName(address);
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return false;
    } catch (SocketException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public String receive() {
    byte[] data = new byte[1024];
    DatagramPacket packet = new DatagramPacket(data, data.length);
    try {
      socket.receive(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String message = new String(packet.getData());
    return message;
  }

  public void send(final byte[] data) {
    send = new Thread("Send") {
      public void run() {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
          socket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
    send.start();
  }

  private void read(DatagramPacket packet) {
    String message = new String(packet.getData());
//		log(message);
    System.out.println("Received a message" + message);
  }


  // ----- GETTERS -----
  public String getUsername() {
    return username;
  }

  public int getDistrict() { return district; }

  public int getId() {
    return id;
  }

  public boolean isFirstTime() {
    return firstTime;
  }

  public boolean isConnected() {
    return isConnected;
  }

  // ----- SETTERS -----
  public void setUsername(String username) {
    this.username = username;
  }

  public void setFirstTime(boolean firstTime) {
    this.firstTime = firstTime;
  }

  public void setIsConnected(boolean isConnected) {
    this.isConnected = isConnected;
  }

}

