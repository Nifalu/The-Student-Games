package Client;

import Game.Game;

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

    //Detects when the server loses connection
    try {
      while ((c = in.read()) != -1) {
        sb.append((char) c);
        if (sb.toString().charAt(i) == ';') {
          sb.delete(i, i + 1);
          String s = sb.toString();
          s = s.replace("ä","ae");
          s = s.replace("ö","oe");
          s = s.replace("ü","ue");
          clientProtocol.sendToClient(s);

          sb.delete(0, i + 1);
          i = 0;
        } else {
          i++;
        }
        Thread.sleep(1); // for more efficiency
      }
      System.out.println("Server went offline");

    } catch (IOException e) {
      // will be thrown when Client quits. Nothing needs to be done.
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}