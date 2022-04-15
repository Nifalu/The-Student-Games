package Server;


import org.apache.logging.log4j.*;
import utility.Exceptions;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * GameServer is the Container in which everything takes place.
 * Clients can connect to this GameServer with an Ip-Address and Port.
 */
public class GameServer {

  static ServerSocket serverSocket;
  private static final Logger logger = LogManager.getLogger(GameServer.class);

  public static void runGameServer(int port) {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      Exceptions.failedToCreateServer(e, port);
      return;
    }

    try {
      logger.info("Server is running");
      logger.trace("trace");
      logger.error("error");
      logger.trace("trace2");
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
  public static void closeGameServer() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
