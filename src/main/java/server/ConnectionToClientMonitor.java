package server;

import utility.io.CommandsToClient;
import utility.io.SendToClient;

/**
 * Continuously checks the connection to the client, disconnects the client if it
 * does not respond for a given amount of time.
 */
public class ConnectionToClientMonitor implements Runnable {

  /**
   * the corresponding clienthandler
   */
  private final ClientHandler clientHandler;

  /**
   * notes whether stopped
   */
  private volatile boolean stop = false;

  /**
   * saves the time of the last received pong
   */
  private volatile long lastReceivedPong = System.currentTimeMillis();

  /**
   * saves the time of the last received ping
   */
  private long lastReceivedPing = System.currentTimeMillis();

  /**
   * the time out
   */
  private long timedOut;

  /**
   * SendToClient object used to communicate with the client
   */
  private final SendToClient sendToClient = new SendToClient();

  /**
   * creates a new ConnectionToClientMonitor object for a clienthandler
   *
   * @param clientHandler ClientHandler
   */
  public ConnectionToClientMonitor(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }


  /**
   * starts this thread and starts sending pings and checks for client timeouts.
   */
  @Override
  public synchronized void run() {

    // Sends the initial Ping to start the infinite loop on the client side
    try {
      Thread.sleep(100);
      sendToClient.send(clientHandler, CommandsToClient.INITIALPING, String.valueOf(System.currentTimeMillis()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    while (!stop && timedOut < 5000) {

      // timeouts a user when the difference between the current SystemTime and the
      // timestamp on the last received Pong is too big.
      timedOut = Math.abs(System.currentTimeMillis() - lastReceivedPong);
      sendToClient.send(clientHandler, CommandsToClient.PING, System.currentTimeMillis() + "-" + lastReceivedPing);
      try {
        wait(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (!stop) {
      // prints the timeout to the ServerConsole and disconnects the user
      System.out.println(clientHandler.user.getUsername() + " timed out. " + timedOut + "s");
      stop = true;
      clientHandler.disconnectClient();
    }
  }

  /**
   * Splits the message into the two timestamps and resets the lastReceivedPong for the ConnectionMonitor
   *
   * @param msg String
   */
  public void ping(String msg) {
    String[] pingpong = msg.split("-", 2);
    this.lastReceivedPong = Long.parseLong(pingpong[0]);
    this.lastReceivedPing = Long.parseLong(pingpong[1]);
  }

  /**
   * to let this thread finish and stop
   */
  public void requestStop() {
    stop = true;
  }
}
