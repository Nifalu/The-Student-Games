package server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static utility.Exceptions.failedToConnectClientHandler;
import static utility.Exceptions.failedToCreateServer;

/**
 * GameServer is the Container in which everything takes place.
 * Clients can connect to this GameServer with an Ip-Address and Port.
 */
public class GameServer {

  static ServerSocket serverSocket;
  private static final Logger logger = LogManager.getLogger(GameServer.class);


  /**
   * Starts the GameServer on the given Port
   * @param port int port
   */
  public static void runGameServer(int port) {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      failedToCreateServer(e, port);
      return;
    }

    try {
      ServerManager.createMainLobby();
      logger.info("server is running! " + InetAddress.getLocalHost() + ":" + port);
      System.out.println("server created: " + InetAddress.getLocalHost() + ":" + port);
      System.out.println("server is running and waiting for a connection... ");

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
      failedToConnectClientHandler();
      logger.fatal("an error occurred when connecting a new client", e);
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
        logger.info("server is closed");
      }
    } catch (IOException e) {
      logger.warn("unable to close ServerSocket. It might already be closed", e);
    }
  }


}
