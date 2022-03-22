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

  public InThread(Socket socket, InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
    this.socket = socket;
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

          // Write here what should happen with the incoming message (sb.toString())
          System.out.println(sb);

          sb.delete(0, i + 1);
          i = 0;
        } else {
          i++;
        }
      }
    } catch (IOException e) {
      // does nothing. --> in.read() will always throw "Socket closed" Exception when leaving with "QUIT"
    }
  }
}