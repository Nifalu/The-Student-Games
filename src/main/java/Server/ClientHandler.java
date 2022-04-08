package Server;

import GameLogic.CreateLobbyHelper;
import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import gui.*;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler handles the connection between the client and itself.
 * It starts Threads to handle incoming messages in the background and continuously check for connection loss.
 * <p>
 * The first message the ClientHandler receives will be the initial Username of this Client.
 */
public class ClientHandler implements Runnable {

  //close
  private volatile boolean stop = false; // stop the thread

  //Connection:
  private Socket socket; // connection to the client

  //Streams:
  private BufferedReader in; // send data
  private BufferedWriter out; // receive data

  //Objects:
  public User user; // ClientHandler knows which User the Client belongs to
  public Name nameClass = new Name(this);
  public Chat chat = new Chat();
  private final SendToClient sendToClient = new SendToClient();
  public GameLogic.CreateLobbyHelper lobbyhelper = new CreateLobbyHelper(this);
  FXMLExampleController testController = new FXMLExampleController(); // has it's own controller for the chat GUI

  //Threads:
  private ClientHandlerIn clientHandlerIn;
  private Thread clientHandlerInThread;

  private ConnectionToClientMonitor connectionToClientMonitor;
  private Thread connectionMonitor;


  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;

      // creating Streams
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

      // Gets the Home-directory name of the Client and creates a User
      this.nameClass = new Name(this);
      user = ServerManager.connect(this.socket.getInetAddress(), this, "Player_" + ServerManager.userlist.size());
      String ClientHomeDirectoryName = in.readLine();
      ClientHomeDirectoryName = nameClass.proposeUsernameIfTaken(ClientHomeDirectoryName);
      user.setUsername(ClientHomeDirectoryName);
      this.lobbyhelper = new CreateLobbyHelper(this);

      // Creates a receiving Thread that receives messages in the background
      this.clientHandlerIn = new ClientHandlerIn(this, in);
      this.clientHandlerInThread = new Thread(clientHandlerIn);
      clientHandlerInThread.setName(user.getUsername() + "'s ClientHandlerIn Thread");
      clientHandlerInThread.start();

      // Creates a ConnectionMonitor Thread that detects when the connection times out.
      this.connectionToClientMonitor = new ConnectionToClientMonitor(this);
      connectionMonitor = new Thread(connectionToClientMonitor);
      connectionMonitor.setName("connectionMonitor  Thread");
      connectionMonitor.start();


    } catch (IOException e) {
      disconnectClient();
    }
  }


  @Override
  public void run() {
    nameClass.askUsername(); // Asks the User if he's fine with his name or wants to change
    lobbyhelper.askWhatLobbyToJoin(this);
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

    if (clientHandlerInThread.isAlive()) {
      clientHandlerIn.requestStop();
    }

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
      System.out.println("Everything closed");
    }
  }

  /**
   * Sends goodbye message and removes this ClientHandler from all lists its in.
   */
  public void goodbye() {
    sendToClient.serverBroadcast(CommandsToClient.PRINT, user.getUsername() + " from district " + user.getDistrict() + " has left");
    ServerManager.getActiveClientList().remove(this);
    ServerManager.getUserlist().remove(user.getId(), user);
  }

  /**
   * exits possible infinite loops to end the Thread
   */
  protected void requestStop() {
    stop = true;
  }

  public synchronized BufferedWriter getOut() {
    return out;
  }

  protected ConnectionToClientMonitor getConnectionToClientMonitor() {
    return connectionToClientMonitor;
  }
}
