package Client;

import java.io.*;
import java.net.Socket;


/**
 * This Thread receives and processes inputs from the server
 */
public class InThread implements Runnable {

  InputStream in;
  OutputStream out;
  Socket socket;
  ClientProtokoll clientProtocol;

  public InThread(Socket socket, InputStream in, OutputStream out, ClientProtokoll clientProtocol) {
    this.in = in;
    this.out = out;
    this.socket = socket;
    this.clientProtocol = clientProtocol;
  }

  @Override
  public void run() {

    int c;
    int i = 0;
    StringBuilder sb = new StringBuilder();

    try {
      while ((c = in.read()) != -1) {
        sb.append((char) c);
        if (sb.toString().charAt(i) == ';') {
          sb.delete(i, i + 1);

          clientProtocol.sendToClient(sb.toString());

          sb.delete(0, i + 1);
          i = 0;
        } else {
          i++;
        }
        Thread.sleep(1); // for more efficiency
      }
    } catch (IOException e) {
      // will be thrown when Client quits. Nothing needs to be done.
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}