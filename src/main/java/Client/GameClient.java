package Client;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * This is the GameClient. Right now it only reads the keyboard input from the console and sends it to the Server.
 */
public class GameClient {

  String serverAddress;
  int serverPort;
  BufferedReader in;
  BufferedWriter out;
  Socket socket;
  ClientProtokoll clientProtocol;
  Thread clientIn;
  Thread conin;

  public GameClient(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.clientProtocol = new ClientProtokoll(this);

    // Connection to the server is made and in/out streams are created:
    try {
      this.socket = new Socket(serverAddress, serverPort);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

      // Thread to handle incoming data:
      InThread th = new InThread(socket, in, clientProtocol);
      clientIn = new Thread(th);
      clientIn.start();


      // Thread to read console Input:
      ConsoleInput consoleInput = new ConsoleInput(clientProtocol);
      conin = new Thread(consoleInput);
      conin.start();

    } catch (IOException e) {
      disconnect();
    }

  }



  /**
   * Does Everything that needs to be done when the client disconnects.
   */
  public void disconnect() {
    System.out.println("terminating...");
    try {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      if (conin.isAlive()) {
        conin.interrupt();
      }
      if (clientIn.isAlive()) {
        clientIn.interrupt();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
