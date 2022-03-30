package Client;

public class ConnectionToServerMonitor implements Runnable{

  private volatile boolean stop = false;
  private volatile long lastReceivedPong = System.currentTimeMillis();
  private long timedOut;

  GameClient client;
  ClientProtocol protocol;

  public ConnectionToServerMonitor(GameClient client, ClientProtocol protocol) {
    this.client = client;
    this.protocol = protocol;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(15000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    lastReceivedPong = System.currentTimeMillis();
    while (!stop && timedOut < 5000) {
      timedOut = Math.abs(System.currentTimeMillis() - lastReceivedPong);
      protocol.send("PING-" + System.currentTimeMillis());
      try {
        Thread.sleep(2500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (!stop) {
      System.out.println("Timed out. " + timedOut + "s");
      client.disconnect();
      requestStop();
    }
  }

  public void setLastReceivedPong(long time) {
    this.lastReceivedPong = time;
  }

  public void requestStop() {
    stop = true;
  }
}
