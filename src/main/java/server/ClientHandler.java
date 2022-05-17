package server;

import gameLogic.CreateLobbyHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToClient;
import utility.io.SendToClient;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Each client is connected to a separate thread of this class.
 * The ClientHandler handles the connection between the client and itself.
 * It starts Threads to handle incoming messages in the background and continuously check for connection loss.
 * <p>
 * The first message the ClientHandler receives will be the initial Username of this client.
 */
public class ClientHandler implements Runnable {

  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(ClientHandler.class);

  /**
   * the connection to the client
   */
  private Socket socket; // connection to the client

  /**
   * the output stream
   * receives data
   */
  private BufferedWriter out; // receive data

  //Objects:
  /**
   * the user
   * the ClientHandler knows which user the client belongs to
   */
  public User user = new User(this,"tmp", String.valueOf(ServerManager.getUserlist().size()),true);

  /**
   * Name object used to communicate with the Name class
   */
  public Name nameClass = new Name(this);

  /**
   * a chat
   */
  public Chat chat = new Chat();

  /**
   * SendToClien object used to communicate with the client
   */
  private final SendToClient sendToClient = new SendToClient();

  /**
   * the clients lobbyhelper object
   */
  public gameLogic.CreateLobbyHelper lobbyhelper = new CreateLobbyHelper(this);

  //Threads:
  /**
   * the clienthandler input
   */
  private ClientHandlerIn clientHandlerIn;

  /**
   * the clienthandler input thread
   */
  private Thread clientHandlerInThread;

  /**
   * monitors the connection to the client
   */
  private ConnectionToClientMonitor connectionToClientMonitor;

  /**
   * the thread for the ConnectionToClientMonitor object
   */
  private Thread connectionMonitor;


  /**
   * creates a new ClientHandler
   *
   * @param socket Socket
   */
  public ClientHandler(Socket socket) {
      this.socket = socket;
  }

  /**
   * First asks the user want name they want to choose and after that if they want to create a new lobby or
   * join an existing one.
   */
  @Override
  public void run() {
    setup();
    System.out.println(user.getUsername() + " has connected to the Server");
    connectionMonitor.start();
    nameClass.askUsername(); // Asks the User if he's fine with his name or wants to change
    lobbyhelper.askWhatLobbyToJoin(this);
  }

  /**
   * sets up the clientHandler for a client
   */
  private void setup() {
    try {
      // send data
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

      // Gets the Home-directory name of the client and creates a User
      this.nameClass = new Name(this);
      String login = in.readLine();
      user = ServerManager.connect(this, login, nameClass);
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

    } catch (IOException e) {
      logger.error("An Error occurred when setting up the ClientHandler. ", e);
      disconnectClient();
    }
  }



  /**
   * "Does everything that needs to be done when a client disconnects."
   * Closes the In/Out-Streams, the socket and removes the client from the ActiveClientList.
   */
  public void disconnectClient() {

    String tmp_name = user.getUsername();
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    ServerManager.getActiveClientList().remove(this);
    user.setOnline(false);
    //ServerManager.getUserlist().remove(user.getUserListNumber(), user); // ????????????????????????????????????????????????????????
    sendToClient.serverBroadcast(CommandsToClient.CHAT, user.getUsername() + " from district " + user.getDistrict() + " has left");
    // close thread
    if (connectionMonitor.isAlive()) {
      connectionToClientMonitor.requestStop();
    }

    if (clientHandlerInThread.isAlive()) {
      clientHandlerIn.requestStop();
    }

    // close streams
    try {
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      logger.warn(tmp_name + " did not exit correctly!", e);
    }
  }

  /**
   * returns the Outputstream of this ClientHandler
   *
   * @return BufferedWriter
   */
  public synchronized BufferedWriter getOut() {
    return out;
  }


  /**
   * returns the ConnectionToClientMonitor
   *
   * @return ConnectionToClientMonitor
   */
  protected ConnectionToClientMonitor getConnectionToClientMonitor() {
    return connectionToClientMonitor;
  }


  /**
   * this constructor creates a fake clienthandler for unit testing
   * it cannot receive and does print to console instead of sending to a client
   * @param username
   */
  public ClientHandler(String username) {
    this.user = ServerManager.connect(this, username, nameClass);
    this.out = new BufferedWriter(new OutputStreamWriter(new PrintStream(System.out)));
  }

}
