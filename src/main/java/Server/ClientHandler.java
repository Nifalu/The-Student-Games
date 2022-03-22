package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {



  Game.Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  public Game.User user; // ClientHandler knows which User the Client belongs to
  DataInputStream in; // Receive data from client
  DataOutputStream out; // Send data to client


  public ClientHandler(Socket socket, Game.Game game) throws IOException {
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
    try {
      String username = askUsername();

      user = game.connect(socket.getInetAddress(), this, username);
      welcomeUser();
      String answer = proposeUsername();
      String newUsername = usernameProposals();
      if (answer.equalsIgnoreCase("YES")) {
        user.setUsername(newUsername);
        out.writeUTF("Your username has been changed to " + "\"" + user.getUsername() + "\"" + ".");
      } else {
        out.writeUTF("Your username remains " + "\"" + user.getUsername() + "\"" + ".");
      }

      // PING Server
      while (!socket.isClosed()) {
        BufferedReader inputStream = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        if (inputStream.read() != -1) {

          String input = inputStream.readLine();
          // Stream valid input to console
          out.writeUTF(ServerProtocol.get(game, user, input));
        }

        // Client has lost connection
        if (inputStream.read() == -1) {

          System.out.println(user.getUsername() + " has left the Game.");
          socket.close();
          //out.writeUTF("QUIT");
          disconnectClient(socket, in, out);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void send(String s) {
    try {
      out.writeUTF(s);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   *
   */
  private void welcomeUser() {
    try {
      System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has connected");
      out.writeUTF("Your name was drawn at the reaping. Welcome to the Student Games, " + user.getUsername() + " from district " + user.getDistrict() + "!");
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
    //System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
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

  /**
   * method sends a message to all clients
   */



  public String askUsername() throws IOException {
    out.writeUTF("Please enter your name: ");
    return in.readUTF();
  }

  public String proposeUsername() throws IOException{
    String proposedUsername = usernameProposals();
    out.writeUTF("Would you like to change your username to " + "\"" + proposedUsername + "\"?");
    return in.readUTF();
  }
  public String usernameProposals() {
    String proposedUsername = user.getUsername() + "_" + user.getDistrict();
    return proposedUsername;
  }

}
