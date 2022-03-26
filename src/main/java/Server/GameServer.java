package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class GameServer implements Runnable {

  int port;
  ServerSocket serverSocket;

  private static final Random random = new Random();


  // Lists all active GameServers (Threads) in a List.
  // will be used to play several games at once
  private static final ArrayList<GameServer> gameServerList = new ArrayList<>();


  /**
   * Empty Constructor. Creates his own random Port Number between 2000 and 10000.
   * Should not be used when multiple instances are made.
   */
  public GameServer() {
    try {
      this.port = (2000 + random.nextInt(8000));
      this.serverSocket = new ServerSocket(port);
      gameServerList.add(this);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Constructor to use a given Port Number.
   *
   * @param port Port Number
   */
  public GameServer(int port) {
    try {
      this.port = port;
      this.serverSocket = new ServerSocket(port);
      gameServerList.add(this);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void run() {

    Game game = new Game();


    try {
      System.out.println("Server created: " + InetAddress.getLocalHost().getHostAddress() + " Port: " + port);
      System.out.println("Server is running and waiting for a connection... ");

      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept(); // program waits here until someone connects !

        // Each User gets his own thread
        ClientHandler clientHandler = new ClientHandler(socket, game);
        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.start();
      }

    } catch (IOException e) {
      e.printStackTrace();
      closeGameServer();
    }


  }


  // GETTER -> Returns list with all current GameServerThreads
  public static ArrayList<GameServer> getGameServerList() {
    return gameServerList;
  }


  /**
   * Removes this GameServer from the GameServer list and closes the ServerSocket.
   * Running this Method will disconnect all Clients connected to this Socket.
   * This Method should be called when this game has finished and all users have left.
   */
  public void closeGameServer() {
    try {
      gameServerList.remove(this);
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
