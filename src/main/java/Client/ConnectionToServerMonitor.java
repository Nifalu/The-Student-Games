package Client;

import utility.CommandsToServer;
import utility.SendToServer;


/**
 * Continuously checks the connection to the Server, disconnects the Client if the Server
 * does not respond for a given amount of time.
 */
public class ConnectionToServerMonitor implements Runnable {

  private volatile boolean stop = false;
  private volatile long lastReceivedPong = System.currentTimeMillis();
  private boolean initialPing = false;
  private long timedOut;

  private final SendToServer sendToServer = new SendToServer();

  @Override
  public synchronized void run() {
    try {
      // waits for an initial ping to be received.
      // When no Ping is received for a given amount of time, the client disconnects.
      int timer = 0; // 10 steps are around 1 second.
      while (!initialPing && timer < 500) {
        wait(100);
        timer += 1;
      }

      // timeouts a user when the difference between the current SystemTime and the
      // timestamp on the last received Pong is too big.
      while (!stop && timedOut < 5000) {
        timedOut = Math.abs(System.currentTimeMillis() - lastReceivedPong);
        wait(0, 100000);
      }
      if (!stop) {
        // prints the timeout to the ServerConsole and disconnects the user
        System.out.println("Timed out. " + timedOut + "s");
        ClientManager.disconnect();
        requestStop();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  /**
   * Splits the message into the two timestamps resets the lastReceivedPong for the ConnectionMonitor
   * and returns a Ping to the Server with a new Timestamp.
   *
   * @param msg String
   */
  protected void newPing(String msg) {
    String[] pingpong = msg.split("-", 2);
    this.lastReceivedPong = Long.parseLong(pingpong[1]);
    sendToServer.send(CommandsToServer.PING, pingpong[0] + "-" + System.currentTimeMillis());
  }

  /**
   * Receives an initial Ping to tell the Client that there is a connection.
   *
   * @param msg String
   */
  protected void start(String msg) {
    this.initialPing = true;
    sendToServer.send(CommandsToServer.PING, msg + "-" + System.currentTimeMillis());
  }

  /**
   * Stops the infinite Loop so the Thread can finish.
   */
  protected void requestStop() {
    stop = true;
  }
}
