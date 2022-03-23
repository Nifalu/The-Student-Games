package Server;

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {
  
  Game.Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  public Game.User user; // ClientHandler knows which User the Client belongs to
  InputStream in; // Receive data from client
  OutputStream out; // Send data to client


  public ClientHandler(Socket socket, Game.Game game) throws IOException {
    this.socket = socket;
    this.game = game;
    try {
      this.in = socket.getInputStream();
      this.out = socket.getOutputStream();
    } catch (IOException e) {
      disconnectClient(socket, in, out);
    }
  }


  @Override
  public void run() {

    // Identifies the new Client
    try {
      askUsername();
      /* ----------- receive incoming msg and check for connection loss -----------------------------

      A StringBuilder (sb) appends every received byte (c) to a String until a break-character is found.
      Then the break-character is removed and the String is given to the Server Protocol.
      The result (return) of the Server Protocol is then sent back to the Client.
      After that the String is cleared, the counter is set to 0 and the StringBuilder starts a new String.

      If String equals "QUIT", the ServerProtocol is given the message but nothing is sent back.
      The loop breaks and the Client is disconnected
      */

      int c;
      int i = 0;
      StringBuilder sb = new StringBuilder();

      while ((c = in.read()) != -1) {
        sb.append((char) c);
        if (sb.toString().charAt(i) == ';') {
          sb.delete(i, i + 1);
          if (sb.toString().equalsIgnoreCase("QUIT")) {
            ServerProtocol.get(game, user, sb.toString());
            break;
          }
          send(ServerProtocol.get(game, user, sb.toString()));
          sb.delete(0, i + 1);
          i = 0;
        } else {
          i++;
        }
      }

      disconnectClient(socket, in, out);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * "Send a String to the Client."
   * Use this instead of "out.write" to avoid errors.
   * It converts the given String to bytes before sending.
   */
  public void send(String msg) {
    try {
      out.write((msg + ';').getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * "Receive a String from the Client"
   * A StringBuilder appends every incoming byte to a String until a certain break-character is found.
   * Then removes the break-character and returns the String.
   */
  public String receive() {
    int c;
    int i = 0;
    StringBuilder sb = new StringBuilder();

    try {
      while (true) {
        c = in.read();
        sb.append((char) c);

        if (sb.toString().charAt(i) == ';') {
          sb.delete(i, i + 1);
          break;

        } else {
          i++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }


  /**
   * "Does everything that needs to be done when a Client disconnects."
   * Closes the In/Out-Streams, the socket and removes the Client from the ActiveClientList.
   */
  public void disconnectClient(Socket socket, InputStream in, OutputStream out) {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    game.getActiveClientList().remove(this);
    try {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void welcomeUser() {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has connected");
    send("Your name was drawn at the reaping. Welcome to the Student Games, " + user.getUsername() + " from district " + user.getDistrict() + "!");
  }

  public void askUsername() {
    try {
      proposeUsernameBasedOnSystemName();
    } catch (UnknownHostException e) {
      e.printStackTrace(); //muss noch eine bessere Lösung hin
    }
    send("Please enter your name: ");
    String answer = receive();
    user = game.connect(socket.getInetAddress(), this, answer);
    proposeUsername();
    if (nameAlreadyExists(user.getUsername())) {
      proposeUsernameIfTaken();
    } else {
      welcomeUser();
    }
  }

  public void proposeUsername() {
    String proposedUsername = user.getUsername() + "_" + user.getDistrict();
    send("We found a way cooler username for you! Would you like to change it to " + "\"" + proposedUsername + "\"?");
    String answer = receive();
    if (answer.equalsIgnoreCase("YES")) {
      user.setUsername(proposedUsername);
      send("Your username has been changed to " + "\"" + user.getUsername() + "\"" + ".");
    }
  }
  public void proposeUsernameBasedOnSystemName() throws UnknownHostException {
    InetAddress ip = InetAddress.getLocalHost();
    String systemName = ip.getHostName();
    send("Hello there, would you like to be named " + systemName + "?");
  }

  public void proposeUsernameIfTaken() {
    String newName = user.getUsername() + ".1";
    send("Oooops that one was already taken, but here's a new one: " + newName + ":)");
    user.setUsername(newName);
    welcomeUser();
  }

  public void getUsernamesInGame() {
    String s = "";
    int length = game.getUserlist().size() - 1;
    for (int i = 0; i < length; i++) {
      s = s + " " + game.getUserlist().get(i).getUsername();
    }
    send("Users in game:" + s);
  }
  public boolean nameAlreadyExists(String name) {
    boolean alreadyExists = false;
    int length = game.getUserlist().size() - 1;
    int counter = 0;
    for (int i = 0; i < length; i++) {
      String username = game.getUserlist().get(i).getUsername();
      if (username.equalsIgnoreCase(name)) {
        counter++;
      }
    }
    if (counter > 0) {
      alreadyExists = true;
    }
    return alreadyExists;
  }

}
