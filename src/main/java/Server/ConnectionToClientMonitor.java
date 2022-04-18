package Server;

import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

/**
 * Continuously checks the connection to the Client, disconnects the Client if it
 * does not respond for a given amount of time.
 */
public class ConnectionToClientMonitor implements Runnable {

  private final ClientHandler clientHandler;
  private volatile boolean stop = false;
  private volatile long lastReceivedPong = System.currentTimeMillis();
  private long lastReceivedPing = System.currentTimeMillis();
  private long timedOut;
  private final SendToClient sendToClient = new SendToClient();

  public ConnectionToClientMonitor(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }


  /**
   * starts this thread and starts sending pings and checks for client timeouts.
   */
  @Override
  public synchronized void run() {

    // Sends the initial Ping to start the infinite loop on the Client side
    sendToClient.send(clientHandler, CommandsToClient.INITIALPING, String.valueOf(System.currentTimeMillis()));
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

  public void requestStop() {
    stop = true;
  }
}
