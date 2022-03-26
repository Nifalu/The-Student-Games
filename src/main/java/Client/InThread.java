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

  public InThread(Socket socket, BufferedReader in, ClientProtokoll clientProtocol) {
    this.in = in;
    this.socket = socket;
    this.clientProtocol = clientProtocol;
  }

  @Override
  public void run() {
    String msg;
    while (true) {
      msg = receive();
      if (msg == null) {
        break;
      }
      clientProtocol.sendToClient(msg);
    }
  }


  public String receive() {
    String line;
    try {
      // reads incoming data
      line = in.readLine();
      return line;
      // if connection fails
    } catch (IOException e) {
      System.out.println("cannot reach Server");
    }
    return null;
  }
}