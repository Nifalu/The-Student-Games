package Server;

public class ConnectionToClientMonitor implements Runnable{

  ClientHandler clientHandler;
  private volatile boolean stop = false;
  private volatile long lastReceivedPong = System.currentTimeMillis();
  private long timedOut;

  public ConnectionToClientMonitor(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }

  @Override
  public void run() {
    while (!stop && timedOut < 5000) {
      timedOut = Math.abs(System.currentTimeMillis() - lastReceivedPong);
      clientHandler.send("PING-" + System.currentTimeMillis());
      try {
        Thread.sleep(2500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (!stop) {
      System.out.println(clientHandler.user.getUsername() + " timed out. " + timedOut + "s");
      requestStop();
      clientHandler.disconnectClient();
    }

  }

  public void setLastReceivedPong(long time) {
    this.lastReceivedPong = time;
  }

  public void requestStop() {
    stop = true;
  }
}
