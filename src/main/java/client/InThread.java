package client;

import java.io.*;


/**
 * This Thread receives and processes inputs from the server
 */
public class InThread implements Runnable {

  /**
   * the in stream
   */
  private final BufferedReader in;

  /**
   * notes if stopped
   */
  private volatile boolean stop = false;

  /**
   * sets the InThread
   *
   * @param in InThread
   */
  public InThread(BufferedReader in) {
    this.in = in;
  }

  /**
   * Starts the thread and waits for incoming messages.
   */
  @Override
  public synchronized void run() {
    String line;
    try {
      while (!stop) {
        wait(0, 100000);
        // reads incoming data

        if (in.ready()) { // this makes sure the following readLine() does not block.
          line = in.readLine(); // reads line
          ClientReceive protocol = new ClientReceive(line);
          Thread ClientReceiveThread = new Thread(protocol);
          ClientReceiveThread.start();
          ClientReceiveThread.setName("ClientReceiveThread");
        }
      }
      System.out.println("server went offline");

    } catch (IOException e) {
      System.out.println("InThread closed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Forces this thread to end.
   */
  public void requestStop() {
    stop = true;
  }
}