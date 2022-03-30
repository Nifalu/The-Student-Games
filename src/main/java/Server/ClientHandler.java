package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {
  
  Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  ConnectionToClientMonitor connectionToClientMonitor;
  public User user; // ClientHandler knows which User the Client belongs to
  BufferedReader in; // send data
  BufferedWriter out; // receive data
  private volatile boolean stop = false; // stop the thread
  Thread connectionMonitor;
  public Name nameClass;
  ClientHandlerIn clientHandlerIn;
  Thread clientHandlerInThread;


  public ClientHandler(Socket socket, Game game) {
    try {
      this.socket = socket;
      this.game = game;

      // creating Streams
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

      // Gets the Home-directory name of the Client and creates a User
      this.nameClass = new Name(this);
      String ClientHomeDirectoryName = in.readLine();
      ClientHomeDirectoryName = nameClass.proposeUsernameIfTaken(ClientHomeDirectoryName);
      user = game.connect(this.socket.getInetAddress(), this,  ClientHomeDirectoryName);

      // Creates a receiving Thread that receives messages in the background
      this.clientHandlerIn = new ClientHandlerIn(this, in);
      this.clientHandlerInThread = new Thread(clientHandlerIn);
      clientHandlerInThread.start();

      // Creates a ConnectionMonitor Thread that detects when the connection times out.
      this.connectionToClientMonitor = new ConnectionToClientMonitor(this);
      connectionMonitor = new Thread(connectionToClientMonitor);
      connectionMonitor.start();



    } catch (IOException e) {
      disconnectClient();
    }
  }


  @Override
  public void run() {
    nameClass.askUsername();
  }
  /**
   * "Send a String to the Client."
   * Use this instead of "out.write" to avoid errors.
   * It converts the given String to bytes before sending.
   */
  public void send(String msg) {
    try {
      if (msg.equals("-1")) {
        return;
      }
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      System.out.println("cannot reach user" );
    }
  }
  /**
   * "Does everything that needs to be done when a Client disconnects."
   * Closes the In/Out-Streams, the socket and removes the Client from the ActiveClientList.
   */
  public void disconnectClient() {

    if (user != null) {
      goodbye();
    }
    // close thread
    if (connectionMonitor.isAlive()) {
      connectionToClientMonitor.requestStop();
    }
    requestStop();

    // close streams
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

  public void goodbye() {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    game.getActiveClientList().remove(this);
    game.getUserlist().remove(user.getId(),user);
  }

  public void requestStop() {
    stop = true;
  }

}
