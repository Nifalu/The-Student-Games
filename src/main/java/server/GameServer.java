package server;


import gameLogic.GameList;
import gameLogic.Lobby;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToClient;
import utility.io.SendToClient;
import utility.io.UserBackup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static utility.Exceptions.failedToCreateServer;

/**
 * GameServer is the Container in which everything takes place.
 * Clients can connect to this GameServer with an Ip-Address and Port.
 */
public class GameServer {

  /**
   * the server connection
   */
  static ServerSocket serverSocket;

  public static boolean isonline = true;

  public static int port;

  private static final SendToClient sendToClient = new SendToClient();



  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(GameServer.class);

  static ServerConsoleIn serverconin;

  static Thread seconin;

  /**
   * Starts the GameServer on the given Port
   *
   * @param port int port
   */
  public static void runGameServer(int port) {
    GameServer.port = port;

    UserBackup.loadUsers();


    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      failedToCreateServer(port);
      return;
    }

    serverconin = new ServerConsoleIn();
    seconin = new Thread(serverconin);
    seconin.start();


    try {
      ServerManager.createMainLobby();

      logger.info("server is running! " + InetAddress.getLocalHost() + ":" + port);
      System.out.println("server created: " + InetAddress.getLocalHost() + ":" + port);
      System.out.println("server is running and waiting for a connection... ");

      int i = 0;

      while (isonline) {
        Socket socket = serverSocket.accept(); // program waits here until someone connects !
        // Each User gets his own thread
        if (isonline) {
          ClientHandler clientHandler = new ClientHandler(socket);
          Thread clientHandlerThread = new Thread(clientHandler);
          clientHandlerThread.setName("ClientHandlerThread" + i);
          clientHandlerThread.start();
          i++;
        }
      }
    } catch (Exception e) {
      closeGameServer();
    }
  }

  /**
   * closes the ServerSocket
   */
  public static void closeGameServer() {
    isonline = false;

    // Backup
    logger.info("backing up users");
    UserBackup.saveUsers();
    logger.info("finished backing up! Shutting down!");

    // Send goodbye and disconnect everyone
    sendToClient.serverBroadcast(CommandsToClient.CHAT, "[SERVER] SHUTTING DOWN");
    try {
      for (int i = 0; i >= 0; i--) {
        sendToClient.serverBroadcast(CommandsToClient.CHAT, "[SERVER] " + i);
        TimeUnit.SECONDS.sleep(1);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    disconnectAllClients();

    // Close Lobby Receiver Threads
    for (HashMap.Entry<Integer, Lobby> entry : GameList.getLobbyList().entrySet()) {
      entry.getValue().stopLobby();
    }

    try {
      Socket s = new Socket("localhost", port); // unblocks the serversocket.accept() line so its method can finish
      serverconin.stop();
      serverSocket.close();
      logger.info("server closed");
    } catch (IOException e) {
      logger.error("unable to close ServerSocket. It might already be closed", e);
    }
  }

  private static void disconnectAllClients() {
    if (ServerManager.getActiveClientList().size() != 0) {
      ServerManager.getActiveClientList().get(0).disconnectClient();
      disconnectAllClients();
    } else {
      System.out.println("all clients disconnected");
    }
  }
}