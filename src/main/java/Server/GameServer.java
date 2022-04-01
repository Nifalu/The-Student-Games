package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * GameServer is the Container in which everything takes place.
 * Clients can connect to this GameServer with an Ip-Address and Port.
 */
public class GameServer implements Runnable {

  int port;
  ServerSocket serverSocket;

  private static final Random random = new Random();


  /**
   * Empty Constructor. Creates his own random Port Number between 2000 and 10000.
   * Should not be used when multiple instances are made.
   */
  public GameServer() {
    try {
      this.port = (2000 + random.nextInt(8000));
      this.serverSocket = new ServerSocket(port);

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

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void run() {

    try {
      System.out.println("Server created: " + InetAddress.getLocalHost() + ":" + port);
      System.out.println("Server is running and waiting for a connection... ");
      int i = 0;

      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept(); // program waits here until someone connects !

        // Each User gets his own thread
        ClientHandler clientHandler = new ClientHandler(socket);
        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.setName("ClientHandlerThread" + i);
        clientHandlerThread.start();
        i++;
      }

    } catch (IOException e) {
      e.printStackTrace();
      closeGameServer();
    }


  }

  /**
   * closes the ServerSocket
   */
  public void closeGameServer() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
