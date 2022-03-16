package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {


  Game.Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  Game.User user; // ClientHandler knows which User the Client belongs to
  DataInputStream in; // Receive data from client
  DataOutputStream out; // Send data to client


  public ClientHandler(Socket socket, Game.Game game) {
    this.socket = socket;
    this.game = game;
    try {
      this.in = new DataInputStream(socket.getInputStream());
      this.out = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      disconnectClient(socket, in, out);
    }
  }


  @Override
  public void run() {

    // Identifies the new Client
    user = game.connect(socket.getInetAddress(),this, socket.getInetAddress().getHostName());
    welcomeUser();

    try {
      String s;
      String[] input;

      // do chont vlt pinpong here??
      while (!(s = in.readUTF()).equals("QUIT")) {

        // Reads the incoming String and splits it into two parts
        // ( "instruction" and "value" ) which are saved in an array.
        input = s.split("-", 2); // Splits the String (limit - 1) times at the first "-"


        // Some possible Instructions. --> should be sent to a separate protocol class for more clarity.
        if (input[0].equals("CHANGENAME")) { // (use: CHANGENAME-YourNewName )
          System.out.println(user.getUsername() + "is now called: " + input[1]);
          user.setUsername(input[1]);
          out.writeUTF("Your new nickname is: " + user.getUsername());

        } else if (input[0].equals("PING")) { // (use: PING )
          System.out.println(user.getUsername() + ": " + input[0]);
          out.writeUTF("PONG");

        } else {
          out.writeUTF(input[0]);
        }


      }
      out.writeUTF("QUIT");
      disconnectClient(socket, in, out);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   *
   */
  private void welcomeUser() {
    try {
      if (user.isFirstTime()) {
        System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has connected");
        out.writeUTF("Your name was drawn at the reaping. Welcome to the Student Games, " + user.getUsername() + " from district " + user.getDistrict() + "!");
      } else {
        System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has reconnected");
        out.writeUTF("Welcome back " + user.getUsername() + " from district " + user.getDistrict() + "!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Disconnects this Client from the Server, sets the status of the user to "disconnected".
   * Closes the socket and all streams.
   *
   * @param socket Socket
   * @param in     DataInputStream
   * @param out    DataOutputStream
   */
  public void disconnectClient(Socket socket, DataInputStream in, DataOutputStream out) {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    user.setIsConnected(false);
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

}
