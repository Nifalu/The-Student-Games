package Client;

import java.io.*;
import java.net.Socket;


/**
 * This Thread receives and processes inputs from the server
 */
public class InThread implements Runnable {

  BufferedReader in;
  Socket socket;
  ClientProtokoll clientProtocol;
  private volatile boolean stop = false;

  public InThread(Socket socket, BufferedReader in, ClientProtokoll clientProtocol) {
    this.in = in;
    this.socket = socket;
    this.clientProtocol = clientProtocol;
  }

  @Override
  public void run() {
    String line;
    try {
      while (!stop) {
        Thread.sleep(0,200000);
        // reads incoming data
        if (in.ready()) { // this makes sure the following readLine() does not block.
          line = in.readLine();
          clientProtocol.sendToClient(line);
        }
      }
    } catch (IOException e) {
      System.out.println("InThread closed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // forces this thread to end.
  public void requestStop() {
    stop = true;
  }
}